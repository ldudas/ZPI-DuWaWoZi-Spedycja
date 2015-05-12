/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import com.esri.client.toolkit.utilities.ComponentDragger;

/**
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.utilities.ResultPanel} instead.
 */
@Deprecated
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

    // make the result panel draggable using out component dragger
    ComponentDragger componentDragger = new ComponentDragger(this);
    addMouseMotionListener(componentDragger);
    addMouseListener(componentDragger);
    addMouseListener(new MouseAdapter() {

      @Override
      public void mouseEntered(MouseEvent e) {
        setCursor(MOVE_CURSOR);
      }
      @Override
      public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
      }

      @Override
      public void mouseClicked(MouseEvent e) {
      }
    });
    
    applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
  }

  private void loadCloseIcon() {
    closeIcon = new ImageIcon(getClass().getResource("/com/esri/client/toolkit/images/close.png"));
  }
}
