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

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getPrimaryKeys(String, String, String)
 */
@EqualsAndHashCode(callSuper = true)
public class PrimaryKey
        extends AbstractMetadataType {

    private static final long serialVersionUID = 3159826510060898330L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<PrimaryKey> comparing(final Comparator<? super String> comparator) {
        return Comparator.comparing(PrimaryKey::getColumnName, comparator);
    }

    static Comparator<PrimaryKey> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return comparing(ContextUtils.nullPrecedence(context, comparator));
    }

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**x
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // ------------------------------------------------------------------------------------------------------ TABLE_NAME

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // --------------------------------------------------------------------------------------------------------- KEY_SEQ

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_KEY_SEQ = "KEY_SEQ";

    // --------------------------------------------------------------------------------------------------------- PK_NAME

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_PK_NAME = "PK_NAME";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    protected PrimaryKey() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",columnName=" + columnName +
               ",keySeq=" + keySeq +
               ",pkName=" + pkName +
               '}';
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    @Nullable
    public String getTableCat() {
        return tableCat;
    }

    protected void setTableCat(@Nullable final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }

    protected void setTableSchem(@Nullable final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    protected void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    protected void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ---------------------------------------------------------------------------------------------------------- keySeq
    public Integer getKeySeq() {
        return keySeq;
    }

    protected void setKeySeq(final Integer keySeq) {
        this.keySeq = keySeq;
    }

    // ---------------------------------------------------------------------------------------------------------- pkName
    @Nullable
    public String getPkName() {
        return pkName;
    }

    protected void setPkName(@Nullable final String pkName) {
        this.pkName = pkName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @_ColumnLabel(COLUMN_LABEL_KEY_SEQ)
    private Integer keySeq;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PK_NAME)
    private String pkName;
}
