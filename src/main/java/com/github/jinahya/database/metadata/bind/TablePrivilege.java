package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A class for binding results of the
 * {@link java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTablePrivileges(String, String, String)
 */
@_ChildOf(Catalog.class)
public class TablePrivilege
        extends AbstractMetadataType {

    private static final long serialVersionUID = -2142097373603478881L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    static Comparator<TablePrivilege> comparingInSpecifiedOrder(final UnaryOperator<String> operator,
                                                                final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(operator, "operator is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator
                .<TablePrivilege, String>comparing(v -> operator.apply(v.getTableCat()), comparator)
                .thenComparing(v -> operator.apply(v.getTableSchem()), comparator)
                .thenComparing(v -> operator.apply(v.getTableName()), comparator)
                .thenComparing(TablePrivilege::getPrivilege, comparator);
    }

    static Comparator<TablePrivilege> comparingInSpecifiedOrder(final Context context,
                                                                final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final var nullSafe = ContextUtils.nullOrdered(context, comparator);
        return Comparator
                .comparing(TablePrivilege::getTableCat, nullSafe)        // nullable
                .thenComparing(TablePrivilege::getTableSchem, nullSafe)  // nullable
                .thenComparing(TablePrivilege::getTableName, comparator) // NOT nullable
                .thenComparing(TablePrivilege::getPrivilege, comparator); // NOT nullable
    }

    static Comparator<TablePrivilege> comparingInSpecifiedOrder(final Context context)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_GRANTOR = "GRANTOR";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_GRANTEE = "GRANTEE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PRIVILEGE = "PRIVILEGE";

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

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    TablePrivilege() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
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
        final var that = (TablePrivilege) obj;
        return Objects.equals(tableCat, that.tableCat) &&
               Objects.equals(tableSchem, that.tableSchem) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(grantee, that.grantee) &&
               Objects.equals(privilege, that.privilege);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableCat, tableSchem, tableName, grantee, privilege);
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
