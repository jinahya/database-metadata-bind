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


import java.sql.DatabaseMetaData;
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
@XmlType(
    propOrder = {
        "nonUnique", "indexQualifier", "indexName", "type", "ordinalPosition",
        "columnName", "ascOrDesc", "cardinality", "pages", "filterCondition"
    }
)
public class IndexInfo {


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


    // --------------------------------------------------------------- nonUnique
    public boolean getNonUnique() {

        return nonUnique;
    }


    public void setNonUnique(final boolean nonUnique) {

        this.nonUnique = nonUnique;
    }


    // ---------------------------------------------------------- indexQualifier
    public String setIndexQualifier() {

        return indexQualifier;
    }


    public void setIndexQualifier(final String indexQualifier) {

        this.indexQualifier = indexQualifier;
    }


    // --------------------------------------------------------------- indexName
    public String getIndexName() {

        return indexName;
    }


    public void setIndexName(final String indexName) {

        this.indexName = indexName;
    }


    // -------------------------------------------------------------------- type
    public short getType() {

        return type;
    }


    public void setType(final short type) {

        this.type = type;
    }


    // --------------------------------------------------------- ordinalPosition
    public short getOrdinalPosition() {

        return ordinalPosition;
    }


    public void setOrdinalPosition(final short ordinalPosition) {

        this.ordinalPosition = ordinalPosition;
    }


    // -------------------------------------------------------------- columnName
    public String getColumnName() {

        return columnName;
    }


    public void setColumnName(final String columnName) {

        this.columnName = columnName;
    }


    // --------------------------------------------------------------- ascOrDesc
    public String getAscOrDesc() {

        return ascOrDesc;
    }


    public void setAscOrDesc(final String ascOrDesc) {

        this.ascOrDesc = ascOrDesc;
    }


    // ------------------------------------------------------------- cardinality
    public long getCardinality() {

        return cardinality;
    }


    public void setCardinality(final long cardinality) {

        this.cardinality = cardinality;
    }


    // ------------------------------------------------------------------- pages
    public long getPages() {

        return pages;
    }


    public void setPages(final long pages) {

        this.pages = pages;
    }


    // --------------------------------------------------------- filterCondition
    public String getFilterCondition() {

        return filterCondition;
    }


    public void setFilterCondition(final String filterCondition) {

        this.filterCondition = filterCondition;
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
    }


    @Label("TABLE_CAT")
    @XmlAttribute
    private String tableCat;


    @Label("TABLE_SCHEM")
    @XmlAttribute
    private String tableSchem;


    @Label("TABLE_SCHEM")
    @XmlAttribute
    private String tableName;


    @Label("NON_UNIQUE")
    @XmlElement(required = true)
    private boolean nonUnique;


    @Label("INDEX_QUALIFIER")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String indexQualifier;


    @Label("INDEX_NAME")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String indexName;


    @Label("TYPE")
    @XmlElement(required = true)
    private short type;


    @Label("ORDINAL_POSITION")
    @XmlElement(required = true)
    private short ordinalPosition;


    @Label("COLUMN_NAME")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String columnName;


    @Label("ASC_OR_DESC")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String ascOrDesc;


    @Label("CARDINALITY")
    @XmlElement(required = true)
    private long cardinality;


    @Label("PAGES")
    @XmlElement(required = true)
    private long pages;


    @Label("FILTER_CONDITION")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String filterCondition;


    @XmlTransient
    private Table table;


}

