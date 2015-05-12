/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.bookmarks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.esri.client.toolkit.utilities.ComponentDragger;

/**
 * A JPanel with a generic bookmark/favorites functionality. This class can be extended
 * to create bookmarks of a certain type (e.g., bookmarks of extents, bookmarks of symbols, etc.).
 * <p>
 * Persistence: The bookmarks are persisted by default to the file system so that they are available
 * between application restarts. All actions immediately persist the bookmarks. Bookmarks are not
 * persisted if no persistence file is specified.
 * <p>
 * This JPanel:
 * <ul>
 * <li> has a customizable title
 * <li> has a button to add a bookmark
 * <li> has a button to remove a single selected bookmark
 * <li> has a button to remove all bookmarks
 * <li> has a UI to list bookmarks
 * <li> is draggable
 * </ul>
 *
 * @param <B> bookmark type.
 * @param <SB> serialized bookmark type. Currently, only String is supported.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.bookmarks.JBookmark} instead.
 */
@Deprecated
public class JBookmark<B, SB> extends JPanel {

    private static final long serialVersionUID = 1L;

    // callback to handle serialization/de-serialization of the bookmark. this logic is required
    // to persist the bookmark.
    IBookmarkCallback<B, SB> bookmarkCallback;

    // back-end table model to represent the bookmarks in-memory.
    // the columns of this table are {@link BookmarkColumns}
    private DefaultTableModel modelBookmarks = new DefaultTableModel();

    // constants for bookmark name auto-generation
    private String labelPrefix = "Bookmark ";
    private AtomicInteger currBookmarkNum = new AtomicInteger(0);

    // persistence file name. this could be set to the file name or its full path.
    private String persistenceFileName = null;

    // UI related
    private String  textTitle = "Bookmarks";
    private JLabel  labelTitle;
    private JTable  tableBookmarks;
    private JScrollPane scrollPane;
    private JPanel  controlsPanel;
    private JPanel  panelButtons;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JButton buttonRemoveAll;

// ------------------------------------------------------------------------
// Constructors
// ------------------------------------------------------------------------
    /**
     * Creates a bookmark UI that is visible by default.
     * @param bookmarkCallback callback with logic to serialize/de-serialize bookmarks, and
     * the action to be performed on bookmark selection.
     * @param persistenceFileName file to which the bookmarks will be persisted.
     * It could be just the file name or the file's full path.
     * If null, the bookmarks will NOT be persisted.
     */
    public JBookmark(IBookmarkCallback<B, SB> bookmarkCallback, String persistenceFileName) {
        this.bookmarkCallback = bookmarkCallback;
        this.persistenceFileName = persistenceFileName;
        initializeModel();
        initGUI();
    }

    /**
     * Creates a bookmark UI that is visible by default.
     * The bookmarks will NOT be persisted when this is used.
     * @see #JBookmark(IBookmarkCallback, String)
     */
    public JBookmark(IBookmarkCallback<B, SB> bookmarkCallback) {
        this(bookmarkCallback, null);
    }

// ------------------------------------------------------------------------
// Public methods
// ------------------------------------------------------------------------
    // --------------------------------------------------------------------
    // bookmark related API
    // --------------------------------------------------------------------
    /**
     * Adds a bookmark to the existing ones.
     * @param label bookmark label that will be displayed.
     * @param bookmark actual bookmark that corresponds to the label.
     */
    public void addBookmark(String label, B bookmark) {
        int id = currBookmarkNum.incrementAndGet();
        modelBookmarks.insertRow(modelBookmarks.getRowCount(), new Object[] {id, label, bookmark});
    }

    /**
     * Adds a bookmark to the model. The label will be auto-generated.
     * @param bookmark bookmark to be added.
     */
    public void addBookmark(B bookmark) {
        int id = currBookmarkNum.incrementAndGet();
        String label = labelPrefix + id;
        modelBookmarks.insertRow(modelBookmarks.getRowCount(), new Object[] {id, label, bookmark});
    }

    /**
     * Removes all bookmarks.
     */
    public void clearBookmarks() {
        modelBookmarks.setRowCount(0);
        tableBookmarks.validate();
        currBookmarkNum.set(0);
    }

    /**
     * Gets the total number of bookmarks.
     * @return the total number of bookmarks.
     */
    public int getBookmarkCount() {
        return modelBookmarks.getRowCount();
    }

    // --------------------------------------------------------------------
    // UI related API
    // --------------------------------------------------------------------
    /**
     * Sets the text for the title.
     * @param titleText title text
     */
    public void setTitleText(String titleText) {
        this.textTitle = titleText;
        labelTitle.setText(titleText);
    }
    /**
     * Returns the title text.
     * @return the title text.
     */
    public String getTitleText() {
        return labelTitle.getText();
    }

