/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;
import com.esri.map.ArcGISDynamicMapServiceLayer;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.ArcGISPopupInfo;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.FeatureLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.GroupLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.LayerEvent;
import com.esri.map.LayerInfo;
import com.esri.map.LayerList;
import com.esri.map.LayerListEventListenerAdapter;
import com.esri.map.MapOverlay;
import com.esri.map.popup.PopupDialog;
import com.esri.map.popup.PopupView;
import com.esri.toolkit.utilities.ExceptionHandler;

/**
* This class implements a map overlay that will display a popup dialog 
* (infopopup) for any features clicked on. Layers of interest are added to 
* the overlay with the {@link #addLayer} method.
* <p>
* When the user clicks a graphic or feature, the class will loop through all the 
* layers it has a reference to and does a hit test on each one. Any hit graphics 
* will have their corresponding infopopup instance display. If more than one 
* graphic was hit, the infopopups will be stacked, and the user can switch from 
* one to the next using the arrow icons.
* <p>
* The title of the popup dialog can be set for all popups in an app, using 
* the setPopupTitle method. The title underneath the dialog title is the item 
* title, and this can be set using setItemTitle, which support token-replacement 
* with field values. A field name in braces (curly brackets) will be replaced 
* with its value, e.g. <code>field_name: {field_name}</code>.
* @usage
* <code>
* <pre>
* InfoPopupOverlay infoPopupOverlay = new InfoPopupOverlay();
* infoPopupOverlay.setPopupTitle("Feature");
* infoPopupOverlay.setItemTitle("Selected {BLOCK}"); // BLOCK is a attribute of a feature in the layer 
* infoPopupOverlay.addLayer((Layer) layer);
* ((JMap) jMap).addMapOverlay(infoPopupOverlay);
* </pre>
* </code>
* @since 10.2.3
*/
public class InfoPopupOverlay extends MapOverlay {
  private static final long serialVersionUID = 1L;
  private List<Layer> _layers = Collections.synchronizedList(new ArrayList<Layer>());
  private String _itemTitle;
  private Dimension _initialPopupSize = null;
  private String _popupTitle;
  private boolean _autoAddLayers;
  private Color _borderColour;
  private int _popupBorderWidth = -1;

  // ------------------------------------------------------------------------
  // Constructors
  // ------------------------------------------------------------------------
  /**
   * Constructs an info popup overlay instance. 
   */
  public InfoPopupOverlay() {
  }

  /**
   * Constructs an info popup overlay instance where any layers added to the map
   * automatically get added the the overlay. Particularly useful if you want popups 
   * to display for a webmap where you may not know the layers in advance.
   * 
   * @param autoAddLayers set to true to add to the overlay any layers added to the map
   */
  public InfoPopupOverlay(boolean autoAddLayers) {
    _autoAddLayers = autoAddLayers;
  }
  
  // ------------------------------------------------------------------------
  // Properties
  // ------------------------------------------------------------------------
  /**
   * Gets the initial size of the info popups.
   * 
   * @return the info popup's initial size
   */
  public Dimension getInitialPopupSize(){
    return _initialPopupSize;
  }
  
  /**
   * Sets the initial size of the info popups. If this is not set, the popup
   * will be sized to fit its contents.
   * 
   * @param initialSize the initial size of the popup
   */
  public void setInitialPopupSize(Dimension initialSize){
    _initialPopupSize = initialSize;
  }
  
  /**
   * Gets the current title set for the popup dialog.
   * 
   * @return the popup title
   */
  public String getPopupTitle() {
    return _popupTitle;
  }

  /**
   * Sets the title to display in the title bar of the popup dialog.
   * This title will be the same for all popups displayed in the app.
   * 
   * @param popupTitle the popup title to set
   */
  public void setPopupTitle(String popupTitle) {
    _popupTitle = popupTitle;
  }

  /**
   * Gets the title that will be displayed above the attributes list
   * for a given feature.
   * 
   * @return the item title
   */
  public String getItemTitle(){
    return _itemTitle;
  }
  
  /**
   * Sets the title that will be displayed above the attributes list for a 
   * given feature. This can contain tokens that will be replaced with field 
   * values. A field name in braces (curly brackets) will be replaced with its 
   * value for the given feature, e.g.: <code>field_name: {field_name}</code>
   * 
   * @param title the item title
   */
  public void setItemTitle(String title){
    _itemTitle = title;
  }

  public Color getPopupBorderColor() {
    return _borderColour;
  }

  public void setPopupBorderColor(Color borderColor) {
    _borderColour = borderColor;
  }

  public int getPopupBorderWidth() {
    return _popupBorderWidth;
  }

  /**
   * Sets the border width. This should be between zero and fifteen inclusive. 
   * Any values outside of this range will be ignored and the border width will 
   * remain unchanged.
   * 
   * @param popupBorderWidth the new border width
   */
  public void setPopupBorderWidth(int popupBorderWidth) {
    _popupBorderWidth = popupBorderWidth;
  }

