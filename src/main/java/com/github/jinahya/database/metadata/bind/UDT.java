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
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getUDTs(String, String, String, int[])
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class UDT extends AbstractMetadataType {

    private static final long serialVersionUID = 8665246093405057553L;

    // -----------------------------------------------------------------------------------------------------------------
    static final Comparator<UDT> CASE_INSENSITIVE_ORDER =
            Comparator.comparingInt(UDT::getDataType)
                    .thenComparing(UDT::typeCatNonNull, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(UDT::typeSchemNonNull, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(UDT::getTypeName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<UDT> LEXICOGRAPHIC_ORDER =
            Comparator.comparingInt(UDT::getDataType)
                    .thenComparing(UDT::typeCatNonNull, naturalOrder())
                    .thenComparing(UDT::typeSchemNonNull, naturalOrder())
                    .thenComparing(UDT::getTypeName, nullsFirst(naturalOrder()));

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CLASS_NAME = "CLASS_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // --------------------------------------------------------------------------------------------------------- typeCat
    @EqualsAndHashCode.Include
    String typeCatNonNull() {
        final String typeCat_ = getTypeCat();
        if (typeCat_ != null) {
            return typeCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem
    @EqualsAndHashCode.Include
    String typeSchemNonNull() {
        final String typeSchem_ = getTypeSchem();
        if (typeSchem_ != null) {
            return typeSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    private String typeCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    private String typeSchem;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    @EqualsAndHashCode.Include
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableByVendor("PostgreSQL")
    @_ColumnLabel(COLUMN_LABEL_CLASS_NAME)
    private String className;

    @NotNull
    @_NonNullBySpecification
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_NullableByVendor("PostgreSQL")
    @_ColumnLabel("REMARKS")
    private String remarks;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("BASE_TYPE")
    private Integer baseType;
}
