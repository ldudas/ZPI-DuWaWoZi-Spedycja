package visualisations.Transporters;

import java.util.ArrayList;
import java.util.stream.Collectors;

import dataModels.City;
import dataModels.SizeCategory;
import dataModels.Transporter;
import database.DataAccessObjectFactory;
import database.DataAccessObjectPathVisualisation;
import database.DataAccessObjectTransportersVisualisation;

public class VisualisationTransportersModel 
{
	
	/**
	 * Data Access Object dla wizualizacji przewoznikow
	 */
	private DataAccessObjectTransportersVisualisation DAO_TransVis;
	
	/**
	 * Miasto początkowe
	 */
	private String cityFrom;
	
	/**
	 * Miasto końcowe
	 */
	private String cityTo;
	
	/**
	 * Wszyscy przeoźnicy z miasta city_form do city_to
	 */
	private ArrayList<Transporter> transporters;
	
	/**
	 * Przeoźnicy z miasta city_form do city_to przefiltrowani wg prefernecji rozmiaru i typu specjalnego (np. chłodnia)
	 */
	private ArrayList<Transporter> transporters_filtered;
	
	/**
	 * wybrana kategoria rozmiaru do filtrowania
	 */
	private SizeCategory size_category;
	
	/**
	 * wybrany typ pojazdu do filtrowania
	 */
	private String type_of_vechicle;
	
	public VisualisationTransportersModel()
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_TransVis = factory.getDataAccessObjectTransportersVisualisation();
		transporters = new ArrayList<Transporter>();
		transporters_filtered = new ArrayList<Transporter>();
		cityFrom="";
		cityTo="";
		size_category = null;
	}
	
	private void getTranspotersFormDatabase(String city_from, String city_to)
	{
		//pobieranie listy przewoźnikow z bazy 
		//transporters = DAO_TransVis.getTranspoters(String city_from, String city_to);
		//na razie tworzę sam
		///
				transporters.clear();
				transporters.add(new Transporter(1,SizeCategory.SMALL,430,560,200,300,0.0,0,"Trans",123456789));
				transporters.add(new Transporter(2,SizeCategory.MEDIUM,30,800,400,600,0.1,0.1,"Expert",123456789));
				transporters.add(new Transporter(3,SizeCategory.MEDIUM,100,670,330,900,0.3,0.01,"MarianEx",123456789));
				transporters.add(new Transporter(4,SizeCategory.BIG,1000,1000,1000,2000,0.01,0.03,"BudMarEx",123456789));
				transporters.add(new Transporter(5,SizeCategory.BIG,213,344,808,3000,0.12,0.022,"Poltrans",123456789));
				transporters.add(new Transporter(6,SizeCategory.BIG,20,607,700,200,0.3,0.001,"Tuumby",123456789));
				transporters.add(new Transporter(7,SizeCategory.SMALL,200,450,180,200,0.02,0.008,"Damby",123456789));
				transporters.add(new Transporter(8,SizeCategory.SMALL,100,300,100,200,0.0,0.01,"Union",123456789));
				transporters.add(new Transporter(9,SizeCategory.SMALL,80,600,280,380,0.1,0,"Kotlex",123456789));
				transporters.add(new Transporter(10,SizeCategory.SMALL,830,570,500,500,0.05,0.04,"Hindi",123456789));
				transporters.add(new Transporter(11,SizeCategory.MEDIUM,200,900,400,700,0,0,"CyberTrans",123456789));
				transporters.add(new Transporter(12,SizeCategory.MEDIUM,300,770,470,900,0.2,0.0,"Tybol",123456789));
				transporters.add(new Transporter(13,SizeCategory.MEDIUM,400,700,500,720,0.1,0,"LoliPor",123456789));
				transporters.add(new Transporter(14,SizeCategory.MEDIUM,500,740,630,770,0.07,0.015,"Tyrenda",123456789));
				transporters.add(new Transporter(15,SizeCategory.MEDIUM,670,780,700,840,0.06,0,"Qternik",123456789));
				transporters.add(new Transporter(16,SizeCategory.MEDIUM,800,880,680,940,0.02,0,"Omater",123456789));
				transporters.add(new Transporter(17,SizeCategory.BIG,1200,1100,1100,2000,0.01,0.0004,"Nindy",123456789));
				transporters.add(new Transporter(18,SizeCategory.BIG,2013,1700,1500,3000,0.12,0.09,"Irduna",123456789));
				transporters.add(new Transporter(19,SizeCategory.BIG,1400,1300,1200,4000,0.07,0.6,"Kolcol",123456789));
				//System.out.println("Przed: "+transporters);
		///
	}
	
	private void sortTransporters()
	{
		transporters = transporters.stream().sorted(Transporter::compareByCapacity).collect(Collectors.toCollection(ArrayList::new));
		//System.out.println("Po: "+transporters);
	}
	
	private void filterTransporters(SizeCategory sc)
	{
		transporters_filtered = transporters.stream().filter( t -> (t.getSizeCategory() == sc || sc == SizeCategory.ALL)).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<Transporter> getFilteredTransporters(String city_from, String city_to, SizeCategory sc)
	{
		if(!city_from.equals(cityFrom) || !city_to.equals(cityTo))
		{
			cityFrom = city_from;
			cityTo = city_to;
			getTranspotersFormDatabase(cityFrom, cityTo);
			sortTransporters();
		}
		
		if(size_category!=sc)
		{
			filterTransporters(sc);
		}
		return transporters_filtered;
	}

}