    /**
     * Sets the prefix for the bookmark label. This prefix is used when the label is auto-generated.
     * @param labelPrefix label prefix.
     */
    public void setLabelPrefix(String labelPrefix) {
        this.labelPrefix = labelPrefix;
    }
    /**
     * Returns the bookmark label prefix used for auto-generated labels.
     * @return the bookmark label prefix.
     */
    public String getLabelPrefix() {
        return labelPrefix;
    }

    /**
     * Changes the background of this UI, including the title.
     * @param background background of this UI, including the title.
     */
    @Override
    public void setBackground(Color background) {
        super.setBackground(background);
        if (labelTitle != null) {
            labelTitle.setBackground(getBackground());
        }
    }
    
    /**
     * Changes the title text color.
     * @param foreground color for the title.
     */
    @Override
    public void setForeground(Color foreground) {
      super.setForeground(foreground);
      if (labelTitle != null) {
        labelTitle.setForeground(foreground);
      }
    }

// ------------------------------------------------------------------------
// Private methods
// ------------------------------------------------------------------------
    /**
     * Initializes the table model.
     */
    private void initializeModel() {
        // the table model has columns specified in the {@link BookmarkColumns}
        modelBookmarks.setColumnIdentifiers(BookmarkColumns.getColumnIdentifiers());

        // attach a listener to the model that will handle persistence
        if (persistenceFileName == null || bookmarkCallback == null) {
            return;
        }
        modelBookmarks.addTableModelListener(new BookmarkPersistenceHandler<B, SB>(
            modelBookmarks,
            persistenceFileName,
            bookmarkCallback));
    }

    /**
     * Initializes the GUI.
     */
    private void initGUI() {
        setLayout(new BorderLayout());
        setSize(210, 180);
        setBorder(BorderFactory.createCompoundBorder(
          new LineBorder(Color.DARK_GRAY, 1), new EmptyBorder(5, 10, 10, 10)));
        setVisible(true);

        labelTitle = new JLabel(textTitle);
        labelTitle.setOpaque(false);
        labelTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setOpaque(false);
        
        panelButtons = new JPanel();
        FlowLayout lytPnlButtons = new FlowLayout(FlowLayout.CENTER, 0, 0);
        panelButtons.setLayout(lytPnlButtons);
        panelButtons.setOpaque(false);
        
        buttonAdd = new JButton("+");
        buttonAdd.setToolTipText("Add bookmark.");
        buttonAdd.setOpaque(false);
        buttonAdd.setFocusPainted(false);
        panelButtons.add(buttonAdd);

        buttonRemove = new JButton("x");
        buttonRemove.setToolTipText("Remove selected bookmark.");
        buttonRemove.setOpaque(false);
        buttonRemove.setFocusPainted(false);
        panelButtons.add(buttonRemove);

        buttonRemoveAll = new JButton("Remove All");
        buttonRemoveAll.setToolTipText("Remove all bookmarks.");
        buttonRemoveAll.setOpaque(false);
        buttonRemoveAll.setFocusPainted(false);
        panelButtons.add(buttonRemoveAll);
        
        controlsPanel.add(labelTitle);
        controlsPanel.add(panelButtons);
        add(controlsPanel, BorderLayout.NORTH);

        // make the header, id and value column invisible
        // the only display column is the bookmark label.
        currBookmarkNum.set(modelBookmarks.getRowCount());
        tableBookmarks = new JTable(modelBookmarks);
        tableBookmarks.setTableHeader(null);
        TableColumn idColumn = tableBookmarks.getColumn(BookmarkColumns.ID.getColumnIdentifier());
        idColumn.setWidth(0);
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        TableColumn valueColumn = tableBookmarks.getColumn(BookmarkColumns.VALUE.getColumnIdentifier());
        valueColumn.setWidth(0);
        valueColumn.setMinWidth(0);
        valueColumn.setMaxWidth(0);
        tableBookmarks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(tableBookmarks);
        scrollPane.setPreferredSize(new Dimension(150, 100));
        add(scrollPane, BorderLayout.CENTER);

        initGuiListeners();
    }

