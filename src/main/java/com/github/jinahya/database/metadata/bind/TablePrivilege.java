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

import jakarta.annotation.Nullable;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiPredicate;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * An class for binding results of the
 * {@link java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTablePrivileges(String, String, String)
 */
@XmlRootElement
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class TablePrivilege extends AbstractMetadataType {

    private static final long serialVersionUID = -2142097373603478881L;

    // -----------------------------------------------------------------------------------------------------------------
    static final Comparator<TablePrivilege> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(TablePrivilege::getTableCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(TablePrivilege::getTableSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(TablePrivilege::getTableName, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(TablePrivilege::getPrivilege, String.CASE_INSENSITIVE_ORDER);

    static final Comparator<TablePrivilege> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(TablePrivilege::getTableCat, nullsFirst(naturalOrder()))
                    .thenComparing(TablePrivilege::getTableSchem, nullsFirst(naturalOrder()))
                    .thenComparing(TablePrivilege::getTableName)
                    .thenComparing(TablePrivilege::getPrivilege);

    // -----------------------------------------------------------------------------------------------------------------

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

    // -----------------------------------------------------------------------------------------------------------------
    static final BiPredicate<TablePrivilege, Table> IS_OF = (p, t) -> {
        return Objects.equals(p.tableCat, t.getTableCat()) &&
               Objects.equals(p.tableSchem, t.getTableSchem()) &&
               Objects.equals(p.tableName, t.getTableName());
    };

    // -------------------------------------------------------------------------------------------------------- tableCat

    // ------------------------------------------------------------------------------------------------------ tableSchem

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    @EqualsAndHashCode.Include
    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    @EqualsAndHashCode.Include
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    @EqualsAndHashCode.Include
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("GRANTOR")
    private String grantor;

    @_ColumnLabel("GRANTEE")
    private String grantee;

    @_ColumnLabel("PRIVILEGE")
    @EqualsAndHashCode.Include
    private String privilege;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("IS_GRANTABLE")
    private String isGrantable;
}
