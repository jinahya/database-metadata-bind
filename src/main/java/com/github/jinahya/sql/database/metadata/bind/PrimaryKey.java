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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {"columnName", "keySeq", "pkName"})
public class PrimaryKey {


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
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
    }


    /**
     * table catalog (may be {@code null}).
     */
    @Label("TABLE_CAT")
    @XmlAttribute
    private String tableCat;


    /**
     * table schema (may be {@code null}).
     */
    @Label("TABLE_SCHEM")
    @XmlAttribute
    private String tableSchem;


    /**
     * table name.
     */
    @Label("TABLE_Name")
    @XmlAttribute
    private String tableName;


    /**
     * column name.
     */
    @Label("COLUMN_NAME")
    @XmlElement(required = true)
    private String columnName;


    /**
     * sequence number within primary key. (a value of 1 represents the first
     * column of the primary key, a value of 2 would represent the second column
     * within the primary key).
     */
    @Label("KEY_SEQ")
    @XmlElement(required = true)
    short keySeq;


    /**
     * primary key name (may be {@code null})
     */
    @Label("PK_NAME")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private String pkName;


    /**
     * parent table.
     */
    @XmlTransient
    private Table table;


}

