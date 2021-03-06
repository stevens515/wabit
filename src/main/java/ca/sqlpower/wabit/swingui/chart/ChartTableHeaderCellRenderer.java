/*
 * Copyright (c) 2009, SQL Power Group Inc.
 *
 * This file is part of Wabit.
 *
 * Wabit is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Wabit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package ca.sqlpower.wabit.swingui.chart;

import java.util.List;

import javax.swing.JComponent;

import ca.sqlpower.swingui.table.CleanupTableCellRenderer;
import ca.sqlpower.wabit.report.chart.ChartColumn;

/**
 * The interface that must be implemented by all components that want to provide
 * a table header in the chart editor.
 */
interface ChartTableHeaderCellRenderer extends CleanupTableCellRenderer {

    /**
     * Returns all the column roles currently defined in this table header.
     */
    List<ChartColumn> getChartColumns();

    /**
     * Returns a component that can be placed beside the header (usually to the
     * left) to explain what the various rows of controls in the header mean.
     */
    JComponent getHeaderLegendComponent();
}
