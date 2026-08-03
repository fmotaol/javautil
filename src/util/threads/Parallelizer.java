package util.threads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.BiFunction;
import java.util.function.Function;

import util.tables.Map2K;
import util.tables.MultiLevelHashMap2K;

public class Parallelizer {

	private ExecutorService executor;

	public enum ErrorPolicy {
		STOP, AFTER_ALL, IGNORE
	};

	private ErrorPolicy errorPolicy = ErrorPolicy.AFTER_ALL;

	public Parallelizer(ExecutorService executor) {
		super();
		this.executor = executor;
	}

	public Parallelizer(int threadPoolSize) {
		super();
		this.executor = Executors.newFixedThreadPool(threadPoolSize);
		//shutdownExecutor = true;
	}

	public ExecutorService getExecutor() {
		if (executor == null)
			throw new RuntimeException("Executor was not initialized");
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public ErrorPolicy getErrorPolicy() {
		return errorPolicy;
	}

	public void setErrorPolicy(ErrorPolicy errorPolicy) {
		this.errorPolicy = errorPolicy;
	}

	public <T> Future<T> call(Callable<T> callable) {
		FutureTask<T> task = new FutureTask<T>(callable);
		getExecutor().execute(task);
		return task;
	}

	public <T> T callAndWait(Callable<T> callable) {
		return callAndWait(callable, 2000);
	}

	public <T> T callAndWait(Callable<T> callable, int cyclems) {
		Future<T> r = call(callable);

		try {

			do {
				Thread.sleep(cyclems);
			} while (!r.isDone());

			return r.get();

		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException("Erro ao obter resultado", e);
		}
	}

//	public void runAndWait(Runnable runnable) {
//		runAndWait(runnable, 200);
//	}
//	
//	public <T> T runAndWait(Runnable runnable, int cyclems) {
//	    run(runnable);
//	    
//	    do {
//	        try {
//	            Thread.sleep(cyclems); // Espera cyclems antes de verificar novamente
//	        } catch (InterruptedException e) {
//	            Thread.currentThread().interrupt();
//	            throw new RuntimeException("Thread interrompida enquanto aguardava", e);
//	        }
//	        
//	    } while (!runnable.isDone());
//	    
//	}

	public <I, O> Map<I, O> process(Collection<I> input, Function<I, O> process) {
		HashMap<I, O> r = new HashMap<>();
		HashMap<I, FutureTask<O>> tasks = new HashMap<>();
		for (I i : input) {
			FutureTask<O> f = new FutureTask<>(() -> {
				return process.apply(i);
			});
			executor.execute(f);
			tasks.put(i, f);
		}

		ArrayList<Throwable> errors = errorPolicy == ErrorPolicy.AFTER_ALL ? new ArrayList<>() : null;

		for (Entry<I, FutureTask<O>> e : tasks.entrySet()) {
			O v = safeGetParallelValue(e.getValue(), errors);
			r.put(e.getKey(), v);
		}

		if (errors != null)
			for (Throwable e : errors)
				throw new RuntimeException(e);

		return r;
	}

	public <I, O> O[] process(I[] input, Function<I, O> process, O[] outputTemplate) {
		Future<O>[] fs = (Future<O>[]) new Future<?>[input.length];
		for (int i = 0; i < input.length; i++) {
			I in = input[i];
			FutureTask<O> f = new FutureTask<O>(() -> process.apply(in));
			executor.execute(f);
			fs[i] = f;
		}

		ArrayList<Throwable> errors = errorPolicy == ErrorPolicy.AFTER_ALL ? new ArrayList<>() : null;

		O[] r = Arrays.copyOf(outputTemplate, input.length);
		for (int i = 0; i < r.length; i++) {
			r[i] = safeGetParallelValue(fs[i], errors);
		}

		if (errors != null)
			for (Throwable e : errors)
				throw new RuntimeException(e);

		return r;
	}

	public <I1, I2, O> Map2K<I1, I2, O> process(Collection<I1> input1, Collection<I2> input2,
			BiFunction<I1, I2, O> process) {

		MultiLevelHashMap2K<I1, I2, Future<O>> fs = new MultiLevelHashMap2K<>();

		for (I1 i1 : input1) {
			for (I2 i2 : input2) {
				FutureTask<O> f = new FutureTask<O>(() -> process.apply(i1, i2));
				executor.execute(f);
				fs.put(i1, i2, f);
			}
		}

		ArrayList<Throwable> errors = errorPolicy == ErrorPolicy.AFTER_ALL ? new ArrayList<>() : null;
		MultiLevelHashMap2K<I1, I2, O> r = new MultiLevelHashMap2K<>();

		for (I1 i1 : input1) {
			for (I2 i2 : input2) {
				Future<O> res = fs.get(i1, i2);
				O value = null;

				value = safeGetParallelValue(res, errors);
				r.put(i1, i2, value);
			}
		}

		if (errors != null)
			for (Throwable e : errors)
				throw new RuntimeException(e);

		return r;
	}

	private <O> O safeGetParallelValue(Future<O> futureResult, ArrayList<Throwable> errors) {
		O value;
		try {

			value = futureResult.get();
			return value;

		} catch (InterruptedException | ExecutionException e) {

			if (errorPolicy == ErrorPolicy.AFTER_ALL)
				errors.add(e);
			else if (errorPolicy == ErrorPolicy.IGNORE)
				return null;

			throw new RuntimeException(e);

		}
	}

	private ArrayList<Runnable> running = new ArrayList<>();
	private boolean finished = false;

	public List<Runnable> running() {
		return Collections.unmodifiableList(running);
	}

	public void run(Runnable run) {
		if (running.contains(run))
			throw new RuntimeException("Runnable % já está em execução");
		running.add(run);
		Runnable newRun = () -> {
			try {
				run.run();
			} finally {
				running.remove(run);
			}
		};
		getExecutor().execute(newRun);
	}

	public void waitAllFinish() {
		ThreadUtil.waitFor(() -> running.isEmpty());
	}

	//private boolean shutdownExecutor = true;

	public void shutdown() {
		finished  = true;
		running.clear();
//		for (Runnable r : running) {
//			r.shutdown();
//		}
		executor.shutdown();
	}

	public boolean finished() {
		return finished;
	}

}
