/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.overlays;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import com.esri.map.JMap;
import com.esri.map.MapOverlay;

import com.esri.core.geometry.Envelope;

/**
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.overlays.ZoomBoxOverlay} instead.
 */
@Deprecated
public class ZoomBoxOverlay extends MapOverlay {

    private static final long serialVersionUID = 1L;
    private static final Color TRANS_BG = new Color(0, 0, 0, 30);
    private Point _firstPoint; // java.awt.Point, in screen coordinates
    private Rectangle2D _zoomRect;

    public ZoomBoxOverlay(){
    }

    @Override
    public void onMousePressed(MouseEvent event) {
        // Get first point in screen coords.
        _firstPoint = event.getPoint();
        _zoomRect = new Rectangle2D.Double();
    }

    @Override
    public void onMouseReleased(MouseEvent event) {

        JMap map = this.getMap();
        // Convert first point to envelope top left in map coords.
        com.esri.core.geometry.Point topLeft = map.toMapPoint(
                (int)_zoomRect.getMinX(), (int)_zoomRect.getMinY());

        // Convert second point to envelope bottom right in map coords.
        com.esri.core.geometry.Point bottomRight = map.toMapPoint(
                (int)_zoomRect.getMaxX(), (int)_zoomRect.getMaxY());

        // create and set the extent
        Envelope extent = new Envelope(topLeft.getX(), topLeft.getY(),
            bottomRight.getX(), bottomRight.getY());
        map.setExtent(extent);

        _firstPoint = null;
        _zoomRect = null;
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        if(_firstPoint != null){
            double width = Math.abs(event.getX() - _firstPoint.getX());
            double height = Math.abs(event.getY() - _firstPoint.getY());
            Point topLeft = new Point(); // java.awt.Point
            if(_firstPoint.getX() < event.getX()){
                topLeft.setLocation(_firstPoint.getX(), 0);
            }
            else{
                topLeft.setLocation(event.getX(), 0);
            }

            if(_firstPoint.getY() < event.getY()){
                topLeft.setLocation(topLeft.getX(), _firstPoint.getY());
            }
            else{
                topLeft.setLocation(topLeft.getX(), event.getY());
            }
            _zoomRect.setRect(topLeft.getX(), topLeft.getY(), width, height);
            this.repaint();
        }
    }

    @Override
    public void paint(Graphics graphics) {
        if(_zoomRect != null){
            Graphics2D g = (Graphics2D) graphics;
            g.setColor(Color.DARK_GRAY);
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_MITER, 2, new float[]{6, 6}, 0f));
            g.draw(_zoomRect);
            g.setPaint(TRANS_BG);
            g.fill(_zoomRect);
        }
    }
}
