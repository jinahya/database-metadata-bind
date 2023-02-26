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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getUDTs(String, String, String, int[])
 */
@_ParentOf(Attribute.class)
@_ChildOf(Schema.class)
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class UDT extends AbstractMetadataType {

    private static final long serialVersionUID = 8665246093405057553L;

    public static final Comparator<UDT> COMPARING_IN_CASE_INSENSITIVE_ORDER =
            Comparator.comparingInt(UDT::getDataType)
                    .thenComparing(UDT::getUDTId, UDTId.CASE_INSENSITIVE_ORDER);

    public static final Comparator<UDT> COMPARING_IN_LEXICOGRAPHIC_ORDER =
            Comparator.comparingInt(UDT::getDataType)
                    .thenComparing(UDT::getUDTId, UDTId.LEXICOGRAPHIC_ORDER);

    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    public static final String COLUMN_LABEL_CLASS_NAME = "CLASS_NAME";

    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UDT)) return false;
        final UDT that = (UDT) obj;
        return dataType == that.dataType &&
               Objects.equals(typeCat, that.typeCat) &&
               Objects.equals(typeSchem, that.typeSchem) &&
               Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                typeCat,
                typeSchem,
                typeName, dataType
        );
    }

    // --------------------------------------------------------------------------------------------------------- typeCat
    String getTypeCatNonNull() {
        return Optional.ofNullable(getTypeCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
        udtId = null;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem
    String getTypeSchemNonNull() {
        return Optional.ofNullable(getTypeSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    private String typeCat;

    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    private String typeSchem;

    @ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @_NullableByVendor("PostgreSQL")
    @ColumnLabel(COLUMN_LABEL_CLASS_NAME)
    private String className;

    @ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private int dataType;

    @_NullableByVendor("PostgreSQL")
    @ColumnLabel("REMARKS")
    private String remarks;

    @_NullableBySpecification
    @ColumnLabel("BASE_TYPE")
    private Integer baseType;

    // -----------------------------------------------------------------------------------------------------------------
    UDTId getUDTId() {
        if (udtId == null) {
            udtId = UDTId.of(
                    getTypeCatNonNull(),
                    getTypeSchemNonNull(),
                    getTypeName()
            );
        }
        return udtId;
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient UDTId udtId;
}
