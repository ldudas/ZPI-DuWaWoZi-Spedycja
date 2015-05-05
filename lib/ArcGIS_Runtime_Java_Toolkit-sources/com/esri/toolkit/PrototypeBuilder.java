/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit;
import java.util.ArrayList;
import java.util.List;

import com.esri.core.ags.LayerServiceInfo;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.FeatureTable;
import com.esri.map.FeatureLayer;
import com.esri.map.Layer.LayerStatus;


/**
 * This class is used to create a list of {@link PrototypeFeature}s from a <code>FeatureLayer</code>.
 * @since 10.2.3
 */
public class PrototypeBuilder {

  FeatureLayer _featureLayer;
  boolean _initialised = false;

  /**
   * Creates an instance from a feature layer.
   * 
   * @param featureLayer
   * @throws IllegalArgumentException if the feature layer is not yet initialized
   */
  public PrototypeBuilder(FeatureLayer featureLayer) {
    if (featureLayer.getStatus() != LayerStatus.INITIALIZED) {
      throw new IllegalArgumentException("Feature layer must be initialized");
    }
    _featureLayer = featureLayer;
  }

  /**
   * Creates and returns a list of prototype features for the feature layer associated 
   * with this prototype builder.
   * 
   * @return the list of prototype features.
   * @throws RuntimeException an exception will be thrown unless the feature layer is 
   * from a GeodatabaseFeatureTable
   */
  public List<PrototypeFeature> build() {
    ArrayList<PrototypeFeature> prototypeFeatures = new ArrayList<PrototypeFeature>();
    Renderer r = _featureLayer.getRenderer();
    FeatureTable featureTable = _featureLayer.getFeatureTable();
    if (!(featureTable instanceof GeodatabaseFeatureTable)) {
      throw new RuntimeException("Only GeodatabaseFeatureTable based feature layers are supported.");
    }
    GeodatabaseFeatureTable geodatabaseFeatureTable = (GeodatabaseFeatureTable)featureTable;
    LayerServiceInfo layerServiceInfo = geodatabaseFeatureTable.getLayerServiceInfo();
    for (FeatureTemplate ft : layerServiceInfo.getTemplates()) {
      Graphic g = new Graphic(null, null, ft.getPrototype());
      Symbol symbol = r.getSymbol(g);

      if (symbol != null) {
        prototypeFeatures.add(new PrototypeFeature(ft.getPrototype(), symbol, _featureLayer, ft.getDrawingTool(), ft.getName(), ft.getDescription()));
      }
    }
    for (FeatureType f : layerServiceInfo.getSubTypes()) {
      for (FeatureTemplate t : f.getTemplates()) {
        Feature feature = new Graphic(null, null, t.getPrototype());
        Symbol symbol = r.getSymbol(feature);

        if (symbol != null) {
          prototypeFeatures.add(new PrototypeFeature(t.getPrototype(), symbol, _featureLayer, t.getDrawingTool(), t.getName(), t.getDescription()));
        }
      }
    }

    return prototypeFeatures;
  }
}
