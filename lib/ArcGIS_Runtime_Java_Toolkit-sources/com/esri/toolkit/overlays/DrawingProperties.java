/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.util.Map;

import com.esri.toolkit.overlays.DrawingOverlay.DrawingMode;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.Symbol;

/**
 * Class used to store properties associated with drawing features, 
 * including the drawing mode, renderer, and attributes. For example, 
 * this can be used by a drawing tool to create new features 
 * according to what is specified in these properties.
 * 
 * @since 10.2.3
 */
public class DrawingProperties {
    private DrawingMode drawingMode;
    private Renderer renderer;
    private Map<String, Object> attributes;

    public DrawingProperties(
        DrawingMode drawingMode,
        Symbol symbol,
        Map<String, Object> attributes) {
        this.drawingMode = drawingMode;
        this.renderer = new SimpleRenderer(symbol);
        this.attributes = attributes;
    }

    public DrawingProperties(
        DrawingMode drawingMode,
        Renderer renderer,
        Map<String, Object> attributes) {
        this.drawingMode = drawingMode;
        this.renderer = renderer;
        this.attributes = attributes;
    }

    public DrawingMode getDrawingMode() {
        return drawingMode;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
