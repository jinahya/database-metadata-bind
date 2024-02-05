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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getIndexInfo(String, String, String, boolean, boolean)
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class IndexInfo extends AbstractMetadataType {

    private static final long serialVersionUID = 924040226611181424L;

    static final Comparator<IndexInfo> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(IndexInfo::getNonUnique, nullsFirst(naturalOrder()))
                    .thenComparing(IndexInfo::getType, nullsFirst(naturalOrder()))
                    .thenComparing(IndexInfo::getIndexName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(IndexInfo::getOrdinalPosition, nullsFirst(naturalOrder()));

    static final Comparator<IndexInfo> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(IndexInfo::getNonUnique, nullsFirst(naturalOrder()))
                    .thenComparing(IndexInfo::getType, nullsFirst(naturalOrder()))
                    .thenComparing(IndexInfo::getIndexName, nullsFirst(naturalOrder()))
                    .thenComparing(IndexInfo::getOrdinalPosition, nullsFirst(naturalOrder()));

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // ------------------------------------------------------------------------------------------------------ TABLE_NAME
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ------------------------------------------------------------------------------------------------------------ TYPE
    public static final String COLUMN_LABEL_TYPE = "TYPE";

    /**
     * Constants for the {@code type} attribute binds {@value #COLUMN_LABEL_TYPE} column.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public enum Type implements _IntFieldEnum<Type> {

        /**
         * The constant for the
         * {@link DatabaseMetaData#tableIndexStatistic}({@value DatabaseMetaData#tableIndexStatistic}).
         */
        TABLE_INDEX_STATISTIC(DatabaseMetaData.tableIndexStatistic), // 0

        /**
         * The constant for the
         * {@link DatabaseMetaData#tableIndexClustered}({@value DatabaseMetaData#tableIndexClustered}).
         */
        TABLE_INDEX_CLUSTERED(DatabaseMetaData.tableIndexClustered), // 1

        /**
         * The constant for the {@link DatabaseMetaData#tableIndexHashed}({@value DatabaseMetaData#tableIndexHashed}).
         */
        TABLE_INDEX_HASHED(DatabaseMetaData.tableIndexHashed), // 2

        /**
         * The constant for the {@link DatabaseMetaData#tableIndexOther}({@value DatabaseMetaData#tableIndexOther}).
         */
        TABLE_INDEX_OTHER(DatabaseMetaData.tableIndexOther); // 3

        public static Type valueOfType(final int type) {
            return _IntFieldEnum.valueOfFieldValue(Type.class, type);
        }

        Type(final short fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    @EqualsAndHashCode.Include
    String tableCatNonNull() {
        if (tableCat == null) {
            return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
        }
        return tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    @EqualsAndHashCode.Include
    String tableSchemNonNull() {
        if (tableSchem == null) {
            return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
        }
        return tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------------ type
    Type getTypeAsEnum() {
        return Optional.ofNullable(getType())
                .map(Type::valueOfType)
                .orElse(null);
    }

    void setTypeAsEnum(final Type typeAsEnum) {
        setType(
                Optional.ofNullable(typeAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
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
    @EqualsAndHashCode.Include
    private String tableName;

    @_ColumnLabel("NON_UNIQUE")
    @EqualsAndHashCode.Include
    private Boolean nonUnique;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("INDEX_QUALIFIER")
    private String indexQualifier;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("INDEX_NAME")
    @EqualsAndHashCode.Include
    private String indexName;

    @_ColumnLabel(COLUMN_LABEL_TYPE)
    @EqualsAndHashCode.Include
    private Integer type;

    @_ColumnLabel("ORDINAL_POSITION")
    @EqualsAndHashCode.Include
    private Integer ordinalPosition;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_NAME")
    private String columnName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("ASC_OR_DESC")
    private String ascOrDesc;

    @_ColumnLabel("CARDINALITY")
    @EqualsAndHashCode.Include
    private Long cardinality;

    @_ColumnLabel("PAGES")
    private Long pages;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("FILTER_CONDITION")
    private String filterCondition;
}
