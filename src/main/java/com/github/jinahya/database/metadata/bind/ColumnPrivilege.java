package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumnPrivileges(String, String, String, String)
 */
@_ChildOf(Table.class)
public class ColumnPrivilege
        extends AbstractMetadataType {

    private static final long serialVersionUID = 4384654744147773380L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * They are ordered by <code>COLUMN_NAME</code> and <code>PRIVILEGE</code>.
     * </blockquote>
     *
     * @param operator   a unary operator for adjusting string values; applied only to non-{@code null} values.
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    static Comparator<ColumnPrivilege> comparingInSpecifiedOrder(final UnaryOperator<String> operator,
                                                                 final Comparator<? super String> comparator) {
        Objects.requireNonNull(operator, "operator is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final UnaryOperator<String> op = v -> v == null ? null : operator.apply(v);
        return Comparator
                .<ColumnPrivilege, String>comparing(v -> op.apply(v.getColumnName()), comparator)
                .thenComparing(v -> op.apply(v.getPrivilege()), comparator);
    }

    // ------------------------------------------------------------------------------------------------------- TABLE_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // ----------------------------------------------------------------------------------------------------- TABLE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // ------------------------------------------------------------------------------------------------------ TABLE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // --------------------------------------------------------------------------------------------------------- GRANTOR

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_GRANTOR = "GRANTOR";

    // --------------------------------------------------------------------------------------------------------- GRANTEE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_GRANTEE = "GRANTEE";

    // ------------------------------------------------------------------------------------------------------- PRIVILEGE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PRIVILEGE = "PRIVILEGE";

    // ----------------------------------------------------------------------------------------------------- IS_GRANTABLE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_IS_GRANTABLE = "IS_GRANTABLE";

    /**
     * A column value of {@value} for the {@value #COLUMN_LABEL_IS_GRANTABLE} column.
     */
    public static final String COLUMN_VALUE_IS_GRANTABLE_YES = MetadataTypeConstants.YES;

    /**
     * A column value of {@value} for the {@value #COLUMN_LABEL_IS_GRANTABLE} column.
     */
    public static final String COLUMN_VALUE_IS_GRANTABLE_NO = MetadataTypeConstants.NO;

    /**
     * A list of values for the {@value #COLUMN_LABEL_IS_GRANTABLE} column.
     */
    static final List<String> COLUMN_VALUES_IS_GRANTABLE = List.of(
            COLUMN_VALUE_IS_GRANTABLE_YES,
            COLUMN_VALUE_IS_GRANTABLE_NO
    );

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    ColumnPrivilege() {
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

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    @Nullable
    public String getTableCat() {
        return tableCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @param tableCat the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @param tableSchem the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     *
     * @param tableName the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     */
    void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName

    /**
     * Returns the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @param columnName the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // --------------------------------------------------------------------------------------------------------- grantor

    /**
     * Returns the value of {@value #COLUMN_LABEL_GRANTOR} column.
     *
     * @return the value of {@value #COLUMN_LABEL_GRANTOR} column.
     */
    @Nullable
    public String getGrantor() {
        return grantor;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_GRANTOR} column.
     *
     * @param grantor the value of {@value #COLUMN_LABEL_GRANTOR} column.
     */
    void setGrantor(final String grantor) {
        this.grantor = grantor;
    }

    // --------------------------------------------------------------------------------------------------------- grantee

    /**
     * Returns the value of {@value #COLUMN_LABEL_GRANTEE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_GRANTEE} column.
     */
    public String getGrantee() {
        return grantee;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_GRANTEE} column.
     *
     * @param grantee the value of {@value #COLUMN_LABEL_GRANTEE} column.
     */
    void setGrantee(final String grantee) {
        this.grantee = grantee;
    }

    // ------------------------------------------------------------------------------------------------------- privilege

    /**
     * Returns the value of {@value #COLUMN_LABEL_PRIVILEGE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PRIVILEGE} column.
     */
    public String getPrivilege() {
        return privilege;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PRIVILEGE} column.
     *
     * @param privilege the value of {@value #COLUMN_LABEL_PRIVILEGE} column.
     */
    void setPrivilege(final String privilege) {
        this.privilege = privilege;
    }

    // ----------------------------------------------------------------------------------------------------- isGrantable

    /**
     * Returns the value of {@value #COLUMN_LABEL_IS_GRANTABLE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_IS_GRANTABLE} column.
     */
    @Nullable
    public String getIsGrantable() {
        return isGrantable;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_IS_GRANTABLE} column.
     *
     * @param isGrantable the value of {@value #COLUMN_LABEL_IS_GRANTABLE} column.
     */
    void setIsGrantable(final String isGrantable) {
        this.isGrantable = isGrantable;
    }

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
    @_ColumnLabel(COLUMN_LABEL_GRANTOR)
    private String grantor;

    @_ColumnLabel(COLUMN_LABEL_GRANTEE)
    private String grantee;

    @_ColumnLabel(COLUMN_LABEL_PRIVILEGE)
    private String privilege;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_IS_GRANTABLE)
    private String isGrantable;
}
