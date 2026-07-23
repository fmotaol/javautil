package util.logical;
import java.util.Collection;
import java.util.function.Predicate;

public class Predicates {

	public static <T> Predicate<T> isIn(Collection<T> col) {
		Predicate<T> r = new Predicate<T>() {
			
			@Override
			public boolean test(T p) {
				return col.contains(p);
			}
		};
		
		return r;
	}


}
