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

package ca.sqlpower.wabit.report;

import ca.sqlpower.wabit.AbstractWabitObjectTest;
import ca.sqlpower.wabit.WabitObject;
import ca.sqlpower.wabit.report.Guide.Axis;

public class GuideTest extends AbstractWabitObjectTest {

    Guide guide;
    
    @Override
    public Class<? extends WabitObject> getParentClass() {
    	return Page.class;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        guide = new Guide(Axis.HORIZONTAL, 123);
        
        Report report = new Report("report");
        report.getPage().addGuide(guide);
        
        getWorkspace().addReport(report);
    }
    
    @Override
    public WabitObject getObjectUnderTest() {
        return guide;
    }
}