  // ------------------------------------------------------------------------
  // Public methods
  // ------------------------------------------------------------------------
  /**
   * Adds a layer that we want to display popups for.
   *
   * @param layer
   *            the layer
   */
  public void addLayer(Layer layer) {
    _layers.add(layer);
  }

  /**
   * Removes the given layer from the overlay. Popups will no longer be 
   * displayed for features in this layer.
   *
   * @param layer
   *            the layer
   */
  public void removeLayer(Layer layer){
    _layers.remove(layer);
  }

  /**
   * Removes all layers used by this overlay.
   */
  public void removeAllLayers() {
    _layers.clear();
  }

  /**
   * This override is used to perform a hit test on all the graphics or
   * feature layers this class has a reference to. All graphics found at
   * the clicked location will be displayed in the popup.
   *
   * @param event
   *            the event
   * @see com.esri.map.MapOverlay#onMouseClicked(java.awt.event.MouseEvent)
   */
  @Override
  public void onMouseClicked(final MouseEvent event) {
    super.onMouseClicked(event);
    final Point mouseMapPoint = getMap().toMapPoint(event.getX(), event.getY());

    if (event.getButton() == MouseEvent.BUTTON1) {

      SwingUtilities.invokeLater(new Runnable() {
        
        @Override
        public void run() {
          HashMap<Layer, ArrayList<Feature>> hitResultMap = new HashMap<Layer, ArrayList<Feature>>();
          // hit test the layers starting with the topmost
          for (Layer l: getAllLayers(getMap().getLayers())) {
            ArrayList<Feature> hitTestResult = new ArrayList<Feature>();
            if (_layers.contains(l)) {
              if(l instanceof GraphicsLayer){
                GraphicsLayer curGraphicsLayer = (GraphicsLayer)l;
                int[] hitIDs = curGraphicsLayer.getGraphicIDs(event.getX(), event.getY(), 0);
                for (int i : hitIDs) {
                  hitTestResult.add(curGraphicsLayer.getGraphic(i));
                }
    
                if(!hitTestResult.isEmpty()){
                  hitResultMap.put(curGraphicsLayer, hitTestResult);
                }
              }else if(l instanceof FeatureLayer){
                FeatureLayer curFeatureLayer = (FeatureLayer) l;
                FeatureTable table = curFeatureLayer.getFeatureTable();
                long[] hitIDs = curFeatureLayer.getFeatureIDs(event.getX(), event.getY(), 12);
                for(long id: hitIDs){
                  try {
                    hitTestResult.add(table.getFeature(id));
                  } catch (TableException e) {
                    ExceptionHandler.handleException(InfoPopupOverlay.this, e);
                  }
                }
    
                if(!hitTestResult.isEmpty()){
                  hitResultMap.put(curFeatureLayer, hitTestResult);
                }
              }else{
                // Handle map service layer
                ArrayList<LayerInfo> layerInfos = new ArrayList<LayerInfo>();
                
                // Get list of sublayers
                if(l instanceof ArcGISDynamicMapServiceLayer){
                  layerInfos.addAll(((ArcGISDynamicMapServiceLayer)l).getLayersList());
                }else if(l instanceof ArcGISTiledMapServiceLayer){
                  layerInfos.addAll(((ArcGISTiledMapServiceLayer)l).getLayersList());
                }
                int layerCount = layerInfos.size();
                
                // Perform a query on each sublayer
                for(int count = 0; count < layerCount; ++count){
                  if(l.getPopupInfo(count) != null){
                    String queryUrl = l.getQueryLayerUrl(count);
                    
                    if(queryUrl != null){
                      QueryParameters query = new QueryParameters();
                      
                      if(layerInfos.get(count).getGeometryType() != Type.POLYGON){
                        // Create an envelope around the clicked point and to an intersects
                        // spatial query
                        
                        double contractRatio = getMap().getExtent().getWidth() / 20;
                        Envelope inputEnvelope = new Envelope(mouseMapPoint.getX() - contractRatio,
                            mouseMapPoint.getY() - contractRatio,
                            mouseMapPoint.getX() + contractRatio,
                            mouseMapPoint.getY() + contractRatio);
                        query.setGeometry(inputEnvelope);
                        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
                      }else{
                        // Have a polygon layer, we can do a spatial query for features containing
                        // the clicked point.
                        query.setGeometry(mouseMapPoint);
                        query.setSpatialRelationship(SpatialRelationship.WITHIN);
                      }
                      
                      SpatialReference spatialReference = getMap().getSpatialReference();
                      query.setInSpatialReference(spatialReference);
                      query.setOutSpatialReference(spatialReference);
                      query.setOutFields(new String[]{"*"});
                      QueryTask queryTask = new QueryTask(queryUrl);
                      FeatureResult features;
                      try {
                        features = queryTask.execute(query);
                        Iterator<Object> it = features.iterator();
                        while (it.hasNext()) {
                          Object object = it.next();
                          if (object instanceof Feature) {
                            hitTestResult.add((Feature) object);
                          }
                        }
                      } catch (Exception e) {
                      }
                    }
                    hitResultMap.put(l, hitTestResult);
                  }
                }
              }
            }
          }
    
          if (!hitResultMap.isEmpty()) {
            displayPopup(hitResultMap, event.getLocationOnScreen());
          }          
        }
      });
    }
  }

