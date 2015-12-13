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
 * {@link java.sql.DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see java.sql.DatabaseMetaData#getIndexInfo(java.lang.String,
 * java.lang.String, java.lang.String, boolean, boolean)
 * @see MetadataContext#getIndexInfo(java.lang.String, java.lang.String,
 * java.lang.String, boolean, boolean)
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "nonUnique", "indexQualifier", "indexName", "type", "ordinalPosition",
        "columnName", "ascOrDesc", "cardinality", "pages", "filterCondition"
    }
)
public class IndexInfo extends AbstractChild<Table> {


    public static Comparator<IndexInfo> natural() {

        return new Comparator<IndexInfo>() {

            @Override
            public int compare(final IndexInfo o1, final IndexInfo o2) {

                // by NON_UNIQUE, TYPE, INDEX_NAME, and ORDINAL_POSITION.
                return new CompareToBuilder()
                    .append(o1.isNonUnique(), o2.isNonUnique())
                    .append(o1.getType(), o2.getType())
                    .append(o1.getIndexName(), o2.getIndexName())
                    .append(o1.getOrdinalPosition(), o2.getOrdinalPosition())
                    .build();
            }

        };
    }


    @Override
    public String toString() {

        return super.toString() + "{"
               + "tableCat=" + tableCat
               + ", tableSchem=" + tableSchem
               + ", tableName=" + tableName
               + ", nonUnique=" + nonUnique
               + ", indexQualifier=" + indexQualifier
               + ", indexName=" + indexName
               + ", type=" + type
               + ", ordinalPosition=" + ordinalPosition
               + ", columnName=" + columnName
               + ", ascOrDesc=" + ascOrDesc
               + ", cardinality=" + cardinality
               + ", pages=" + pages
               + ", filterCondition=" + filterCondition
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


    // --------------------------------------------------------------- nonUnique
    public boolean isNonUnique() {

        return nonUnique;
    }


    public void setNonUnique(final boolean nonUnique) {

        this.nonUnique = nonUnique;
    }


    // ---------------------------------------------------------- indexQualifier
    public String getIndexQualifier() {

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
    // just for class diagram
    private Table getTable() {

        return getParent();
    }


//    public void setTable(final Table table) {
//
//        setParent(table);
//    }
    // -------------------------------------------------------------------------
    @Label("TABLE_CAT")
    @NillableBySpecification
    @XmlAttribute
    private String tableCat;


    @Label("TABLE_SCHEM")
    @NillableBySpecification
    @XmlAttribute
    private String tableSchem;


    @Label("TABLE_NAME")
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

}

