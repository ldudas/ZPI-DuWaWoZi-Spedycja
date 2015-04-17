package visualisations;

public class VisualisationPathPresenter {
	
	private VisualisationPathModel model_PathVis;
	private VisualisationPathView view_PathVis;
	
	public VisualisationPathPresenter(final VisualisationPathView view, VisualisationPathModel model)
	{
		view_PathVis = view;
		model_PathVis = model;
	}
	
	
	public void showPathMap()
	{
		view_PathVis.openWindowWithMap(model_PathVis.getPathMap());
	}
	
	public void createInitialMap(final String cityNameFrom, final String cityNameTo)
	{
		model_PathVis.setInitialMap(cityNameFrom, cityNameTo);
	}

}
