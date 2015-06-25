package specification;

/**
 * Inteface do wzorca specyfikacji
 * @author Łukasz Dudaszek
 */
public interface Specification<T>
{
	  boolean isSatisfiedBy(T t); 
}