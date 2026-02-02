package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of the {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */

@_ChildOf(Table.class)
public class PseudoColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = -5612575879670895510L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    static Comparator<PseudoColumn> comparing(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(PseudoColumn::getTableCat, comparator)
                .thenComparing(PseudoColumn::getTableSchem, comparator)
                .thenComparing(PseudoColumn::getTableName, comparator)
                .thenComparing(PseudoColumn::getColumnName, comparator);
    }

    static Comparator<PseudoColumn> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return comparing(ContextUtils.nullOrdered(context, comparator));
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
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_SIZE = "COLUMN_SIZE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DECIMAL_DIGITS = "DECIMAL_DIGITS";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_NUM_PREC_RADIX = "NUM_PREC_RADIX";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_USAGE = "COLUMN_USAGE";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CHARACTER_OCTET_LENGTH = "CHAR_OCTET_LENGTH";

    // ----------------------------------------------------------------------------------------------------- IS_NULLABLE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_IS_NULLABLE = "IS_NULLABLE";

    public static final String COLUMN_VALUE_COLUMN_IS_NULLABLE_YES = MetadataTypeConstants.YES;

    public static final String COLUMN_VALUE_COLUMN_IS_NULLABLE_NO = MetadataTypeConstants.NO;

    public static final String COLUMN_VALUE_COLUMN_IS_NULLABLE_EMPTY = MetadataTypeConstants.EMPTY;

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    @SuppressWarnings({
            "java:S2637" // "@NonNull" values should not be set to null
    })
    PseudoColumn() {
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
               ",dataType=" + dataType +
               ",columnSize=" + columnSize +
               ",decimalDigits=" + decimalDigits +
               ",numPrecRadix=" + numPrecRadix +
               ",columnUsage=" + columnUsage +
               ",remarks=" + remarks +
               ",charOctetLength=" + charOctetLength +
               ",isNullable=" + isNullable +
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
        final var that = (PseudoColumn) obj;
        return Objects.equals(tableCat, that.tableCat) &&
               Objects.equals(tableSchem, that.tableSchem) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableCat, tableSchem, tableName, columnName);
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

    // -------------------------------------------------------------------------------------------------------- dataType

    /**
     * Returns the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @param dataType the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // ------------------------------------------------------------------------------------------------------ columnSize

    /**
     * Returns the value of {@value #COLUMN_LABEL_COLUMN_SIZE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_COLUMN_SIZE} column.
     */
    @Nullable
    public Integer getColumnSize() {
        return columnSize;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_COLUMN_SIZE} column.
     *
     * @param columnSize the value of {@value #COLUMN_LABEL_COLUMN_SIZE} column.
     */
    void setColumnSize(final Integer columnSize) {
        this.columnSize = columnSize;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits

    /**
     * Returns the value of {@value #COLUMN_LABEL_DECIMAL_DIGITS} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DECIMAL_DIGITS} column.
     */
    @Nullable
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DECIMAL_DIGITS} column.
     *
     * @param decimalDigits the value of {@value #COLUMN_LABEL_DECIMAL_DIGITS} column.
     */
    void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- numPrecRadix

    /**
     * Returns the value of {@value #COLUMN_LABEL_NUM_PREC_RADIX} column.
     *
     * @return the value of {@value #COLUMN_LABEL_NUM_PREC_RADIX} column.
     */
    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_NUM_PREC_RADIX} column.
     *
     * @param numPrecRadix the value of {@value #COLUMN_LABEL_NUM_PREC_RADIX} column.
     */
    void setNumPrecRadix(final Integer numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    // ----------------------------------------------------------------------------------------------------- columnUsage

    /**
     * Returns the value of {@value #COLUMN_LABEL_USAGE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_USAGE} column.
     */
    public String getColumnUsage() {
        return columnUsage;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_USAGE} column.
     *
     * @param columnUsage the value of {@value #COLUMN_LABEL_USAGE} column.
     */
    void setColumnUsage(final String columnUsage) {
        this.columnUsage = columnUsage;
    }

    // --------------------------------------------------------------------------------------------------------- remarks

    /**
     * Returns the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @return the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    @Nullable
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_REMARKS} column.
     *
     * @param remarks the value of {@value #COLUMN_LABEL_REMARKS} column.
     */
    void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // -------------------------------------------------------------------------------------------- characterOctetLength

    /**
     * Returns the value of {@value #COLUMN_LABEL_CHARACTER_OCTET_LENGTH} column.
     *
     * @return the value of {@value #COLUMN_LABEL_CHARACTER_OCTET_LENGTH} column.
     */
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_CHARACTER_OCTET_LENGTH} column.
     *
     * @param charOctetLength the value of {@value #COLUMN_LABEL_CHARACTER_OCTET_LENGTH} column.
     */
    void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable

    /**
     * Returns the value of {@value #COLUMN_LABEL_COLUMN_IS_NULLABLE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_COLUMN_IS_NULLABLE} column.
     */
    public String getIsNullable() {
        return isNullable;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_COLUMN_IS_NULLABLE} column.
     *
     * @param isNullable the value of {@value #COLUMN_LABEL_COLUMN_IS_NULLABLE} column.
     */
    void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
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
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_SIZE)
    private Integer columnSize;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_DECIMAL_DIGITS)
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_NUM_PREC_RADIX)
    private Integer numPrecRadix;

    @_ColumnLabel(COLUMN_LABEL_USAGE)
    private String columnUsage;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @_ColumnLabel(COLUMN_LABEL_CHARACTER_OCTET_LENGTH)
    private Integer charOctetLength;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_IS_NULLABLE)
    private String isNullable;
}
