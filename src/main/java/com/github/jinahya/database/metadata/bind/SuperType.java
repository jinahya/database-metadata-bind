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

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getSuperTypes(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTypes(String, String, String)
 */
@_ChildOf(Schema.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class SuperType extends AbstractMetadataType {

    private static final long serialVersionUID = 4603878785941565029L;

    // --------------------------------------------------------------------------------------------------------- typeCat
    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
        typeId = null;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem
    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
        typeId = null;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public void setTypeName(final String typeName) {
        this.typeName = typeName;
        typeId = null;
    }

    // ---------------------------------------------------------------------------------------------------- supertypeCat
    public void setSupertypeCat(final String supertypeCat) {
        this.supertypeCat = supertypeCat;
        supertypeId = null;
    }

    // -------------------------------------------------------------------------------------------------- supertypeSchem
    public void setSupertypeSchem(final String supertypeSchem) {
        this.supertypeSchem = supertypeSchem;
        supertypeId = null;
    }

    // --------------------------------------------------------------------------------------------------- supertypeName
    public void setSupertypeName(final String supertypeName) {
        this.supertypeName = supertypeName;
        supertypeId = null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @ColumnLabel("TYPE_CAT")
    private String typeCat;

    @_NullableBySpecification
    @ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @_NullableBySpecification
    @ColumnLabel("SUPERTYPE_CAT")
    private String supertypeCat;

    @_NullableBySpecification
    @ColumnLabel("SUPERTYPE_SCHEM")
    private String supertypeSchem;

    @ColumnLabel("SUPERTYPE_NAME")
    private String supertypeName;

    // -----------------------------------------------------------------------------------------------------------------
    UDTId getTypeId() {
        if (typeId == null) {
            typeId = UDTId.of(
                    typeCat == null ? Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY : typeCat,
                    typeSchem == null ? Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY : typeSchem,
                    typeName
            );
        }
        return typeId;
    }

    UDTId getSupertypeId() {
        if (supertypeId == null) {
            supertypeId = UDTId.of(
                    supertypeCat == null ? Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY : supertypeCat,
                    supertypeSchem == null ? Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY : supertypeSchem,
                    supertypeName
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
