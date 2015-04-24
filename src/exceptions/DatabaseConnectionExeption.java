/*
 * @author [Kamil Zimny]
 */
package exceptions;

public class DatabaseConnectionExeption extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatabaseConnectionExeption(String msg)
	{
		super(msg);
	}

}
