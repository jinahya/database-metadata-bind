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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getUDTs(String, String, String, int[])
 */
@_ParentOf(Attribute.class)
@_ChildOf(Schema.class)
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class UDT extends AbstractMetadataType {

    private static final long serialVersionUID = 8665246093405057553L;

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

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CLASS_NAME = "CLASS_NAME";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "typeCat=" + typeCat +
               ",typeSchem=" + typeSchem +
               ",typeName=" + typeName +
               ",className=" + className +
               ",dataType=" + dataType +
               ",remarks=" + remarks +
               ",baseType=" + baseType +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UDT)) return false;
        final UDT that = (UDT) obj;
        return Objects.equals(dataType, that.dataType) &&
               Objects.equals(typeCatNonNull(), that.typeCatNonNull()) &&
               Objects.equals(typeSchemNonNull(), that.typeSchemNonNull()) &&
               Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                typeCatNonNull(),
                typeSchemNonNull(),
                typeName,
                dataType
        );
    }

    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    @_NotNull
    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(@_NotNull final Integer dataType) {
        this.dataType = dataType;
    }

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    private String typeCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    private String typeSchem;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @_NullableByVendor("PostgreSQL")
    @_ColumnLabel(COLUMN_LABEL_CLASS_NAME)
    private String className;

    @_NotNull
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_NullableByVendor("PostgreSQL")
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NullableBySpecification
    @_ColumnLabel("BASE_TYPE")
    private Integer baseType;

    String typeCatNonNull() {
        final String typeCat_ = getTypeCat();
        if (typeCat_ != null) {
            return typeCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    String typeSchemNonNull() {
        final String typeSchem_ = getTypeSchem();
        if (typeSchem_ != null) {
            return typeSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }
}
