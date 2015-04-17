/**
 * @author[Kamil Zimny]
 */
package database;

public class DataAccessObjectFactory 
{
	/**
	 * Metoda zwracajaca referencje do DataAccessObjectManufacturersVisualisation.
	 * @return DataAccessObjectManufacturersVisualisation
	 * @author Kamil Zimny
	 */
	public DataAccessObjectManufacturersVisualisation getDataAccessObjectManufacturersVisualisation()
	{
		return new DataAccessObjectManufacturersVisualisation();
	}
	
	public DataAccessObjectPathVisualisation getDataAccessObjectPathVisualisation()
	{
		return new DataAccessObjectPathVisualisation();
	}
}
