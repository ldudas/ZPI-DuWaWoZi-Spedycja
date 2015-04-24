package dataModels;

import java.util.ArrayList;

public class EvaluatorOfManufacturers 
{
	
	public EvaluatorOfManufacturers()
	{		
	}
	
	public void evaluateManfacturersCollection(ArrayList<Manufacturer> manufacturers)
	{
		evaluateActivityOfManufacturers(manufacturers);
		evaluateSizeOfManufacturers(manufacturers);
	}
	
	/**
	 * Metoda obliczajaca aktywnosc kazdego producenta na podstawie danych pobranych z bazy.
	 * @return ArrayList<Double> 
	 * <br> Tablica wartosci obliczonych dla kazdego producenta.
	 * @author Kamil Zimny
	 */
	private void evaluateActivityOfManufacturers(ArrayList<Manufacturer> manufacturers)
	{
		int activityValueOfTheBest = 0;
		int activityValueOfTheWorst = 255;
		
		ArrayList<Double> activityOfManufacturers = new ArrayList<Double>();
		double theBestEvaluation = -1;
		for(int i=0; i<manufacturers.size() ; i++)
		{
			double totalValue = manufacturers.get(i).getSumOfOrdersValue();
			int totalD = manufacturers.get(i).getSumOfDays();
			double evaluationOfManufacturers = totalValue/totalD;
			
			if( evaluationOfManufacturers > theBestEvaluation)
				theBestEvaluation = evaluationOfManufacturers;
			
			activityOfManufacturers.add( evaluationOfManufacturers ); 
		}

		for(int i=0; i<manufacturers.size() ; i++)
		{
			double evaluationOfManufacturers;
			if(activityOfManufacturers.get(i) ==  theBestEvaluation)
				evaluationOfManufacturers = activityValueOfTheBest;
			else	
				evaluationOfManufacturers = activityValueOfTheWorst - activityOfManufacturers.get(i)/theBestEvaluation*activityValueOfTheWorst;
			manufacturers.get(i).setRankOfDailyProfit((int)evaluationOfManufacturers);
		}
	}
	
	/**
	 * Metoda obliczajaca awielkosc kazdego producenta na podstawie danych pobranych z bazy.
	 * @return ArrayList<Double> 
	 * <br> Tablica wartosci obliczonych dla kazdego producenta.
	 * @author Kamil Zimny
	 */
	private void evaluateSizeOfManufacturers(ArrayList<Manufacturer> manufacturers)
	{
		int sizeValueOfTheBest = 50;
		int sizeValueOfTheWorst = 20;
		
		ArrayList<Double> sizesOfManufacturers = new ArrayList<Double>();
		double theBestEvaluation = -1;
		for(int i=0; i<manufacturers.size() ; i++)
		{
			int numberOfOr = manufacturers.get(i).getNumberOfOrders();
			double evaluationOfManufacturers = numberOfOr;
			
			if( evaluationOfManufacturers > theBestEvaluation)
				theBestEvaluation = evaluationOfManufacturers;
			
			sizesOfManufacturers.add( evaluationOfManufacturers ); 
		}

		for(int i=0; i<manufacturers.size() ; i++)
		{
			double evaluationOfManufacturers;
			if(sizesOfManufacturers.get(i) ==  theBestEvaluation)
				evaluationOfManufacturers = sizeValueOfTheBest;
			else	
			{
				evaluationOfManufacturers = sizesOfManufacturers.get(i)/theBestEvaluation*sizeValueOfTheBest;
				if( evaluationOfManufacturers < 20 )
					evaluationOfManufacturers = sizeValueOfTheWorst;
			}
			manufacturers.get(i).setRankOfNumberOfOrders((int)evaluationOfManufacturers);
		}

	}
}
