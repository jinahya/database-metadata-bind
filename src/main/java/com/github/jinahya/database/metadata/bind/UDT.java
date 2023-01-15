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
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A class for binding results of {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ChildOf_(Schema.class)
@ParentOf(Attribute.class)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class UDT
        implements MetadataType {

    private static final long serialVersionUID = 8665246093405057553L;

    public void getAttributes(final Context context, final String attributeNamePattern,
                              final Consumer<? super Attribute> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer, "consumer is null");
        Objects.requireNonNull(consumer, "consumer is null");
        context.getAttributes(getTypeCat(), getTypeSchem(), getTypeName(), attributeNamePattern, consumer);
    }

    @NullableBySpecification
    @ColumnLabel("TYPE_CAT")
    private String typeCat;

    @NullableBySpecification
    @ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @ColumnLabel("CLASS_NAME")
    private String className;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("REMARKS")
    private String remarks;

    @NullableBySpecification
    @ColumnLabel("BASE_TYPE")
    private Integer baseType;
}
