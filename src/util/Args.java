package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import util.logical.Assert;

public class Args {

	private String[] args;
	
	public Args(String[] args) {
		this.args = args;
	}

	public String get(int index) {
		String r = extractArg(index);
		return r;
	}

	public <T> T get(int index, T defaultValue) {
		String a = extractArg(index, Strings.toString(defaultValue));
		T r = parse(a, defaultValue);
		return r;
	}

	public <T> T get(int index, Function<String, T> parser) {
		return get(index, parser, null);
	}

	public <T> T get(int index, Function<String, T> parser, T defaultValue) {
		String a = extractArg(index);
		if (a == null)
			return defaultValue;
		T r;
		if (parser == null)
			r = parse(a, defaultValue);
		else
			r = parser.apply(a);
		return r;
	}

	private String extractArg(int index, String defaultValue) {
		if (index >= args.length) {
			return defaultValue;
		}
		return args[index];
	}

	private String extractArg(int index) {
		if (index >= args.length) {
			throw new RuntimeException("argumento inexistente");
		}
		return args[index];
	}
	
	public boolean caseSensitive = true;
	
	public String get(String argName) {
		String r = extractArg(argName);
		return r;
	}

	public <T> T get(String argName, T defaultValue) {
	    String a = extractArg(argName);
	    T r = parse(a, defaultValue);
	    return r;
	}	

	public <T> T get(String argName, Function<String, T> parser) {
		return get(argName, parser, null);
	}

	public <T> T get(String argName, Function<String, T> parser, T defaultValue) {
		String a = extractArg(argName);
		if (a == null)
			return defaultValue;
		T r;
		if (parser == null)
			r = parse(a, defaultValue);
		else
			r = parser.apply(a);
		return r;
	}

	private String extractArg(String arg) {
		int i = indexOfArg(arg);
		if (i == -1)
			return null;
		String a = args[i];
		String[] ss = a.split("=", 2);
		if (ss.length <= 1)
			return null;
		return ss[1];
	}

	private int indexOfArg(String arg) {
		for (int i = 0; i < args.length; i++) {
			String s = args[i];
			if (s == null)
				continue;
			if (!caseSensitive) {
				s = s.toLowerCase();
				arg = arg.toLowerCase();
			}

			String[] ss = s.split("=", 2);
			//if (ss.length < 2)
				//continue;

			if (ss[0].equals(arg))
				return i;
		}
		return -1;
	}

	
	public void assertCount(int total) {
		Assert.that(args.length == total, "Comando deve ter " + total + " argumentos");
	}

	public void assertCount(int min, int max) {
		Assert.that(args.length >= min || args.length <= max,
				"Comando deve ter entre " + min + " e " + max + " argumentos");
	}

	public Args sub(int start) {
		Args r = new Args(Colls.arrayPart(args, start));
		return r;
	}

	public int length() {
		return args.length;
	}
	
	@Override
	public String toString() {
		return "Args [args=" + Arrays.toString(args) + "]";
	}

	public boolean contains(String value) {
		return Strings.contains(args, value, !caseSensitive);
	}

	public boolean containsArg(String arg) {
		return indexOfArg(arg) > -1;
	}

	public List<String> getStartingWith(String s) {
		ArrayList<String> r = new ArrayList<>();
		if (!caseSensitive) 
			s = s.toLowerCase();
		
		for (String a : args) {
			String eval = a;
			if (!caseSensitive) 
				eval = eval.toLowerCase();				
			
			if (eval.startsWith(s))
				r.add(a);
		}
		return r;
	}
	
	public List<String> getEndingWith(String s) {
		ArrayList<String> r = new ArrayList<>();
		if (!caseSensitive) 
			s = s.toLowerCase();
		
		for (String a : args) {
			String eval = a;
			if (!caseSensitive) 
				eval = eval.toLowerCase();				
			
			if (eval.endsWith(s))
				r.add(a);
		}
		return r;
	}
	
	public static <T> T parse(String rawValue, T defaultValue) {
		Class<T> clazz = null;
		if (defaultValue == null) {
			if (rawValue == null)
				return null;
			else
				return (T) rawValue;
		} else
			clazz = (Class<T>) defaultValue.getClass();

		if (rawValue == null)
			return defaultValue;
		else
			return parseByClass(rawValue, clazz);
	}

	public static <T> T parseByClass(String rawValue, Class<T> clazz) {
		if (clazz == String.class)
			return (T) rawValue;
		if (clazz == Integer.class)
			return (T) (Integer) Integer.parseInt(rawValue);
		if (clazz == Float.class)
			return (T) (Float) Float.parseFloat(rawValue);
		if (clazz == Long.class)
			return (T) (Long) Long.parseLong(rawValue);
		if (clazz == Double.class)
			return (T) (Double) Double.parseDouble(rawValue);
		if (clazz == Boolean.class)
			return (T) (Boolean) Boolean.parseBoolean(rawValue);
		if (clazz == Date.class)
			return (T) Dates.parse(rawValue);
		throw new RuntimeException("Tipo não suportado: " + clazz);
	}

	

}
