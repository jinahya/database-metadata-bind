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
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getPrimaryKeys(String, String, String)
 */

@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class PrimaryKey
        extends AbstractMetadataType {

    private static final long serialVersionUID = 3159826510060898330L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<PrimaryKey> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
//        return Comparator.comparing(PrimaryKey::getTableCat, ContextUtils.nulls(context, comparator))
//                .thenComparing(PrimaryKey::getTableSchem, ContextUtils.nulls(context, comparator))
//                .thenComparing(PrimaryKey::getTableName, ContextUtils.nulls(context, comparator))
//                .thenComparing(PrimaryKey::getColumnName, ContextUtils.nulls(context, comparator));
        return Comparator.comparing(PrimaryKey::getColumnName, ContextUtils.nulls(context, comparator));
    }

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**
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

    // -------------------------------------------------------------------------------------------------------- tableCat

    // ------------------------------------------------------------------------------------------------------ tableSchem

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
    @EqualsAndHashCode.Include
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    @EqualsAndHashCode.Include
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @Positive
    @_ColumnLabel(COLUMN_LABEL_KEY_SEQ)
    private Integer keySeq;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PK_NAME)
    @EqualsAndHashCode.Include
    private String pkName;
}
