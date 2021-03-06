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

package ca.sqlpower.wabit.rs.query;

import java.util.ArrayList;
import java.util.List;

import ca.sqlpower.object.SPObject;
import ca.sqlpower.query.Container;
import ca.sqlpower.query.Item;
import ca.sqlpower.query.SQLObjectItem;
import ca.sqlpower.query.TableContainer;
import ca.sqlpower.wabit.WabitObject;

/**
 * This class distinguishes a WabitContainer as more specifically containing a
 * {@link TableContainer}.
 */
public class WabitTableContainer extends WabitContainer<WabitColumnItem> {

    public WabitTableContainer(Container delegate) {
        super(delegate);
    }

    public WabitTableContainer(Container delegate, boolean createItemWrappers) {
        super(delegate, createItemWrappers);
    }

    @Override
    protected WabitColumnItem createWabitItemChild(Item item) {
    	if (!(item instanceof SQLObjectItem)) {
    		throw new IllegalArgumentException("The item to add to " + getClass() + " with" +
    				" name \"" + getName() + "\" and UUID + \"" + getUUID() + "\" must" +
    						" be of type " + SQLObjectItem.class.getName() + ", not " + 
    						item.getClass() + ".");
    	}
        return new WabitColumnItem((SQLObjectItem) item);
    }

    @Override
    protected Class<WabitColumnItem> getChildClass() {
        return WabitColumnItem.class;
    }
    
    @Override
    /*
     * This method should only be used internally or in special cases such as
     * an undo manager or synchronizing with a server. The children of the 
     * underlying TableContainer cannot be modified so this will only add a new
     * wrapper for one of the children of the TableContainer.
     */
    protected void addChildImpl(SPObject child, int index) {
        final WabitColumnItem wabitItem = (WabitColumnItem) child;
        if (!getDelegate().getItems().contains(wabitItem.getDelegate())) {
            throw new IllegalArgumentException("Cannot add " + child.getName() + " to " + getName() + 
                    " as the container does not have the child " + wabitItem.getDelegate().getName() + 
                    " to wrap.");
        }
        
        for (WabitObject existingChild : getChildren()) {
            if (((WabitColumnItem) existingChild).getDelegate().equals(wabitItem.getDelegate())) {
                throw new IllegalArgumentException("A child for the item " + wabitItem.getDelegate().getName() + 
                        " already exists in " + getName() + " and cannot be added again");
            }
        }
        
        children.add(index, wabitItem);
        child.setParent(this);
        fireChildAdded(child.getClass(), child, index);
    }

    @Override
    /*
     * This method should only be used internally or in special cases such as
     * an undo manager or synchronizing with a server. The children of the
     * underlying TableContainer cannot be modified so this will only remove
     * the Wabit wrapper for one of the columns in the container
     */
    protected boolean removeChildImpl(SPObject child) {
        if (getChildren().contains(child)) {
            int index = children.indexOf(child);
            children.remove(child);
            fireChildRemoved(child.getClass(), child, index);
            return true;
        }
        return false;
    }

	public List<Class<? extends SPObject>> getAllowedChildTypes() {
		List<Class<? extends SPObject>> types = new ArrayList<Class<? extends SPObject>>();
		types.add(WabitColumnItem.class);
		return types;
	}
    
}
