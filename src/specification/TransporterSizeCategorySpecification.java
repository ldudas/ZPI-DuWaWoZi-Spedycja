package specification;

import dataModels.SizeCategory;
import dataModels.Transporter;

/**
 * Klasa specyfikacji przewoźnika ze względu na podaną kategorię rozmiaru
 * @author Łukasz Dudaszek
 */
public class TransporterSizeCategorySpecification implements Specification<Transporter>
{

	/**
	 * kategoria rozmiaru
	 */
	private SizeCategory sc;
	
	public TransporterSizeCategorySpecification(SizeCategory sc)
	{
		this.sc=sc;
	}

	/**
	 * sprawdzenie czy podany przewoźnik spełnia wymagnia
	 */
	@Override
	public boolean isSatisfiedBy(Transporter t) 
	{
		return (t.getSizeCategory() == sc || sc == SizeCategory.ALL);
	}
	
	
}
