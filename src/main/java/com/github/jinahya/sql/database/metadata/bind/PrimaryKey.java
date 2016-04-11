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

import java.util.Comparator;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
        propOrder = {
            "columnName", "keySeq", "pkName"
        }
)
public class PrimaryKey extends AbstractChild<Table> {

    public static Comparator<PrimaryKey> natural() {
        return new Comparator<PrimaryKey>() {
            @Override
            public int compare(final PrimaryKey o1, final PrimaryKey o2) {
                // by COLUMN_NAME.
                return new CompareToBuilder()
                        .append(o1.getColumnName(), o2.getColumnName())
                        .build();
            }
        };
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

    // -------------------------------------------------------------- columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ------------------------------------------------------------------ keySeq
    public short getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(final short keySeq) {
        this.keySeq = keySeq;
    }

    // ------------------------------------------------------------------ pkName
    public String getPkName() {
        return pkName;
    }

    public void setPkName(final String pkName) {
        this.pkName = pkName;
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
    // -------------------------------------------------------------------------
    @_Label("TABLE_CAT")
    @_NillableBySpecification
    @XmlAttribute
    private String tableCat;

    @_Label("TABLE_SCHEM")
    @_NillableBySpecification
    @XmlAttribute
    private String tableSchem;

    @_Label("TABLE_NAME")
    @XmlAttribute
    private String tableName;

    @_Label("COLUMN_NAME")
    @XmlElement(required = true)
    private String columnName;

    @_Label("KEY_SEQ")
    @XmlElement(required = true)
    private short keySeq;

    @_Label("PK_NAME")
    @_NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String pkName;
}
