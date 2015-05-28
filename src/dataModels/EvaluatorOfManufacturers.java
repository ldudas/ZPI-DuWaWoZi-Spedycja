package dataModels;

import java.awt.Color;
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
		evaluateMonthActivityOfManufacturers(manufacturers);
		evaluateQuarterActivityOfManufacturers(manufacturers);
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
			if(activityOfManufacturers.get(i).equals( theBestEvaluation )  )
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
	 * Metoda obliczajaca i ustawiajaca kolory odpowiadajace aktywnosci producentow 
	 * kazdego miesiaca roku
	 * @param manufacturers
	 * @author Kamil Zimny
	 */
	private void evaluateMonthActivityOfManufacturers(ArrayList<Manufacturer> manufacturers)
	{
		ArrayList<Color> activityColors = new ArrayList<Color>(12);
		int activityValueOfTheWorst = 255;
		int activityValueOfTheBest = 0;
		double theBestEvaluation = -1;
		int activityColorEvaluation = 0;
		
		for(Manufacturer man : manufacturers)
		{
			activityValueOfTheBest = 0;
			theBestEvaluation = -1;
			for(int i=0; i<12 ;i++)
			{
				if(theBestEvaluation < man.getMonthActivity(i) )
					theBestEvaluation = man.getMonthActivity(i);		
			}
			
			for(int i=0; i<12 ;i++)
			{
				if(man.getMonthActivity(i) == ( theBestEvaluation )  )
					activityColorEvaluation = activityValueOfTheBest;
				else	
					activityColorEvaluation = (int) (activityValueOfTheWorst - man.getMonthActivity(i)/theBestEvaluation*activityValueOfTheWorst);
				
				activityColors.add(new Color(255,activityColorEvaluation,activityColorEvaluation) );
			}
			
			man.setMonthsActivityColors(activityColors);
			activityColors.clear();
		}
	}
	
	/***
	 * Metoda obliczajaca i ustawiajaca kolory odpowiadajace aktywnosci producentow 
	 * w kazdym kwartale 
	 * @param manufacturers
	 * @author Kamil Zimny
	 */
	private void evaluateQuarterActivityOfManufacturers(ArrayList<Manufacturer> manufacturers)
	{

		ArrayList<Color> activityColors = new ArrayList<Color>(4);
		int activityValueOfTheWorst = 255;
		int activityValueOfTheBest = 0;
		double theBestEvaluation = -1;
		int activityColorEvaluation = 0;
		
		for(Manufacturer man : manufacturers)
		{
			activityValueOfTheBest = 0;
			theBestEvaluation = -1;
			for(int i=0; i<4 ;i++)
			{
				if(theBestEvaluation < man.getQuarterActivity(i) )
					theBestEvaluation = man.getQuarterActivity(i);		
			}
			
			for(int i=0; i<4 ;i++)
			{
				if(man.getQuarterActivity(i) == ( theBestEvaluation )  )
					activityColorEvaluation = activityValueOfTheBest;
				else	
					activityColorEvaluation = (int) (activityValueOfTheWorst - man.getQuarterActivity(i)/theBestEvaluation*activityValueOfTheWorst);
				
				activityColors.add(new Color(255,activityColorEvaluation,activityColorEvaluation) );
			}
			
			man.setQuarterActivityColor(activityColors);
			activityColors.clear();
		}
	}
	
}
