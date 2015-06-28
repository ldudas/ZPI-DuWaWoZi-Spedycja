package database;

/**
 * Fabryka obkietów Data Access Object
 * @author Kamil Zimny
 *
 */
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
	
	/**
	 * Metoda zwracajaca referencje do DataAccessObjectPathVisualisation.
	 * @return DataAccessObjectPathVisualisation
	 * @author Kamil Zimny
	 */
	public DataAccessObjectPathVisualisation getDataAccessObjectPathVisualisation()
	{
		return new DataAccessObjectPathVisualisation();
	}
	
	/**
	 * Metoda zwracajaca referencje do DataAccessObjectTransportersVisualisation.
	 * @return DataAccessObjectTransportersVisualisation
	 * @author Kamil Zimny
	 */
	public DataAccessObjectTransportersVisualisation getDataAccessObjectTransportersVisualisation()
	{
		return new DataAccessObjectTransportersVisualisation();
	}
}
