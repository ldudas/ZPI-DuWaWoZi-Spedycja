/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.utilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;


/**
 * This component can be used to display results, or any textual content which has 
 * a title and (String) content. It is not visible by default when created, but can be 
 * shown using <code>setVisible(true)</code>, for example when some search task has just
 * completed, to display results to the user. The panel's content is set using 
 * <code>setContent</code> and displayed in a scrollable text area. The panel can 
 * be moved on screen by pressing on the edges of the component, dragging it 
 * to a new location, and releasing the mouse.
 * 
 * @usage
 * <code>
 * ResultPanel resultPanel = new ResultPanel();<br>
 * // can set a location, for example if layering on top of the map in a JLayeredPane<br>
 * resultPanel.setLocation(10, 10);<br>
 * resultPanel.setSize(250, 200);<br>
 * // ... <br>
 * resultPanel.setTitle(titleText);<br>
 * resultPanel.setContent(resultText);<br>
 * resultPanel.setVisible(true);<br>
 * </code>
 *
 * @since 10.2.3
 */
public class ResultPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  protected static final Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);

  private JTextArea resultText;
  private JTextField titleText;
  private JScrollPane scrollPane;
  private int defaultWidth = 250; //default
  private int defaultHeight = 120; //default
  private ImageIcon closeIcon;

  public ResultPanel() {
    super();
    loadCloseIcon();
    createResultPanel();
  }

  public void setTitle(String title) {
    titleText.setText(title);
  }

  public void setContent(String content) {
    resultText.setText(content);
    // set scroll bar to top
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        scrollPane.getVerticalScrollBar().setValue(0);
      }
    });
  }

  // override to deal with transparency issues when background color has alpha value
  @Override
  protected void paintComponent(Graphics g) {
    g.setColor( getBackground() );
    g.fillRect(0, 0, getWidth(), getHeight());
    super.paintComponent(g);
  }

  private void createResultPanel() {

    // title for panel
    titleText = new JTextField();
    titleText.setHorizontalAlignment(SwingConstants.LEFT);
    titleText.setFont(new Font(titleText.getFont().getName(), Font.BOLD, 12));
    titleText.setBackground(Color.BLACK);
    titleText.setForeground(Color.WHITE);
    titleText.setBorder(BorderFactory.createEmptyBorder(2,10,2,10));

    JPanel topPanel = new JPanel();
    topPanel.setBackground(Color.BLACK);
    topPanel.setLayout(new BorderLayout(0, 0));

    // create close/hide panel button
    JButton close = new JButton(closeIcon);
    close.setBackground(Color.BLACK);
    close.setSize(closeIcon.getIconWidth(), closeIcon.getIconHeight());
    close.setBorder(null);
    close.setToolTipText("Close");
    close.setRolloverIcon(new ImageIcon(getClass().getResource("/com/esri/client/toolkit/images/close_select.png")));
    close.setRolloverEnabled(true);
    close.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setVisible(false);
      }
    });

    topPanel.add(titleText, BorderLayout.CENTER);
    topPanel.add(close, BorderLayout.EAST);

    resultText = new JTextArea();
    resultText.setForeground(Color.BLACK);
    resultText.setBackground(Color.WHITE);
    resultText.setMaximumSize(new Dimension(defaultWidth, defaultHeight-closeIcon.getIconHeight()));
    resultText.setEditable(false);
    resultText.setLineWrap(true);
    resultText.setWrapStyleWord(true);
    resultText.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
    
    // group the above UI items into a panel
    setLayout(new BorderLayout());
    setLocation(10, 10); // set a default location
    setSize(defaultWidth, defaultHeight);

    scrollPane = new JScrollPane(resultText);
    add(topPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    setOpaque(false);
    setBackground(new Color(0, 0, 0, 0));
    setBorder(new LineBorder(new Color(0, 0, 0, 80), 5, false));
    setVisible(false);

    // make the result panel draggable using the toolkit's component dragger
    ComponentDragger componentDragger = new ComponentDragger(this);
    addMouseMotionListener(componentDragger);
    addMouseListener(componentDragger);
    
    applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
  }

  private void loadCloseIcon() {
    closeIcon = new ImageIcon(getClass().getResource("/com/esri/client/toolkit/images/close.png"));
  }
}
