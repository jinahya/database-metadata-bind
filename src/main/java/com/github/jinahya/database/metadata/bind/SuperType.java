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
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getSuperTypes(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTypes(String, String, String)
 */
@_ChildOf(Schema.class)
@Setter
@Getter
public class SuperType extends AbstractMetadataType {

    private static final long serialVersionUID = 4603878785941565029L;

    @Override
    public String toString() {
        return "SuperType{" +
               "typeCat=" + typeCat +
               ",typeSchem=" + typeSchem +
               ",typeName=" + typeName +
               ",supertypeCat=" + supertypeCat +
               ",supertypeSchem=" + supertypeSchem +
               ",supertypeName=" + supertypeName +
               '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SuperType)) return false;
        final SuperType that = (SuperType) obj;
        return Objects.equals(typeCat, that.typeCat) &&
               Objects.equals(typeSchem, that.typeSchem) &&
               Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCat, typeSchem, typeName);
    }

    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
        typeId = null;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
        typeId = null;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
        typeId = null;
    }

    public String getSupertypeCat() {
        return supertypeCat;
    }

    public void setSupertypeCat(final String supertypeCat) {
        this.supertypeCat = supertypeCat;
        supertypeId = null;
    }

    public String getSupertypeSchem() {
        return supertypeSchem;
    }

    public void setSupertypeSchem(final String supertypeSchem) {
        this.supertypeSchem = supertypeSchem;
        supertypeId = null;
    }

    public String getSupertypeName() {
        return supertypeName;
    }

    public void setSupertypeName(final String supertypeName) {
        this.supertypeName = supertypeName;
        supertypeId = null;
    }

    @_NullableBySpecification
    @_ColumnLabel("TYPE_CAT")
    private String typeCat;

    @_NullableBySpecification
    @_ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_NullableBySpecification
    @_ColumnLabel("SUPERTYPE_CAT")
    private String supertypeCat;

    @_NullableBySpecification
    @_ColumnLabel("SUPERTYPE_SCHEM")
    private String supertypeSchem;

    @_ColumnLabel("SUPERTYPE_NAME")
    private String supertypeName;

    String getTypeCatNonNull() {
        return Optional.ofNullable(getTypeCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getTypeSchemNonNull() {
        return Optional.ofNullable(getTypeSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    String getSupertypeCatNonNull() {
        return Optional.ofNullable(getSupertypeCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getSupertypeSchemNonNull() {
        return Optional.ofNullable(getSupertypeSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    UDTId getTypeId() {
        if (typeId == null) {
            typeId = UDTId.of(
                    getTypeCatNonNull(),
                    getTypeSchemNonNull(),
                    getTypeName()
            );
        }
        return typeId;
    }

    UDTId getSupertypeId() {
        if (supertypeId == null) {
            supertypeId = UDTId.of(
                    getSupertypeCatNonNull(),
                    getSupertypeSchemNonNull(),
                    getSupertypeName()
            );
        }
        return supertypeId;
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient UDTId typeId;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient UDTId supertypeId;
}
