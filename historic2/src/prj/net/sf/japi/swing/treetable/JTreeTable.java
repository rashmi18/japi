/*
 * Copyright (C) 2009  Christian Hujer.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.sf.japi.swing.treetable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;

/** This example shows how to create a simple JTreeTable component,
 * by using a JTree as a renderer (and editor) for the cells in a
 * particular column in the JTable.
 * @param <R> root type
 * @param <T> node type
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.1
 * @warning DO NOT RELY ON THE INHERITANCE!
 */
public class JTreeTable<R, T> extends JTable {

    /** TreeTableCellRenderer.
     * @serial include
     */
    private final TreeTableCellRenderer tree;

    /** The TreeTableModel for this JTreeTable.
     * @serial include
     */
    private TreeTableModel<R, T> treeTableModel;

    /** Create a JTreeTable.
     * @param treeTableModel TreeTableModel to create the view/controller for.
     */
    public JTreeTable(final TreeTableModel<R, T> treeTableModel) {
        this.treeTableModel = treeTableModel;
        tree = new TreeTableCellRenderer(new TreeTableModelTreeModelAdapter<R, T>(treeTableModel));
        setModel(new TreeTableModelTableModelAdapter(treeTableModel, tree));

        // Force the JTable and JTree to share their row selection models.
        tree.setSelectionModel(new DefaultTreeSelectionModel() {

            /** Serial version. */
            private static final long serialVersionUID = 1L;
            {
                setSelectionModel(listSelectionModel);
            }
        });
        // Make the tree and table row heights the same.
        tree.setRowHeight(getRowHeight());

        // Install the tree editor renderer and editor.
        setDefaultRenderer(TreeTableModel.class, tree);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
    }

    /** Returns the TreeTableModel for this JTreeTable.
     * @return the TreeTableModel for this JTreeTable.
     */
    public TreeTableModel<R, T> getTreeTableModel() {
        return treeTableModel;
    }

    /* Workaround for BasicTableUI anomaly. Make sure the UI never tries to
     * paint the editor. The UI currently uses different techniques to
     * paint the renderers and editors and overriding setBounds() below
     * is not the right thing to do for an editor. Returning -1 for the
     * editing row in this case, ensures the editor is never painted.
     */
    @Override public int getEditingRow() {
        return getColumnClass(editingColumn) == TreeTableModel.class ? -1 : editingRow;
    }

    /** Sets the TreeTableModel for this JTreeTable.
     * @param treeTableModel the TreeTableModel for this JTreeTable.
     */
    public void setTreeTableModel(final TreeTableModel<R, T> treeTableModel) {
        this.treeTableModel = treeTableModel;
        setModel(new TreeTableModelTableModelAdapter(treeTableModel, tree));
    }

    /** Renderer for TreeTableCells. */
    public class TreeTableCellRenderer extends JTree implements TableCellRenderer {

        /** Row that's currently visible.
         * @serial include
         */
        private int visibleRow;

        /** Create a TreeTableCellRenderer.
         * @param model TreeModel to display.
         */
        public TreeTableCellRenderer(final TreeModel model) {
            super(model);
        }

        /** {@inheritDoc} */
        @Override public void setBounds(final int x, final int y, final int width, final int height) {
            super.setBounds(x, 0, width, JTreeTable.this.getHeight());
        }

        /** {@inheritDoc} */
        @Override public void paint(final Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());
            super.paint(g);
        }

        /** {@inheritDoc} */
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }

            visibleRow = row;
            return this;
        }

    } // class TreeTableCellRenderer

    /** Editor for TreeTableCells. */
    public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        /** Serial Version. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
            return tree;
        }

        /** {@inheritDoc} */
        public Object getCellEditorValue() {
            return ""; // TODO:2009-02-24:christianhujer:Implement.
        }

    } // class TreeTableCellEditor

} // class JTreeTable
