/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit;
import java.util.ArrayList;
import java.util.List;

import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.Symbol;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.FeatureLayer;
import com.esri.map.LayerInitializeCompleteEvent;
import com.esri.map.LayerInitializeCompleteListener;
import com.esri.map.Layer.LayerStatus;


/**
 * This class is used to create a list of prototype features from an <code>ArcGISFeatureLayer</code>.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.PrototypeBuilder} instead.
 * The new package works with the {@link FeatureLayer} introduced in 10.2.
 */
@Deprecated
public class PrototypeBuilder {
  
  ArcGISFeatureLayer _featureLayer;
  boolean _initialised = false;
  
  /**
   * Creates an instance from a URL to a feature layer.
   * 
   * @param url
   */
  public PrototypeBuilder(String url) {
    _featureLayer = new ArcGISFeatureLayer(url);
    _featureLayer.addLayerInitializeCompleteListener(new LayerInitializeCompleteListener() {
      
      @Override
      public void layerInitializeComplete(LayerInitializeCompleteEvent e) {
        _initialised = true;
      }
    });
    _featureLayer.initializeAsync();
    
    while (!_initialised) {
      // wait for service to be initialized
    }
  }
  
  /**
   * Creates an instance from a feature layer.
   * 
   * @param featureLayer
   * @throws IllegalArgumentException if the feature layer is not yet initialized
   * 
   */
  public PrototypeBuilder(ArcGISFeatureLayer featureLayer) {
    if (featureLayer.getStatus() != LayerStatus.INITIALIZED) {
      throw new IllegalArgumentException("Feature layer must be initialized");
    }
    _featureLayer = featureLayer;
  }
  
  /**
   * Gets a list of graphics each of which have the symbol and attributes of one of the prototypes.
   * 
   * @return the list 
   */
  public List<PrototypeGraphic> build() {
    ArrayList<PrototypeGraphic> prototypeGraphics = new ArrayList<PrototypeGraphic>();
    Renderer r = _featureLayer.getRenderer();
    for (FeatureTemplate ft : _featureLayer.getTemplates()) {
      Graphic g = new Graphic(null, null, ft.getPrototype());
      Symbol symbol = r.getSymbol(g);
      
      prototypeGraphics.add(new PrototypeGraphic(ft.getPrototype(), symbol, _featureLayer, ft.getDrawingTool(), ft.getName(), ft.getDescription()));
    }
    for (FeatureType f : _featureLayer.getSubTypes()) {
      for (FeatureTemplate t : f.getTemplates()) {
        Graphic g = new Graphic(null, null, t.getPrototype());
        Symbol symbol = r.getSymbol(g);
        
        prototypeGraphics.add(new PrototypeGraphic(t.getPrototype(), symbol, _featureLayer, t.getDrawingTool(), t.getName(), t.getDescription()));
      }
    }
    
    return prototypeGraphics;
  }
}
