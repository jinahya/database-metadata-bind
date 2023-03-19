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
import lombok.ToString;

import java.util.Comparator;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * An class for binding results of the
 * {@link java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTablePrivileges(String, String, String)
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class TablePrivilege extends AbstractMetadataType {

    private static final long serialVersionUID = -2142097373603478881L;

    static final Comparator<TablePrivilege> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(TablePrivilege::getTableCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(TablePrivilege::getTableSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(TablePrivilege::getTableName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(TablePrivilege::getPrivilege, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<TablePrivilege> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(TablePrivilege::getTableCat, nullsFirst(naturalOrder()))
                    .thenComparing(TablePrivilege::getTableSchem, nullsFirst(naturalOrder()))
                    .thenComparing(TablePrivilege::getTableName, nullsFirst(naturalOrder()))
                    .thenComparing(TablePrivilege::getPrivilege, nullsFirst(naturalOrder()));

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
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

    @_NullableBySpecification
    @_ColumnLabel("GRANTOR")
    private String grantor;

    @_ColumnLabel("GRANTEE")
    private String grantee;

    @_ColumnLabel("PRIVILEGE")
    private String privilege;

    @_NullableBySpecification
    @_ColumnLabel("IS_GRANTABLE")
    private String isGrantable;

    String tableCatNonNull() {
        final String tableCat_ = getTableCat();
        if (tableCat_ != null) {
            return tableCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    String tableSchemNonNull() {
        final String tableSchem_ = getTableSchem();
        if (tableSchem_ != null) {
            return tableSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }
}
