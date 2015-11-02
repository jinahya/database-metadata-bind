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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlType(
    propOrder = {
        "nonUnique", "indexQualifier", "indexName", "type", "ordinalPosition",
        "columnName", "ascOrDesc", "cardinality", "pages", "filterCondition"
    }
)
public class IndexInfo {


    /**
     * logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(IndexInfo.class);


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schema
     * @param table
     * @param unique
     * @param approximate
     * @param indexInfo
     *
     * @throws SQLException
     *
     * @see DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String,
     * java.lang.String, boolean, boolean)
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final String catalog, final String schema,
                                final String table, final boolean unique,
                                final boolean approximate,
                                final Collection<? super IndexInfo> indexInfo)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (indexInfo == null) {
            throw new NullPointerException("null indexInfo");
        }

        if (suppression.isSuppressed(Table.SUPPRESSION_PATH_INDEX_INFO)) {
            return;
        }

        final ResultSet resultSet = database.getIndexInfo(
            catalog, schema, table, unique, approximate);
        try {
            while (resultSet.next()) {
                indexInfo.add(ColumnRetriever.retrieve(
                    IndexInfo.class, suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Table table)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (table == null) {
            throw new NullPointerException("null table");
        }

        retrieve(database, suppression,
                 table.getSchema().getCatalog().getTableCat(),
                 table.getSchema().getTableSchem(), table.getTableName(),
                 false, false,
                 table.getIndexInfo());

        for (final IndexInfo indexInfo : table.getIndexInfo()) {
            indexInfo.setTable(table);
        }
    }


    /**
     * Creates a nIndexInfostance.
     */
    public IndexInfo() {

        super();
    }


    // ------------------------------------------------------------------- table
    public Table getTable() {

        return table;
    }


    public void setTable(final Table table) {

        this.table = table;
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


    public int getCardinality() {

        return cardinality;
    }


    public void setCardinality(final int cardinality) {

        this.cardinality = cardinality;
    }


    public int getPages() {

        return pages;
    }


    public void setPages(final int pages) {

        this.pages = pages;
    }


    // --------------------------------------------------------- filterCondition
    public String getFilterCondition() {

        return filterCondition;
    }


    public void setFilterCondition(final String filterCondition) {

        this.filterCondition = filterCondition;
    }


    @ColumnLabel("TABLE_CAT")
    @SuppressionPath("indexInfo/tableCat")
    @XmlAttribute
    private String tableCat;


    @ColumnLabel("TABLE_SCHEM")
    @SuppressionPath("indexInfo/tableSchem")
    @XmlAttribute
    private String tableSchem;


    @ColumnLabel("TABLE_SCHEM")
    @XmlAttribute
    private String tableName;


    /**
     * parent table.
     */
    @XmlTransient
    private Table table;


    /**
     * Can index values be non-unique. {@code false} when {@link #type} is
     * {@link DatabaseMetaData#tableIndexStatistic}.
     */
    @ColumnLabel("NON_UNIQUE")
    @XmlElement(required = true)
    boolean nonUnique;


    /**
     * index catalog (may be {@code null}); {@code null} when {@link #type} is
     * {@link DatabaseMetaData#tableIndexStatistic}.
     */
    @ColumnLabel("INDEX_QUALIFIER")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String indexQualifier;


    /**
     * index name; {@code null} when {@link #type} is
     * {@link DatabaseMetaData#tableIndexStatistic}.
     */
    @ColumnLabel("INDEX_NAME")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String indexName;


    /**
     * index type.
     * <ul>
     * <li>{@link DatabaseMetaData#tableIndexStatistic} - this identifies table
     * statistics that are returned in conjuction with a table's index
     * descriptions</li>
     * <li>{@link DatabaseMetaData#tableIndexClustered} - this is a clustered
     * index</li>
     * <li>{@link DatabaseMetaData#tableIndexHashed} - this is a hashed
     * index</li>
     * <li>{@link DatabaseMetaData#tableIndexOther} - this is some other style
     * of index</li>
     * </ul>
     */
    @ColumnLabel("TYPE")
    @XmlElement(required = true)
    short type;


    /**
     * column sequence number within index; zero when {@link #type} is
     * {@link DatabaseMetaData#tableIndexStatistic}.
     */
    @ColumnLabel("ORDINAL_POSITION")
    @XmlElement(required = true)
    short ordinalPosition;


    /**
     * column name; {@code null} when {@link #type} is
     * {@link DatabaseMetaData#tableIndexStatistic}.
     */
    @ColumnLabel("COLUMN_NAME")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String columnName;


    /**
     * column sort sequence, "A" => ascending, "D" => descending, may be
     * {@code null} if sort sequence is not supported; {@code null} when
     * {@link #type} is {@link DatabaseMetaData#tableIndexStatistic}.
     */
    @ColumnLabel("ASC_OR_DESC")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String ascOrDesc;


    /**
     * When {@link #type} is {@link DatabaseMetaData#tableIndexStatistic}, then
     * this is the number of rows in the table; otherwise, it is the number of
     * unique values in the index.
     */
    @ColumnLabel("CARDINALITY")
    @XmlElement(required = true)
    //long cardinality;
    int cardinality;


    /**
     * When {@link #type} is {@link DatabaseMetaData#tableIndexStatisic} then
     * this is the number of pages used for the table, otherwise it is the
     * number of pages used for the current index.
     */
    @ColumnLabel("PAGES")
    @XmlElement(required = true)
    //long pages;
    int pages;


    /**
     * Filter condition, if any. (may be {@code null})
     */
    @ColumnLabel("FILTER_CONDITION")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String filterCondition;


}

