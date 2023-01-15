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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;

/**
 * A class for binding results of {@link DatabaseMetaData#getAttributes(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ChildOf_(UDT.class)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Attribute
        implements MetadataType {

    private static final long serialVersionUID = 1913681105410440186L;

    public static final String VALUE_IS_NULLABLE_YES = "YES";

    public static final String VALUE_IS_NULLABLE_NO = "NO";

    public static final String VALUE_IS_NULLABLE_EMPTY = "";

    @NullableBySpecification
    @ColumnLabel("TYPE_CAT")
    private String typeCat;

    @NullableBySpecification
    @ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @ColumnLabel("ATTR_NAME")
    private String attrName;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("ATTR_TYPE_NAME")
    private String attrTypeName;

    @ColumnLabel("ATTR_SIZE")
    private int attrSize;

    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;

    @ColumnLabel("NULLABLE")
    private int nullable;

    @NullableBySpecification
    @ColumnLabel("REMARKS")
    private String remarks;

    @NullableBySpecification
    @ColumnLabel("ATTR_DEF")
    private String attrDef;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @ColumnLabel("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @NullableBySpecification
    @ColumnLabel("SCOPE_CATALOG")
    private String scopeCatalog;

    @NullableBySpecification
    @ColumnLabel("SCOPE_SCHEMA")
    private String scopeSchema;

    @NullableBySpecification
    @ColumnLabel("SCOPE_TABLE")
    private String scopeTable;

    @NullableBySpecification
    @ColumnLabel("SOURCE_DATA_TYPE")
    private Integer sourceDataType;
}
