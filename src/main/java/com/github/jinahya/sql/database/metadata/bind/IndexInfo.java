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

import java.io.Serializable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

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
@XmlType(propOrder = {
    "nonUnique", "indexQualifier", "indexName", "type", "ordinalPosition",
    "columnName", "ascOrDesc", "cardinality", "pages", "filterCondition"
})
public class IndexInfo implements Serializable {

    private static final long serialVersionUID = -768486884376018474L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(IndexInfo.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableCat=" + tableCat
               + ",tableSchem=" + tableSchem
               + ",tableName=" + tableName
               + ",nonUnique=" + nonUnique
               + ",indexQualifier=" + indexQualifier
               + ",indexName=" + indexName
               + ",type=" + type
               + ",ordinalPosition=" + ordinalPosition
               + ",columnName=" + columnName
               + ",ascOrDesc=" + ascOrDesc
               + ",cardinality=" + cardinality
               + ",pages=" + pages
               + ",filterCondition=" + filterCondition
               + "}";
    }

    // ---------------------------------------------------------------- tableCat
//    public String getTableCat() {
//        return tableCat;
//    }
//
//    public void setTableCat(final String tableCat) {
//        this.tableCat = tableCat;
//    }

    // -------------------------------------------------------------- tableSchem
//    public String getTableSchem() {
//        return tableSchem;
//    }
//
//    public void setTableSchem(final String tableSchem) {
//        this.tableSchem = tableSchem;
//    }

    // --------------------------------------------------------------- tableName
//    public String getTableName() {
//        return tableName;
//    }
//
//    public void setTableName(final String tableName) {
//        this.tableName = tableName;
//    }

    // --------------------------------------------------------------- nonUnique
//    public boolean isNonUnique() {
//        return nonUnique;
//    }
//
//    public void setNonUnique(final boolean nonUnique) {
//        this.nonUnique = nonUnique;
//    }

    // ---------------------------------------------------------- indexQualifier
//    public String getIndexQualifier() {
//        return indexQualifier;
//    }
//
//    public void setIndexQualifier(final String indexQualifier) {
//        this.indexQualifier = indexQualifier;
//    }

    // --------------------------------------------------------------- indexName
//    public String getIndexName() {
//        return indexName;
//    }
//
//    public void setIndexName(final String indexName) {
//        this.indexName = indexName;
//    }

    // -------------------------------------------------------------------- type
//    public short getType() {
//        return type;
//    }
//
//    public void setType(final short type) {
//        this.type = type;
//    }

    // --------------------------------------------------------- ordinalPosition
//    public short getOrdinalPosition() {
//        return ordinalPosition;
//    }
//
//    public void setOrdinalPosition(final short ordinalPosition) {
//        this.ordinalPosition = ordinalPosition;
//    }

    // -------------------------------------------------------------- columnName
//    public String getColumnName() {
//        return columnName;
//    }
//
//    public void setColumnName(final String columnName) {
//        this.columnName = columnName;
//    }

    // --------------------------------------------------------------- ascOrDesc
//    public String getAscOrDesc() {
//        return ascOrDesc;
//    }
//
//    public void setAscOrDesc(final String ascOrDesc) {
//        this.ascOrDesc = ascOrDesc;
//    }

    // ------------------------------------------------------------- cardinality
//    public long getCardinality() {
//        return cardinality;
//    }
//
//    public void setCardinality(final long cardinality) {
//        this.cardinality = cardinality;
//    }

    // ------------------------------------------------------------------- pages
//    public long getPages() {
//        return pages;
//    }
//
//    public void setPages(final long pages) {
//        this.pages = pages;
//    }
    // --------------------------------------------------------- filterCondition
//    public String getFilterCondition() {
//        return filterCondition;
//    }
//
//    public void setFilterCondition(final String filterCondition) {
//        this.filterCondition = filterCondition;
//    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Labeled("TABLE_CAT")
    @Nillable
    @Getter
    @Setter
    private String tableCat;

    @XmlAttribute
    @Labeled("TABLE_SCHEM")
    @Nillable
    @Getter
    @Setter
    private String tableSchem;

    @XmlAttribute
    @Labeled("TABLE_NAME")
    @Getter
    @Setter
    private String tableName;

    // -------------------------------------------------------------------------
    @XmlElement(required = true)
    @Labeled("NON_UNIQUE")
    @Getter
    @Setter
    private boolean nonUnique;

    @XmlElement(nillable = true, required = true)
    @Labeled("INDEX_QUALIFIER")
    @Nillable
    @Getter
    @Setter
    private String indexQualifier;

    @XmlElement(nillable = true, required = true)
    @Labeled("INDEX_NAME")
    @Nillable
    @Getter
    @Setter
    private String indexName;

    @XmlElement(required = true)
    @Labeled("TYPE")
    @Getter
    @Setter
    private short type;

    @XmlElement(required = true)
    @Labeled("ORDINAL_POSITION")
    @Getter
    @Setter
    private short ordinalPosition;

    @XmlElement(nillable = true, required = true)
    @Labeled("COLUMN_NAME")
    @Nillable
    @Getter
    @Setter
    private String columnName;

    @XmlElement(nillable = true, required = true)
    @Labeled("ASC_OR_DESC")
    @Nillable
    @Getter
    @Setter
    private String ascOrDesc;

    @XmlElement(required = true)
    @Labeled("CARDINALITY")
    @Getter
    @Setter
    private long cardinality;

    @XmlElement(required = true)
    @Labeled("PAGES")
    @Getter
    @Setter
    private long pages;

    @XmlElement(nillable = true, required = true)
    @Labeled("FILTER_CONDITION")
    @Nillable
    @Getter
    @Setter
    private String filterCondition;
}