  protected List<Layer> getAllLayers(List<Layer> layers) {
    List<Layer> retVal = new ArrayList<Layer>();
    
    for(Layer curLayer: layers) {
      if(curLayer instanceof GroupLayer) {
        retVal.addAll(getAllLayers(Arrays.asList(((GroupLayer) curLayer).getLayers())));
      } else {
        retVal.add(curLayer);
      }
    }
    return retVal;
  }

  // ------------------------------------------------------------------------
  // Non-Public methods
  // ------------------------------------------------------------------------
  @Override
  protected void setMap(JMap map) {
    super.setMap(map);
    
    if(_autoAddLayers){
      autoAddLayers(map);
    }
  }

  private void autoAddLayers(JMap map) {
    LayerList layers = map.getLayers();
    
    for(Layer curLayer: layers){
      addLayer(curLayer);
    }
    
    layers.addLayerListEventListener(new LayerListEventListenerAdapter() {
      
      @Override
      public void multipleLayersRemoved(LayerEvent event) {
        _layers.removeAll(getAllLayers(new ArrayList<Layer>(event.getChangedLayers().values())));
      }
      
      @Override
      public void multipleLayersAdded(LayerEvent event) {
        _layers.addAll(getAllLayers(new ArrayList<Layer>(event.getChangedLayers().values())));
      }
      
      @Override
      public void layerRemoved(LayerEvent event) {
        Layer changedLayer = event.getChangedLayer();
        if(changedLayer instanceof GroupLayer) {
          _layers.removeAll((getAllLayers(Arrays.asList(((GroupLayer) changedLayer).getLayers()))));
        } else {
          removeLayer(changedLayer);
        }
      }
      
      @Override
      public void layerAdded(LayerEvent event) {
        Layer changedLayer = event.getChangedLayer();
        
        if(changedLayer instanceof GroupLayer) {
          _layers.addAll(getAllLayers(Arrays.asList(((GroupLayer) changedLayer).getLayers())));
        } else {
          addLayer(changedLayer);
        }
      }
    });
  }

  /**
   * Displays popups for the given features with popup tails pointing at the
   * given location.
   * @param hitResultMap 
   *            Map of layers to features. Each entry contains a layer and the
   *            features in that are to be displayed in the popup.
   * @param popupLocation
   *            Location that popup tails will originally point at
   */
  private void displayPopup(HashMap<Layer, ArrayList<Feature>> hitResultMap, java.awt.Point popupLocation) {
    ArrayList<JComponent> popupPanels = new ArrayList<JComponent>();
    Feature graphicToPointTo = null;

    for (Entry<Layer, ArrayList<Feature>> curEntry : hitResultMap.entrySet()) {
      PopupView contentPanel = null;
      Layer popupLayer = curEntry.getKey();
      ArcGISPopupInfo popupInfo = popupLayer.getPopupInfo(0);
      for (Feature curGraphic : curEntry.getValue()) {
        graphicToPointTo = curGraphic;
        if (popupInfo != null) {
          contentPanel = new PopupView(false);
          contentPanel.setPopupInfo(popupInfo);
        } else if (popupLayer instanceof ArcGISFeatureLayer) {
          contentPanel = PopupView.createAttributesView(_itemTitle, ((ArcGISFeatureLayer) popupLayer).getFields());
        } else if (popupLayer instanceof GraphicsLayer) {
          contentPanel = PopupView.createAttributesView(_itemTitle, curGraphic);
        } else if (popupLayer instanceof FeatureLayer){
          contentPanel = PopupView.createAttributesView(_itemTitle, ((FeatureLayer)popupLayer).getFeatureTable().getFields().toArray(new Field[]{}));
        }

        if (contentPanel != null) {
          if (popupLayer instanceof GraphicsLayer) {
            GraphicsLayer featureLayer = (GraphicsLayer) popupLayer;
            contentPanel.setGraphic(featureLayer, (Graphic)curGraphic);
          } else if(popupLayer instanceof FeatureLayer){
            FeatureLayer featureLayer = (FeatureLayer) popupLayer;
            contentPanel.setFeature(getMap(), (GeodatabaseFeatureTable) featureLayer.getFeatureTable(), curGraphic);
          }else {
            contentPanel.setFeature(getMap(), curGraphic);
          }
          if(_initialPopupSize != null){
            contentPanel.setPreferredSize(_initialPopupSize);
          }
          popupPanels.add(contentPanel);
        }
      }
    }

    // Create a popup to host the panel
    if (!popupPanels.isEmpty()) {
      PopupDialog popup = getMap().createPopup(popupPanels.toArray(new JComponent[] {}),
          graphicToPointTo);
      popup.setAnchorPoint(popupLocation);
      popup.setTitle(_popupTitle);
      if(_popupBorderWidth > -1){
        popup.setBorderWidth(_popupBorderWidth);
      }
      popup.setVisible(true);
      if(_borderColour != null){
        popup.setBorderColor(_borderColour);
      }
      getMap().repaint();
    }
  }

}