    /**
     * Sets listeners/handlers for all UI components.
     */
    private void initGuiListeners() {

        // delegate the action to be taken on bookmark selection to the callback
        BookmarkClickListener<B, SB> clickListener = new BookmarkClickListener<B, SB>(tableBookmarks, bookmarkCallback);
        tableBookmarks.addMouseListener(clickListener);

        // add bookmark to the model
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookmark(bookmarkCallback.getBookmarkToAdd());
                tableBookmarks.clearSelection();
                tableBookmarks.changeSelection(tableBookmarks.getRowCount() - 1, 0, true, false);
            }
        });

        // remove selected bookmark from the model
        buttonRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableBookmarks.getSelectedRow();
                if (selectedRow >= 0) {
                    modelBookmarks.removeRow(selectedRow);
                    tableBookmarks.changeSelection(selectedRow, 0, true, false);
                }
            }
        });

        // remove all bookmarks from the model
        buttonRemoveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearBookmarks();
            }
        });

        // handle mouse drag
        ComponentDragger panelDragListener = new ComponentDragger(this);
        addMouseMotionListener(panelDragListener);
        addMouseListener(panelDragListener);
    }

}

class BookmarkClickListener<B, SB> implements MouseListener {

    JTable bookmarkTable;
    IBookmarkCallback<B, SB> bookmarkCallback;

    public BookmarkClickListener(JTable bookmarkTable, IBookmarkCallback<B, SB> bookmarkCallback) {
        this.bookmarkTable = bookmarkTable;
        this.bookmarkCallback = bookmarkCallback;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getComponent() instanceof JTable) {
            bookmarkCallback.onSelectBookmark(
                    (B) bookmarkTable.getModel().getValueAt(
                            bookmarkTable.getSelectedRow(), BookmarkColumns.VALUE.getColumnIndex()));
        }
    }

}

/**
 * Class to handle bookmark persistence.
 */
class BookmarkPersistenceHandler<B, SB> implements TableModelListener {

    // model whose data will be persisted
    DefaultTableModel model;
    // persistence file
    File persistenceFile;
    // value/field separator
    String VAL_SEP = ",";
    // callback to handle serialization/de-serialization
    IBookmarkCallback<B, SB> bookmarkCallback;

    /**
     * Persistence handler.
     * @param model model that will be persisted.
     * @param persistenceFileName persistence file.
     */
    public BookmarkPersistenceHandler(
        DefaultTableModel model,
        String persistenceFileName,
        IBookmarkCallback<B, SB> bookmarkCallback) {
        this.model = model;
        this.persistenceFile = new File(persistenceFileName);
        this.bookmarkCallback = bookmarkCallback;
        // if the file exists, then initialize the model with the data in file
        loadFromFile();
    }

    /**
     * Whenever the model is changed, persist it to the file.
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        persistToFile();
    }

    /**
     * Set the persistence file.
     * @param persistenceFile persistence file.
     */
    public void setPersistenceFile(File persistenceFile) {
        this.persistenceFile = persistenceFile;
    }

    /**
     * Persist model to file.
     */
    private void persistToFile() {
        try {
            if (!persistenceFile.exists()) {
                persistenceFile.createNewFile();
            }

            Properties properties = new Properties();
            for (int r = model.getRowCount() - 1; r >= 0; r--) {
                properties.put(r + "",
                    model.getValueAt(r, BookmarkColumns.LABEL.getColumnIndex()) +
                    VAL_SEP +
                    bookmarkCallback.getSerializedBookmark(
                        (B) model.getValueAt(r, BookmarkColumns.VALUE.getColumnIndex())));
            }

            FileOutputStream fout = new FileOutputStream(persistenceFile);
            properties.store(fout, "");
            fout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads bookmarks from the persistence file and initializes the model.
     */
    private void loadFromFile() {
        try {
            if (persistenceFile.exists()) {
                FileInputStream fin = new FileInputStream(persistenceFile);
                Properties properties = new Properties();
                properties.load(fin);
                int rowIndex = 0;
                for (Entry<Object, Object> prop : properties.entrySet()) {
                    String[] labelValue = ((String) prop.getValue()).split(VAL_SEP, 2);
                    model.insertRow(0, new Object[] {
                        rowIndex++,
                        labelValue[0],
                        bookmarkCallback.getDeserializedBookmark((SB) labelValue[1])});
                }
                fin.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

/**
 * Columns for the bookmarks.
 */
enum BookmarkColumns {
    // id to uniquely identify a bookmark.
    ID(0),
    // label for the bookmark that will be displayed to the user.
    LABEL(1),
    // underlying value of the bookmark (e.g, the extent).
    VALUE(2);

    // column index
    private int colIndex;

    private BookmarkColumns(int colIndex) {
        this.colIndex = colIndex;
    }

    /**
     * Returns all columns identifiers.
     * @return column identifiers.
     */
    public static Object[] getColumnIdentifiers() {
        return new Object[] {ID.toString(), LABEL.toString(), VALUE.toString()};
    }

    public int getColumnIndex() {
        return colIndex;
    }

    public String getColumnIdentifier() {
        return this.toString();
    }
}
