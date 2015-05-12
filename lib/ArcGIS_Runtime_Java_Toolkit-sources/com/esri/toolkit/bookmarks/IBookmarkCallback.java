/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.bookmarks;

/**
 * Interface for a bookmark callback to handle:
 * <ol>
 * <li> the addition and selection of bookmark, and
 * <li> the serialization/de-serialization of bookmarks
 * </ol>
 *
 * @param <B> bookmark type
 * @param <SB> serialized bookmark type
 * @since 10.2.3
 */
public interface IBookmarkCallback<B, SB> {
  
  /**
   * This method is invoked whenever a new bookmark is to be added to the set of bookmarks.
   * @return the bookmark to be added.
   */
  public B getBookmarkToAdd();
  
  /**
   * This method is invoked whenever a bookmark is selected.
   * @param selectedBookmark the selected bookmark.
   */
  public void onSelectBookmark(B selectedBookmark);
  
  /**
   * This method is invoked whenever the bookmark is to be persisted to the file system.
   * This method is ignored if persistence is not required, which is based on how
   * {@link JBookmark} is constructed.
   * @param bookmark the bookmark to be serialized.
   * @return the corresponding serialized bookmark.
   */
  public SB getSerializedBookmark(B bookmark);
  
  
  /**
   * This method is invoked whenever a serialized bookmark is read from the file system.
   * This method is ignored if persistence is not required, which is based on how
   * {@link JBookmark} is constructed.
   * @param serializedBookmark the serialized bookmark from the file system.
   * @return the corresponding de-serialized bookmark.
   */
  public B getDeserializedBookmark(SB serializedBookmark);
}
