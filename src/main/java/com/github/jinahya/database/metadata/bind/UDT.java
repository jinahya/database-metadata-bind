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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ParentOf(Attribute.class)
@ChildOf(Schema.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class UDT
        extends AbstractMetadataType {

    private static final long serialVersionUID = 8665246093405057553L;

    public static final Comparator<UDT> COMPARING_DATA_TYPE_TYPE_CAT_TYPE_SCHEM_TYPE_NAME =
            Comparator.comparingInt(UDT::getDataType)
                    .thenComparing(UDT::getTypeCat, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(UDT::getTypeSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(UDT::getTypeName);

    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    public static final String COLUMN_LABEL_CLASS_NAME = "CLASS_NAME";

    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    public List<Attribute> getAttributes(final Context context, final String attributeNamePattern)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return context.getAttributes(getTypeCat(), getTypeSchem(), getTypeName(), attributeNamePattern);
    }

    public TypeId getTypeId() {
        return TypeId.builder()
                .schemaId(SchemaId.of(getTypeCat(), getTypeSchem()))
                .name(getTypeName())
                .build();
    }

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    private String typeCat;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    private String typeSchem;

    @ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @NullableByVendor("PostgreSQL")
    @ColumnLabel(COLUMN_LABEL_CLASS_NAME)
    private String className;

    @ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private int dataType;

    @NullableByVendor("PostgreSQL")
    @ColumnLabel("REMARKS")
    private String remarks;

    @NullableBySpecification
    @ColumnLabel("BASE_TYPE")
    private Integer baseType;
}
