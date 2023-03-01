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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumnPrivileges(String, String, String, String)
 */
@_ChildOf(Table.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class ColumnPrivilege extends AbstractMetadataType {

    private static final long serialVersionUID = 4384654744147773380L;

    static final Comparator<ColumnPrivilege> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(ColumnPrivilege::getColumnName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(ColumnPrivilege::getPrivilege, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<ColumnPrivilege> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(ColumnPrivilege::getColumnName, nullsFirst(naturalOrder()))
                    .thenComparing(ColumnPrivilege::getPrivilege, nullsFirst(naturalOrder()));

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",columnName=" + columnName +
               ",grantor=" + grantor +
               ",grantee=" + grantee +
               ",privilege=" + privilege +
               ",isGrantable=" + isGrantable +
               '}';
    }

    Boolean getIsGrantableAsBoolean() {
        return Optional.ofNullable(getIsGrantable())
                .map("YES"::equals)
                .orElse(null);
    }

    void setIsGrantableAsBoolean(final Boolean isGrantableAsBoolean) {
        setIsGrantable(
                Optional.ofNullable(isGrantableAsBoolean)
                        .map(v -> Boolean.TRUE.equals(v) ? "YES" : "NO")
                        .orElse(null)
        );
    }

    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @_NullableBySpecification
    @ColumnLabel("GRANTOR")
    private String grantor;

    @ColumnLabel("GRANTEE")
    private String grantee;

    @ColumnLabel("PRIVILEGE")
    private String privilege;

    @_NullableBySpecification
    @ColumnLabel("IS_GRANTABLE")
    private String isGrantable;

    String getTableCatNonNull() {
        return Optional.ofNullable(getTableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getTableSchemNonNull() {
        return Optional.ofNullable(getTableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    ColumnPrivilegeId getColumnPrivilegeId() {
        return ColumnPrivilegeId.of(
                ColumnId.of(
                        TableId.of(
                                getTableCatNonNull(),
                                getTableSchemNonNull(),
                                getTableName()
                        ),
                        getColumnName()
                ),
                getPrivilege()
        );
    }
}
