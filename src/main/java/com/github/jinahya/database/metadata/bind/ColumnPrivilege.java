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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

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

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<ColumnPrivilege> comparingInSpecifiedOrder(final Context context,
                                                                 final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator
                .comparing(ColumnPrivilege::getColumnName, comparator)  // NOT nullable
                .thenComparing(ColumnPrivilege::getPrivilege, comparator); // NOT nullable
    }

    static Comparator<ColumnPrivilege> comparingInSpecifiedOrder(final Context context)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
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

    // ------------------------------------------------------------------------------------------------------- TABLE_NAM

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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_IS_GRANTABLE = "IS_GRANTABLE";

    public static final String COLUMN_VALUE_IS_GRANTABLE_YES = MetadataTypeConstants.YES;

    public static final String COLUMN_VALUE_IS_GRANTABLE_NO = MetadataTypeConstants.NO;

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
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (ColumnPrivilege) obj;
        return Objects.equals(tableCat, that.tableCat) &&
               Objects.equals(tableSchem, that.tableSchem) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(columnName, that.columnName) &&
               Objects.equals(grantee, that.grantee) &&
               Objects.equals(privilege, that.privilege);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableCat, tableSchem, tableName, columnName, grantee, privilege);
    }

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

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
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

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_GRANTOR)
    private String grantor;

    @_ColumnLabel(COLUMN_LABEL_GRANTEE)
    private String grantee;

    @_ColumnLabel(COLUMN_LABEL_PRIVILEGE)
    private String privilege;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_IS_GRANTABLE)
    private String isGrantable;
}
