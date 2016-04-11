/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.sql.database.metadata.bind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)}
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "tableName", "supertableName"
})
public class SuperTable extends AbstractChild<Table> {

    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCat=" + tableCat
               + ", tableSchem=" + tableSchem
               + ", tableName=" + tableName
               + ", supertableName=" + supertableName
               + "}";
    }

    // ---------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // -------------------------------------------------------------- tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // --------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ---------------------------------------------------------- supertableName
    public String getSupertableName() {
        return supertableName;
    }

    public void setSupertableName(final String supertableName) {
        this.supertableName = supertableName;
    }

    // ------------------------------------------------------------------- table
    // just for class diagram
    private Table getTable() {
        return getParent();
    }

//    public void setTable(final Table table) {
//
//        setParent(table);
//    }
//
//
//    SuperTable table(final Table table) {
//
//        setTable(table);
//
//        return this;
//    }
    // -------------------------------------------------------------------------
    @Label("TABLE_CAT")
    @_NillableBySpecification
    @XmlAttribute
    private String tableCat;

    @Label("TABLE_SCHEM")
    @_NillableBySpecification
    @XmlAttribute
    private String tableSchem;

    @Label("TABLE_NAME")
    @XmlElement(required = true)
    private String tableName;

    @Label("SUPERTABLE_NAME")
    @XmlElement(required = true)
    private String supertableName;
}
