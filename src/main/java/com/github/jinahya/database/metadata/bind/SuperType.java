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
import java.util.Optional;

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

    UDTId getTypeId() {
        return UDTId.of(getTypeCatNonNull(), getTypeSchemNonNull(), getTypeName());
    }

    UDTId getSupertypeId() {
        return UDTId.of(getSupertypeCatNonNull(), getSupertypeSchemNonNull(), getSupertypeName());
    }

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
}
