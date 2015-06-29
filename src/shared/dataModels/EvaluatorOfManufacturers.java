package shared.dataModels;

import java.awt.Color;
import java.util.ArrayList;

public class EvaluatorOfManufacturers 
{
	
	public EvaluatorOfManufacturers()
	{		
	}
	
	/**
	 * Wywołuje metody wykonujące obliczenia na kolekcji producentów. 
	 * @param manufacturers
	 */
	public void evaluateManfacturersCollection(ArrayList<Manufacturer> manufacturers)
	{
		evaluateActivityOfManufacturers(manufacturers);
		evaluateSizeOfManufacturers(manufacturers);
		evaluateMonthActivityOfManufacturers(manufacturers);
		evaluateQuarterActivityOfManufacturers(manufacturers);
	}
	
	/**
	 * Metoda obliczająca aktywność każdego producenta na podstawie danych pobranych z bazy.
	 * Opłacalność danego producenta.
	 * @return ArrayList<Double> 
	 * <br> Tablica wartosci obliczonych dla kazdego producenta.
	 * @author Kamil Zimny
	 */
	private void evaluateActivityOfManufacturers(ArrayList<Manufacturer> manufacturers)
	{
		int activityValueOfTheWorst = 255;
		
		int red = 0;
		int green = 0;
		int blue = 0;
		
		int valueOfChange = 153;
		
		ArrayList<Double> activityOfManufacturers = new ArrayList<Double>();
		double theBestEvaluation = -1;
		double theWorstEvaluation = Double.MAX_VALUE;
		for(int i=0; i<manufacturers.size() ; i++)
		{
			double totalValue = manufacturers.get(i).getSumOfOrdersValue();
			int totalD = manufacturers.get(i).getSumOfDays();
			double evaluationOfManufacturers = totalValue/totalD;
			
			if( evaluationOfManufacturers > theBestEvaluation)
				theBestEvaluation = evaluationOfManufacturers;
			
			if( evaluationOfManufacturers < theWorstEvaluation )
				theWorstEvaluation = evaluationOfManufacturers;
			
			activityOfManufacturers.add( evaluationOfManufacturers ); 
		}

		for(int i=0; i<manufacturers.size() ; i++)
		{
			double evaluationOfManufacturers;
			evaluationOfManufacturers = activityValueOfTheWorst - (activityOfManufacturers.get(i) - theWorstEvaluation )
										/(theBestEvaluation-theWorstEvaluation)*activityValueOfTheWorst;
			
			if( evaluationOfManufacturers <= valueOfChange )
			{
				red = 255;
				green = blue = (int) evaluationOfManufacturers;
			}
			else
			{
				int nextEvaluation = (int) (evaluationOfManufacturers - valueOfChange);
				red = green = valueOfChange - nextEvaluation;
				blue = 255;	
			}
		
			manufacturers.get(i).setRankOfDailyProfit(new Color(red,green,blue));
		}
	}
	
	
	/**
	 * Metoda obliczająca wielkośc kazdego producenta na podstawie danych pobranych z bazy.
	 * Wielkość jest liczbą zleceń.
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
			if(sizesOfManufacturers.get(i).equals( theBestEvaluation) )
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
	
	/***
	 * Metoda obliczająca i ustawiająca kolory odpowiadające zarobkowi producentów 
	 * każdego miesiąca w roku
	 * @param manufacturers
	 * @author Kamil Zimny
	 */
	private void evaluateMonthActivityOfManufacturers(ArrayList<Manufacturer> manufacturers)
	{
		ArrayList<Color> activityColors = new ArrayList<Color>(12);
		int activityValueOfTheWorst = 255;
		double theBestEvaluation = -1;
		double theWorstEvaluation = Double.MAX_VALUE;
		
		int red = 0;
		int green = 0;
		int blue = 0;
		
		int valueOfChange = 153;
		
		
		for(Manufacturer man : manufacturers)
		{
			theBestEvaluation = -1;
			theWorstEvaluation = Double.MAX_VALUE;
			for(int i=0; i<12 ;i++)
			{
				if(theBestEvaluation < man.getMonthCost(i) )
					theBestEvaluation = man.getMonthCost(i);	
				if(theWorstEvaluation > man.getMonthCost(i) )
					theWorstEvaluation = man.getMonthCost(i);	
			}
			
			for(int i=0; i<12 ;i++)
			{
				double evaluationOfManufacturers;
				evaluationOfManufacturers = activityValueOfTheWorst - (man.getMonthCost(i) - theWorstEvaluation )
											/(theBestEvaluation-theWorstEvaluation)*activityValueOfTheWorst;

				if( evaluationOfManufacturers <= valueOfChange )
				{
					red = 255;
					green = blue = (int) evaluationOfManufacturers;
				}
				else
				{
					int nextEvaluation = (int) (evaluationOfManufacturers - valueOfChange);
					red = green = valueOfChange - nextEvaluation;
					blue = 255;			
				}
			
				activityColors.add(new Color(red,green,blue) );
			}
			
			man.setMonthsCostColors(activityColors);
			activityColors.clear();
		}
	}
	
	/***
	 * Metoda obliczająca i ustawiająca kolory odpowiadające aktywności producentów 
	 * w każdym kwartale w roku
	 * @param manufacturers
	 * @author Kamil Zimny
	 */
	private void evaluateQuarterActivityOfManufacturers(ArrayList<Manufacturer> manufacturers)
	{

		ArrayList<Color> activityColors = new ArrayList<Color>(4);
		int activityValueOfTheWorst = 255;
		double theBestEvaluation = -1;
		double theWorstEvaluation = Double.MAX_VALUE;
		
		int red = 0;
		int green = 0;
		int blue = 0;
		
		int valueOfChange = 153;
		
		for(Manufacturer man : manufacturers)
		{
			theBestEvaluation = -1;
			theWorstEvaluation = Double.MAX_VALUE;
			for(int i=0; i<4 ;i++)
			{
				if(theBestEvaluation < man.getQuarterCost(i) )
					theBestEvaluation = man.getQuarterCost(i);	
				if(theWorstEvaluation > man.getQuarterCost(i) )
					theWorstEvaluation = man.getQuarterCost(i);	
			}
			
			for(int i=0; i<4 ;i++)
			{
				double evaluationOfManufacturers;
				evaluationOfManufacturers = activityValueOfTheWorst - (man.getQuarterCost(i) - theWorstEvaluation )
											/(theBestEvaluation-theWorstEvaluation)*activityValueOfTheWorst;

				if( evaluationOfManufacturers <= valueOfChange )
				{
					red = 255;
					green = blue = (int) evaluationOfManufacturers;
				}
				else
				{
					int nextEvaluation = (int) (evaluationOfManufacturers - valueOfChange);
					red = green = valueOfChange - nextEvaluation;
					blue = 255;					
				}
				
				activityColors.add(new Color(red,green,blue) );
			}
			
			man.setQuarterCostColor(activityColors);
			activityColors.clear();
		}
	}
	
}
