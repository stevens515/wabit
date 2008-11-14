/*
 * Copyright (c) 2008, SQL Power Group Inc.
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

package ca.sqlpower.wabit.swingui.report;

import java.awt.Color;

import ca.sqlpower.wabit.WabitObject;
import ca.sqlpower.wabit.report.ContentBox;
import ca.sqlpower.wabit.report.Guide;
import ca.sqlpower.wabit.report.Page;
import edu.umd.cs.piccolo.PNode;

public class PageNode extends PNode {

    private final Page page;
    
    public PageNode(Page page) {
        this.page = page;
        
        for (WabitObject pageChild : page.getChildren()) {
            if (pageChild instanceof Guide) {
                addChild(new GuideNode((Guide) pageChild));
            } else if (pageChild instanceof ContentBox) {
                addChild(new ContentBoxNode((ContentBox) pageChild));
            } else {
                throw new UnsupportedOperationException(
                        "Don't know what view class to use for page child: " + pageChild);
            }
        }
        
        setBounds(0, 0, page.getWidth(), page.getHeight());
        setPaint(Color.WHITE);
    }
    
    @Override
    public boolean setBounds(double x, double y, double width, double height) {
        boolean boundsSet = super.setBounds(x, y, width, height);
        if (boundsSet) {
            page.setWidth((int) width);
            page.setHeight((int) height);
        }
        return boundsSet;
    }

    /**
     * Adds the given node to this page node, and if it's a content box node,
     * also adds that node's underlying content box to this page node's
     * underlying page object.
     */
    @Override
    public void addChild(int index, PNode child) {
        super.addChild(index, child);
        if (child instanceof ContentBoxNode) {
            ContentBox contentBox = ((ContentBoxNode) child).getContentBox();
            if (!page.getChildren().contains(contentBox)) {
                page.addContentBox(contentBox);
            }
        } else if (child instanceof GuideNode) {
            Guide guide = ((GuideNode) child).getGuide();
            if (!page.getChildren().contains(guide)) {
                page.addGuide(guide);
            }
        }
        // There are other types of PNodes added that the model doesn't care about (like selection handles)
    }
}
