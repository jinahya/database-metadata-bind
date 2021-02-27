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
import java.util.Collection;

/**
 * An entity class for index info.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see java.sql.DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
 * @see Context#getIndexInfo(String, String, String, boolean, boolean, Collection)
 */
@XmlRootElement
public class IndexInfo extends AbstractChild<Table> {

    private static final long serialVersionUID = -768486884376018474L;

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
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
               + '}';
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

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute
    @MayBeNull
    @Label("TABLE_CAT")
    @Bind(label = "TABLE_CAT", nillable = true)
    private String tableCat;

    @XmlAttribute
    @MayBeNull
    @Label("TABLE_SCHEM")
    @Bind(label = "TABLE_SCHEM", nillable = true)
    private String tableSchem;

    @XmlAttribute
    @Label("TABLE_NAME")
    @Bind(label = "TABLE_NAME")
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement
    @Label("NON_UNIQUE")
    @Bind(label = "NON_UNIQUE")
    private boolean nonUnique;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("INDEX_QUALIFIER")
    @Bind(label = "INDEX_QUALIFIER", nillable = true)
    private String indexQualifier;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("INDEX_NAME")
    @Bind(label = "INDEX_NAME", nillable = true)
    private String indexName;

    @XmlElement
    @Label("TYPE")
    @Bind(label = "TYPE")
    private short type;

    @XmlElement
    @Label("ORDINAL_POSITION")
    @Bind(label = "ORDINAL_POSITION")
    private short ordinalPosition;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("COLUMN_NAME")
    @Bind(label = "COLUMN_NAME", nillable = true)
    private String columnName;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("ASC_OR_DESC")
    @Bind(label = "ASC_OR_DESC", nillable = true)
    private String ascOrDesc;

    @XmlElement
    @Label("CARDINALITY")
    @Bind(label = "CARDINALITY")
    private long cardinality;

    @XmlElement
    @Label("PAGES")
    @Bind(label = "PAGES")
    private long pages;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("FILTER_CONDITION")
    @Bind(label = "FILTER_CONDITION", nillable = true)
    private String filterCondition;
}
