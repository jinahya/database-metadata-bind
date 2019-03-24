package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * An entity class for index info.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see java.sql.DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
 * @see MetadataContext#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
 */
@XmlRootElement
@XmlType(propOrder = {
        "nonUnique", "indexQualifier", "indexName", "type", "ordinalPosition", "columnName", "ascOrDesc", "cardinality",
        "pages", "filterCondition"
})
public class IndexInfo implements Serializable {

    private static final long serialVersionUID = -768486884376018474L;

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

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Bind(label = "TABLE_CAT", nillable = true)
    private String tableCat;

    @XmlAttribute
    @Bind(label = "TABLE_SCHEM", nillable = true)
    private String tableSchem;

    @XmlAttribute
    @Bind(label = "TABLE_NAME")
    private String tableName;

    // -------------------------------------------------------------------------
    @XmlElement
    @Bind(label = "NON_UNIQUE")
    private boolean nonUnique;

    @XmlElement(nillable = true)
    @Bind(label = "INDEX_QUALIFIER", nillable = true)
    private String indexQualifier;

    @XmlElement(nillable = true)
    @Bind(label = "INDEX_NAME", nillable = true)
    private String indexName;

    @XmlElement
    @Bind(label = "TYPE")
    private short type;

    @XmlElement
    @Bind(label = "ORDINAL_POSITION")
    private short ordinalPosition;

    @XmlElement(nillable = true)
    @Bind(label = "COLUMN_NAME", nillable = true)
    private String columnName;

    @XmlElement(nillable = true)
    @Bind(label = "ASC_OR_DESC", nillable = true)
    private String ascOrDesc;

    @XmlElement
    @Bind(label = "CARDINALITY")
    private long cardinality;

    @XmlElement
    @Bind(label = "PAGES")
    private long pages;

    @XmlElement(nillable = true)
    @Bind(label = "FILTER_CONDITION", nillable = true)
    private String filterCondition;
}
