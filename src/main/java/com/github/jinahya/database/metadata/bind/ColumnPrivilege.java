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
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

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
public class ColumnPrivilege
        extends AbstractMetadataType {

    private static final long serialVersionUID = 4384654744147773380L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<ColumnPrivilege> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(ColumnPrivilege::getColumnName, comparator)
                .thenComparing(ColumnPrivilege::getPrivilege, comparator);
    }

    static Comparator<ColumnPrivilege> comparingComparingInSpecifiedOrder(final Context context,
                                                                          final Comparator<? super String> comparator)
            throws SQLException {
        return comparingInSpecifiedOrder(
                ContextUtils.nullPrecedence(context, comparator)
        );
    }

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // ------------------------------------------------------------------------------------------------------- TABLE_NAM

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_IS_GRANTABLE = "IS_GRANTABLE";

    public static final String COLUMN_VALUE_IS_GRANTABLE_YES = MetadataTypeConstants.YES;

    public static final String COLUMN_VALUE_IS_GRANTABLE_NO = MetadataTypeConstants.NO;

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    public ColumnPrivilege() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

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

    // -------------------------------------------------------------------------------------------------------- tableCat

    // ------------------------------------------------------------------------------------------------------ tableSchem

    // ------------------------------------------------------------------------------------------------------- tableName

    // ------------------------------------------------------------------------------------------------------ columnName

    // ----------------------------------------------------------------------------------------------------- isGrantable

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("GRANTOR")
    private String grantor;

    @_ColumnLabel("GRANTEE")
    private String grantee;

    @_ColumnLabel("PRIVILEGE")
    private String privilege;

    @Nullable
    @Pattern(regexp = MetadataTypeConstants.PATTERN_REGEXP_YES_OR_NO)
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_IS_GRANTABLE)
    private String isGrantable;
}
