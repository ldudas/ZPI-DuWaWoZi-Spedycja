package specification;

import dataModels.SizeCategory;
import dataModels.Transporter;


public class TransporterSizeCategorySpecification implements Specification<Transporter>
{

	private SizeCategory sc;
	
	public TransporterSizeCategorySpecification(SizeCategory sc)
	{
		this.sc=sc;
	}

	@Override
	public boolean isSatisfiedBy(Transporter t) 
	{
		return (t.getSizeCategory() == sc || sc == SizeCategory.ALL);
	}
	
	
}
