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
 * A class for binding results of the {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)}
 * method.
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

    public static final String COLUMN_LABEL_IS_GRANTABLE = "IS_GRANTABLE";

    public static final String COLUMN_VALUE_IS_GRANTABLE_YES = "YES";

    public static final String COLUMN_VALUE_IS_GRANTABLE_NO = "NO";

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

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @_NullableBySpecification
    @_ColumnLabel("GRANTOR")
    private String grantor;

    @_ColumnLabel("GRANTEE")
    private String grantee;

    @_ColumnLabel("PRIVILEGE")
    private String privilege;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_IS_GRANTABLE)
    private String isGrantable;

    String tableCatNonNull() {
        return Optional.ofNullable(getTableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String tableSchemNonNull() {
        return Optional.ofNullable(getTableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    Boolean getIsGrantableAsBoolean() {
        return Optional.ofNullable(getIsGrantable())
                .map(COLUMN_VALUE_IS_GRANTABLE_YES::equalsIgnoreCase)
                .orElse(null);
    }

    void setIsGrantableAsBoolean(final Boolean isGrantableAsBoolean) {
        setIsGrantable(
                Optional.ofNullable(isGrantableAsBoolean)
                        .map(v -> Boolean.TRUE.equals(v) ? COLUMN_VALUE_IS_GRANTABLE_YES : COLUMN_VALUE_IS_GRANTABLE_NO)
                        .orElse(null)
        );
    }
}
