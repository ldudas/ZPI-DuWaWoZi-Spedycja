/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.bookmarks;

import java.util.List;

import com.esri.core.geometry.Envelope;
import com.esri.core.map.Bookmark;
import com.esri.map.JMap;

/**
 * A JPanel to bookmark/save-as-favorite the current map extent.
 * <p>
 * Persistence: The extents are persisted by default to the file system so that they are available 
 * between application restarts. All actions immediately persist the extents. Extents are not
 * persisted if no persistence file is specified.
 * <p>
 * This JPanel:
 * <ul> 
 * <li> has a customizable title
 * <li> has a button to add an extent
 * <li> has a button to remove a single selected extent
 * <li> has a button to remove all extents
 * <li> has a UI to list extents
 * <li> is draggable
 * </ul>
 * @usage
 * <code>
 * <pre>
 * JExtentBookmark extentBookmarks = new JExtentBookmark(map, "c://mybookmarks.bookmarks");
 * contentPane.add(extentBookmarks);
 * </pre>
 * </code>
 * @since 10.2.3
 */
public class JExtentBookmark extends JBookmark<Envelope, String> {

  private static final long serialVersionUID = 1L;

  /**
   * Creates an extent bookmark component that is visible by default.
   * @param jMap jMap whose current extent will be bookmarked whenever the user action
   * is to add a bookmark
   * @param persistenceFileName file where the bookmarked extents will be persisted. 
   * If null, the extent bookmarks will NOT be persisted.
   */
  public JExtentBookmark(JMap jMap, String persistenceFileName) {
    super(new ExtentBookmarkCallback(jMap), persistenceFileName);
    initializeThis();
  }
  
  /**
   * Creates an extent bookmark component that is visible by default. The extent bookmarks
   * will NOT be persisted.
   * @param jMap jMap whose current extent will be bookmarked whenever the user action
   * is to add a bookmark
   */
  public JExtentBookmark(JMap jMap) {
    this(jMap, null);
  }
  
  /**
   * Adds a list of bookmarks to the UI.
   * @param bookmarks list of {@link Bookmark} to be added.
   */
  public void addBookmarks(List<Bookmark> bookmarks) {
    if (bookmarks == null || bookmarks.isEmpty()) {
      return;
    }
    for (Bookmark bookmark : bookmarks) {
      addBookmark(bookmark.getName(), bookmark.getExtent());
    }
  }
  
  private void initializeThis() {
    setTitleText("Extent Bookmarks");
    setLabelPrefix("Extent ");
  } 
}

/**
 * Extent Bookmark callback to handle: 
 * <ol>
 * <li> the addition and selection of bookmarked extents, and
 * <li> the serialization/de-serialization of bookmarked extents.
 * </ol>
 */
class ExtentBookmarkCallback implements IBookmarkCallback<Envelope, String> {
  // JMap whose current extent will be added whenever a bookmark is to be added
  JMap jMap;
  private static final String VALUE_SEP = ",";
  
  /**
   * Callback.
   * @param jMap JMap whose current extent will be added whenever a bookmark is to be added.
   */
  public ExtentBookmarkCallback(JMap jMap) {
    this.jMap = jMap;
  }
  
  /**
   * Returns the current extent that is to be added as a bookmark.
   */
  @Override
  public Envelope getBookmarkToAdd() {
    return jMap == null ? null : jMap.getExtent();
  }

  /**
   * The selected extent will be set as the current extent of the JMap.
   */
  @Override
  public void onSelectBookmark(Envelope selectedExtent) {
    if (jMap != null) {
      jMap.setExtent(selectedExtent);
    }
  }
  
  /**
   * Serializes the input extent.
   */
  @Override
  public String getSerializedBookmark(Envelope deserializedExtent) {
    StringBuilder extentString = new StringBuilder();
    extentString.append(deserializedExtent.getXMin());
    extentString.append(VALUE_SEP + deserializedExtent.getYMin());
    extentString.append(VALUE_SEP + deserializedExtent.getXMax());
    extentString.append(VALUE_SEP + deserializedExtent.getYMax());    
    return extentString.toString();   
  }
  
  /**
   * De-serializes the input extent.
   */
  @Override
  public Envelope getDeserializedBookmark(String serializedExtent) {
    String extentValueString = (String) serializedExtent;
    String[] parsedValues = extentValueString.split(VALUE_SEP);
    Envelope extent = new Envelope(
      Double.parseDouble(parsedValues[0]), 
      Double.parseDouble(parsedValues[1]),
      Double.parseDouble(parsedValues[2]),
      Double.parseDouble(parsedValues[3])); 
    return extent;
  }

}
