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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Objects;

/**
 * An entity class for index info.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see java.sql.DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
 * @see Context#getIndexInfo(String, String, String, boolean, boolean, Collection)
 */
@XmlRootElement
public class IndexInfo implements MetadataType {

    private static final long serialVersionUID = -768486884376018474L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public IndexInfo() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final IndexInfo that = (IndexInfo) obj;
        return nonUnique == that.nonUnique
               && type == that.type
               && ordinalPosition == that.ordinalPosition
               && cardinality == that.cardinality
               && pages == that.pages
               && Objects.equals(tableCat, that.tableCat)
               && Objects.equals(tableSchem, that.tableSchem)
               && Objects.equals(tableName, that.tableName)
               && Objects.equals(indexQualifier, that.indexQualifier)
               && Objects.equals(indexName, that.indexName)
               && Objects.equals(columnName, that.columnName)
               && Objects.equals(ascOrDesc, that.ascOrDesc)
               && Objects.equals(filterCondition, that.filterCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat,
                            tableSchem,
                            tableName,
                            nonUnique,
                            indexQualifier,
                            indexName,
                            type,
                            ordinalPosition,
                            columnName,
                            ascOrDesc,
                            cardinality,
                            pages,
                            filterCondition);
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------- nonUnique
    public boolean isNonUnique() {
        return nonUnique;
    }

    public void setNonUnique(final boolean nonUnique) {
        this.nonUnique = nonUnique;
    }

    // -------------------------------------------------------------------------------------------------- indexQualifier
    public String getIndexQualifier() {
        return indexQualifier;
    }

    public void setIndexQualifier(final String indexQualifier) {
        this.indexQualifier = indexQualifier;
    }

    // ------------------------------------------------------------------------------------------------------- indexName
    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(final String indexName) {
        this.indexName = indexName;
    }

    // ------------------------------------------------------------------------------------------------------------ type
    public short getType() {
        return type;
    }

    public void setType(final short type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition
    public short getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final short ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ------------------------------------------------------------------------------------------------------- ascOrDesc
    public String getAscOrDesc() {
        return ascOrDesc;
    }

    public void setAscOrDesc(final String ascOrDesc) {
        this.ascOrDesc = ascOrDesc;
    }

    // ----------------------------------------------------------------------------------------------------- cardinality
    public long getCardinality() {
        return cardinality;
    }

    public void setCardinality(final long cardinality) {
        this.cardinality = cardinality;
    }

    // ----------------------------------------------------------------------------------------------------------- pages
    public long getPages() {
        return pages;
    }

    public void setPages(final long pages) {
        this.pages = pages;
    }

    // ------------------------------------------------------------------------------------------------- filterCondition
    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(final String filterCondition) {
        this.filterCondition = filterCondition;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(required = true)
    @Label("TABLE_NAME")
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("NON_UNIQUE")
    private boolean nonUnique;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("INDEX_QUALIFIER")
    private String indexQualifier;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("INDEX_NAME")
    private String indexName;

    @XmlElement(required = true)
    @Label("TYPE")
    private short type;

    @XmlElement(required = true)
    @Label("ORDINAL_POSITION")
    private short ordinalPosition;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("ASC_OR_DESC")
    private String ascOrDesc;

    @XmlElement(required = true)
    @Label("CARDINALITY")
    private long cardinality;

    @XmlElement(required = true)
    @Label("PAGES")
    private long pages;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("FILTER_CONDITION")
    private String filterCondition;
}
