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
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTablePrivileges(String, String, String)
 */
@ChildOf(Catalog.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class TablePrivilege extends AbstractMetadataType {

    private static final long serialVersionUID = -1799954363648972203L;

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final Comparator<TablePrivilege> COMPARING_TABLE_CAT_TABLE_SCHEM_TABLE_NAME_PRIVILEGE =
            Comparator.comparing(TablePrivilege::getTableCat, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(TablePrivilege::getTableSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(TablePrivilege::getTableName)
                    .thenComparing(TablePrivilege::getPrivilege);

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @NullableBySpecification
    @ColumnLabel("GRANTOR")
    private String grantor;

    @ColumnLabel("GRANTEE")
    private String grantee;

    @ColumnLabel("PRIVILEGE")
    private String privilege;

    @NullableBySpecification
    @ColumnLabel("IS_GRANTABLE")
    private String isGrantable;
}
