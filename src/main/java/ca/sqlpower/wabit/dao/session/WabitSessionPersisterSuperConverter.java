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

package ca.sqlpower.wabit.dao.session;

import java.awt.Font;
import java.awt.Image;
import java.io.InputStream;

import org.olap4j.Axis;
import org.olap4j.metadata.Cube;

import ca.sqlpower.dao.session.JDBCDataSourceConverter;
import ca.sqlpower.dao.session.SessionPersisterSuperConverter;
import ca.sqlpower.query.Container;
import ca.sqlpower.query.Item;
import ca.sqlpower.query.SQLJoin;
import ca.sqlpower.sql.JDBCDataSource;
import ca.sqlpower.sql.Olap4jDataSource;
import ca.sqlpower.wabit.WabitObject;
import ca.sqlpower.wabit.WabitSession;

/**
 * @see SessionPersisterSuperConverter
 */
public class WabitSessionPersisterSuperConverter extends SessionPersisterSuperConverter {
	
	private final CubeConverter cubeConverter;
	
	private final FontConverter fontConverter;
	
	private final SQLJoinConverter sqlJoinConverter;
	
	private final JDBCDataSourceConverter jdbcDataSourceConverter;
	
	private final Olap4jDataSourceConverter olap4jDataSourceConverter;
	
	private final ItemConverter itemConverter;
	
	private final ContainerConverter containerConverter;
	
	private final PNGImageConverter pngImageConverter = new PNGImageConverter();
	
	private final Olap4JAxisConverter olap4jAxisConverter = new Olap4JAxisConverter();

    /**
     * @param containerDoPopulate
     *            If set to false the container model objects will be considered
     *            populated and will not try to populate again. This is useful
     *            for places like the session on the server that can just accept
     *            the objects given to it, it does not need to manipulate the
     *            objects further. If true the container model object named in
     *            containers created by this converter will be populated when
     *            accessed.
     * @see SessionPersisterSuperConverter#SessionPersisterSuperConverter(ca.sqlpower.util.SPSession,
     *      ca.sqlpower.object.SPObject)
     */
	public WabitSessionPersisterSuperConverter(WabitSession session, WabitObject root, boolean containerDoPopulate) {
		super(session.getDataSources(), root);
		cubeConverter = new CubeConverter(session.getContext(), session.getDataSources());
		fontConverter = new FontConverter(session.getFontLoader());
		containerConverter = new ContainerConverter(session, containerDoPopulate);
		sqlJoinConverter = new SQLJoinConverter(root);
		jdbcDataSourceConverter = new JDBCDataSourceConverter(session.getDataSources());
		olap4jDataSourceConverter = new Olap4jDataSourceConverter(session.getWorkspace());
		itemConverter = new ItemConverter(session.getWorkspace());
	}
	
	@Override
	public Object convertToBasicType(Object convertFrom, Object ... additionalInfo) {
		try {
			return super.convertToBasicType(convertFrom, additionalInfo);
		} catch (IllegalArgumentException e) {
			// Could not convert to basic type through SessionPersisterSuperConverter.
			// Squishing the exception so that we can also check known types in Wabit.
		}
			
		if (convertFrom instanceof Cube) {
			Cube c = (Cube) convertFrom;
			return cubeConverter.convertToSimpleType(c, additionalInfo);
			
		} else if (convertFrom instanceof Font) {
			Font f = (Font) convertFrom;
			return fontConverter.convertToSimpleType(f);
			
		} else if (convertFrom instanceof SQLJoin) {
			SQLJoin join = (SQLJoin) convertFrom;
			return sqlJoinConverter.convertToSimpleType(join);
			
		} else if (convertFrom instanceof JDBCDataSource) {
			JDBCDataSource jdbcDataSource = (JDBCDataSource) convertFrom;
			return jdbcDataSourceConverter.convertToSimpleType(jdbcDataSource);
			
		} else if (convertFrom instanceof Olap4jDataSource) {
			Olap4jDataSource olap4jDataSource = (Olap4jDataSource) convertFrom;
			return olap4jDataSourceConverter.convertToSimpleType(olap4jDataSource);
			
		} else if (convertFrom instanceof Item) {
			return itemConverter.convertToSimpleType((Item) convertFrom);
			
		} else if (convertFrom instanceof Container) {
			return containerConverter.convertToSimpleType((Container) convertFrom);
			
		} else if (convertFrom instanceof Image) {
			return pngImageConverter.convertToSimpleType((Image) convertFrom);
			
		} else if (convertFrom instanceof Axis) {
			return olap4jAxisConverter.convertToSimpleType((Axis) convertFrom);
			
		} else {
			throw new IllegalArgumentException("Cannot convert " + convertFrom + " of type " + 
					convertFrom.getClass());
		}
	}
	
	@Override
	public Object convertToComplexType(Object o, Class<? extends Object> type) {
		try {
			return super.convertToComplexType(o, type);
		} catch (IllegalArgumentException e) {
			// Could not convert to complex type through SessionPersisterSuperConverter.
			// Squishing the exception so that we can also check known types in Wabit.
		}
		
		if (Cube.class.isAssignableFrom(type)) {
			return cubeConverter.convertToComplexType((String) o);
			
		} else if (Font.class.isAssignableFrom(type)) {
			return fontConverter.convertToComplexType((String) o);
			
		} else if (Container.class.isAssignableFrom(type)) {
			return containerConverter.convertToComplexType((String) o);
			
		} else if (SQLJoin.class.isAssignableFrom(type)) {
			return sqlJoinConverter.convertToComplexType((String) o);

		} else if (JDBCDataSource.class.isAssignableFrom(type)) {
			return jdbcDataSourceConverter.convertToComplexType((String) o);
			
		} else if (Olap4jDataSource.class.isAssignableFrom(type)) {
			return olap4jDataSourceConverter.convertToComplexType((String) o);
			
		} else if (Item.class.isAssignableFrom(type)) {
			return itemConverter.convertToComplexType((String) o);
			
		} else if (Image.class.isAssignableFrom(type)) { 
			//TODO we should pass this the data type to know that we want a PNG 
			//in case other formats are supported in the future
			return pngImageConverter.convertToComplexType((InputStream) o);
			
		} else if (Axis.class.isAssignableFrom(type)) {
			return olap4jAxisConverter.convertToComplexType((String) o);
			
		} else {
			throw new IllegalArgumentException("Cannot convert " + o + " of type " + 
					o.getClass() + " to the type " + type);
		}
	}

}
