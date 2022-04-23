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
import java.sql.DatabaseMetaData;
import java.util.Collection;

/**
 * A class for binding results of {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getIndexInfo(String, String, String, boolean, boolean, Collection)
 */
@XmlRootElement
@ChildOf(Table.class)
public class IndexInfo
        implements MetadataType {

    private static final long serialVersionUID = -768486884376018474L;

    /**
     * Creates a new instance.
     */
    public IndexInfo() {
        super();
    }

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

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public boolean isNonUnique() {
        return nonUnique;
    }

    public void setNonUnique(final boolean nonUnique) {
        this.nonUnique = nonUnique;
    }

    public String getIndexQualifier() {
        return indexQualifier;
    }

    public void setIndexQualifier(final String indexQualifier) {
        this.indexQualifier = indexQualifier;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(final String indexName) {
        this.indexName = indexName;
    }

    public short getType() {
        return type;
    }

    public void setType(final short type) {
        this.type = type;
    }

    public short getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final short ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    public String getAscOrDesc() {
        return ascOrDesc;
    }

    public void setAscOrDesc(final String ascOrDesc) {
        this.ascOrDesc = ascOrDesc;
    }

    public long getCardinality() {
        return cardinality;
    }

    public void setCardinality(final long cardinality) {
        this.cardinality = cardinality;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(final long pages) {
        this.pages = pages;
    }

    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(final String filterCondition) {
        this.filterCondition = filterCondition;
    }

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(required = true)
    @Label("TABLE_NAME")
    private String tableName;

    @XmlElement(required = true)
    @Label("NON_UNIQUE")
    private boolean nonUnique;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("INDEX_QUALIFIER")
    private String indexQualifier;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("INDEX_NAME")
    private String indexName;

    @XmlElement(required = true)
    @Label("TYPE")
    private short type;

    @XmlElement(required = true)
    @Label("ORDINAL_POSITION")
    private short ordinalPosition;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("ASC_OR_DESC")
    private String ascOrDesc;

    @XmlElement(required = true)
    @Label("CARDINALITY")
    private long cardinality;

    @XmlElement(required = true)
    @Label("PAGES")
    private long pages;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("FILTER_CONDITION")
    private String filterCondition;
}
