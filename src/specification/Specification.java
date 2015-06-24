package specification;

public interface Specification<T>
{
	  boolean isSatisfiedBy(T t); 
}