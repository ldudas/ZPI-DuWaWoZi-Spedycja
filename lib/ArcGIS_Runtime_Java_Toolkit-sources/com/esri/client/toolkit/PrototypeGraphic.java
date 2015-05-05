/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit;
import java.awt.Color;
import java.util.Map;

import javax.swing.ImageIcon;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MultiLayerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.FeatureLayer;
import com.esri.core.map.FeatureTemplate.DRAWING_TOOL;


/**
 * This class represents a prototype of a feature (<code>Graphic</code> class) in an 
 * <code>ArcGISFeatureLayer</code>.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.PrototypeFeature} instead. The new package
 * works with {@link FeatureLayer} introduced in 10.2.
 */
@Deprecated
public class PrototypeGraphic {
  
  Graphic _graphic;
  String _name;
  String _description;
  Geometry.Type _geometryType;
  ArcGISFeatureLayer _featureLayer;
  DRAWING_TOOL _drawingTool;
  
  /**
   * Instantiates a PrototypeGraphic.
   * 
   * @param attributes
   *    The attributes for this prototype
   * @param symbol
   *    The symbol for this prototype
   * @param featureLayer
   *    The feature layer this prototype is for
   * @param name
   *    The name of the prototype
   * @param description
   *    The description of the prototype
   */
  public PrototypeGraphic(Map<String, Object> attributes, Symbol symbol, ArcGISFeatureLayer featureLayer, DRAWING_TOOL drawingTool, String name, String description) {
    if (featureLayer == null) {
      throw new IllegalArgumentException("feature layer cannot be null");
    }
    _name = name;
    _description = description;
    _graphic = new Graphic(null, symbol, attributes);
    _featureLayer = featureLayer;
    _drawingTool = drawingTool;
    //_drawingTool = DRAWING_TOOL.esriFeatureEditToolFreehand;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return _graphic.getAttributes() + "  " + _graphic.getSymbol();
  }
  
  /**
   * Gets the feature layer this prototype is for.
   * 
   * @return the feature layer that this prototype is for
   */
  public ArcGISFeatureLayer getFeatureLayer() {
    return _featureLayer;
  }
  
  /**
   * Gets the name.
   * 
   * @return the name of the prototype feature
   */
  public String getName() {
    return _name;
  }
  
  /**
   * Gets the description.
   * 
   * @return the description of the prototype feature
   */
  public String getDescription() {
    return _description;
  }
  
  /**
   * Gets the geometry type.
   * 
   * @return the geometry type of this prototype
   */
  public Geometry.Type getGeometryType() {
    return _featureLayer.getGeometryType();
  }
  
  /**
   * Gets the drawing tool.
   * 
   * @return a drawing tool that can be used to create features from this prototype
   */
  public DRAWING_TOOL getDrawingTool() {
    return _drawingTool;
  }
  
  /**
   * Gets a sample icon of the prototype.
   * 
   * @return an icon demonstrating how a feature with this prototype would be symbolized 
   */
  public ImageIcon getIcon(int width, int height) {
    Symbol symbol = getSymbol();
    Geometry geom = null;
    
    if (symbol instanceof SimpleMarkerSymbol || symbol instanceof PictureMarkerSymbol) {
      geom = new Point(width / 2, height / 2);
    } else if (symbol instanceof SimpleFillSymbol) {
      LineSymbol outline = ((SimpleFillSymbol)symbol).getOutline();
      float outlineThickness = 0;
      if (outline != null) {
        outlineThickness = outline.getWidth();
      }
      Polygon p = new Polygon();
      p.startPath(new Point(0.0 + outlineThickness, 0.0 + outlineThickness));
      p.lineTo(0.0 + outlineThickness, height - outlineThickness);
      p.lineTo(width - outlineThickness, height - outlineThickness);
      p.lineTo(width - outlineThickness, 0.0 + outlineThickness);
      p.closeAllPaths();

      geom = p;
    } else if (symbol instanceof SimpleLineSymbol) {
      Polyline p = new Polyline();
      p.startPath(new Point(0.0, height / 2.0));
      p.lineTo(width, height / 2.0);
      p.closeAllPaths();
      
      geom = p;
    } else if (symbol instanceof MultiLayerSymbol) {
      if (this.getGeometryType() == Geometry.Type.POINT) {
        geom = new Point(width / 2, height / 2);
      } else if (this.getGeometryType() == Geometry.Type.POLYLINE) {
        Polyline p = new Polyline();
        p.startPath(new Point(0.0, height / 2.0));
        p.lineTo(width, height / 2.0);
        p.closeAllPaths();
        
        geom = p;
      } else if (this.getGeometryType() == Geometry.Type.POLYGON) {
        Polygon p = new Polygon();
        p.startPath(new Point(0.0, 0.0));
        p.lineTo(0.0, height);
        p.lineTo(width, height);
        p.lineTo(width, 0.0);
        p.closeAllPaths();
        
        geom = p;
      }
    }
    
    return new ImageIcon(_featureLayer.createSymbolImage(symbol, geom, width, height, new Color(0x00000000, true)));
  }
  
  public Symbol getSymbol() {
    return _graphic.getSymbol();
  }
  
  public Map<String, Object> getAttributes() {
    return _graphic.getAttributes();
  }
}
