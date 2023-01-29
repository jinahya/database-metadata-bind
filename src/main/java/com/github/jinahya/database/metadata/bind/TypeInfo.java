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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getTypeInfo() getTypeInfo()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTypeInfo()
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class TypeInfo
        extends AbstractMetadataType {

    private static final long serialVersionUID = -3964147654019495313L;

    public static final Comparator<TypeInfo> COMPARING_DATA_TYPE = Comparator.comparingInt(TypeInfo::getDataType);

    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public static final String COLUMN_LABEL_SEARCHABLE = "SEARCHABLE";

    @ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private int dataType;

    @NullableBySpecification // > Null is returned for data types where the column size is not applicable.
    @ColumnLabel("PRECISION")
    private Integer precision;

    @NullableBySpecification
    @ColumnLabel("LITERAL_PREFIX")
    private String literalPrefix;

    @NullableBySpecification
    @ColumnLabel("LITERAL_SUFFIX")
    private String literalSuffix;

    @NullableBySpecification
    @ColumnLabel("CREATE_PARAMS")
    private String createParams;

    @ColumnLabel(COLUMN_LABEL_NULLABLE)
    private int nullable;

    @ColumnLabel("CASE_SENSITIVE")
    private boolean caseSensitive;

    @ColumnLabel(COLUMN_LABEL_SEARCHABLE)
    private int searchable;

    @NotUsedBySpecification
    @ColumnLabel("UNSIGNED_ATTRIBUTE")
    private Boolean unsignedAttribute;

    @ColumnLabel("FIXED_PREC_SCALE")
    private boolean fixedPrecScale;

    @ColumnLabel("AUTO_INCREMENT")
    private boolean autoIncrement;

    @NullableBySpecification
    @ColumnLabel("LOCAL_TYPE_NAME")
    private String localTypeName;

    @ColumnLabel("MINIMUM_SCALE")
    private int minimumScale;

    @ColumnLabel("MAXIMUM_SCALE")
    private int maximumScale;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;
}
