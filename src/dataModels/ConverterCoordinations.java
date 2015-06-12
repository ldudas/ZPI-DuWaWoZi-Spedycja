package dataModels;

public class ConverterCoordinations 
{

	public ConverterCoordinations()
	{	
	}
	
	/**
	 * Zamiana wspolrzednej geograficznej na liczbe dziesietna 
	 * odpowiadajaca tej wspolrzednej (tylko stopnie i minuty).
	 * @return double coordiante
	 * @author Piotr Wołoszyk
	 */
	public double parseCoordinate(String coordinate)
	{        
		if(coordinate.isEmpty())
			return -1;
		int index1 = coordinate.indexOf("°");
		int index2 = coordinate.indexOf("'");
		String part1 = coordinate.substring(0,index1);
		String part2 = coordinate.substring(index1+1,index2);	  
		double a = Double.parseDouble(part1);
	    double b = Double.parseDouble(part2);
	    double result = a + (b/60);
	      
	    if(coordinate.contains("?"))
	    {
	    	int index3 = coordinate.indexOf("?"); 
	        String part3 = coordinate.substring(index2+1,index3);
	        double c = Double.parseDouble(part3);
	        result += c/3600;
	    }
	    
	    return result;
	}
}
