// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

abstract class JDBCDynaClass implements DynaClass, Serializable
{
    protected boolean lowerCase;
    protected DynaProperty[] properties;
    protected Map propertiesMap;
    private Map columnNameXref;
    
    JDBCDynaClass() {
        this.lowerCase = true;
        this.properties = null;
        this.propertiesMap = new HashMap();
    }
    
    public String getName() {
        return this.getClass().getName();
    }
    
    public DynaProperty getDynaProperty(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("No property name specified");
        }
        return this.propertiesMap.get(name);
    }
    
    public DynaProperty[] getDynaProperties() {
        return this.properties;
    }
    
    public DynaBean newInstance() throws IllegalAccessException, InstantiationException {
        throw new UnsupportedOperationException("newInstance() not supported");
    }
    
    protected Class loadClass(final String className) throws SQLException {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = this.getClass().getClassLoader();
            }
            return cl.loadClass(className);
        }
        catch (Exception e) {
            throw new SQLException("Cannot load column class '" + className + "': " + e);
        }
    }
    
    protected DynaProperty createDynaProperty(final ResultSetMetaData metadata, final int i) throws SQLException {
        final String columnName = metadata.getColumnName(i);
        final String name = this.lowerCase ? columnName.toLowerCase() : columnName;
        if (!name.equals(columnName)) {
            if (this.columnNameXref == null) {
                this.columnNameXref = new HashMap();
            }
            this.columnNameXref.put(name, columnName);
        }
        String className = null;
        try {
            final int sqlType = metadata.getColumnType(i);
            switch (sqlType) {
                case 91: {
                    return new DynaProperty(name, Date.class);
                }
                case 93: {
                    return new DynaProperty(name, Timestamp.class);
                }
                case 92: {
                    return new DynaProperty(name, Time.class);
                }
                default: {
                    className = metadata.getColumnClassName(i);
                    break;
                }
            }
        }
        catch (SQLException ex) {}
        Class clazz = Object.class;
        if (className != null) {
            clazz = this.loadClass(className);
        }
        return new DynaProperty(name, clazz);
    }
    
    protected void introspect(final ResultSet resultSet) throws SQLException {
        final ArrayList list = new ArrayList();
        final ResultSetMetaData metadata = resultSet.getMetaData();
        for (int n = metadata.getColumnCount(), i = 1; i <= n; ++i) {
            final DynaProperty dynaProperty = this.createDynaProperty(metadata, i);
            if (dynaProperty != null) {
                list.add(dynaProperty);
            }
        }
        this.properties = list.toArray(new DynaProperty[list.size()]);
        for (int i = 0; i < this.properties.length; ++i) {
            this.propertiesMap.put(this.properties[i].getName(), this.properties[i]);
        }
    }
    
    protected Object getObject(final ResultSet resultSet, final String name) throws SQLException {
        final DynaProperty property = this.getDynaProperty(name);
        if (property == null) {
            throw new IllegalArgumentException("Invalid name '" + name + "'");
        }
        final String columnName = this.getColumnName(name);
        final Class type = property.getType();
        if (type.equals(Date.class)) {
            return resultSet.getDate(columnName);
        }
        if (type.equals(Timestamp.class)) {
            return resultSet.getTimestamp(columnName);
        }
        if (type.equals(Time.class)) {
            return resultSet.getTime(columnName);
        }
        return resultSet.getObject(columnName);
    }
    
    protected String getColumnName(final String name) {
        if (this.columnNameXref != null && this.columnNameXref.containsKey(name)) {
            return this.columnNameXref.get(name);
        }
        return name;
    }
}
