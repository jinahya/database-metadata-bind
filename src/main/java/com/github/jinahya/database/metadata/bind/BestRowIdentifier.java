package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getBestRowIdentifier(String, String, String, int, boolean)
 * @see PseudoColumn
 * @see <a
 * href="https://docs.oracle.com/en/java/javase/25/docs/api/java.sql/java/sql/DatabaseMetaData.html#getBestRowIdentifier(java.lang.String,java.lang.String,java.lang.String,int,boolean)">DatabaseMetaData#getBestRowIdentifier(catalog,
 * schema, table, scope, nullable)</a>
 */
@_ChildOf(Table.class)
public class BestRowIdentifier
        extends AbstractMetadataType {

    private static final long serialVersionUID = -1512051574198028399L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * .
     * <bloackquote>
     * They are ordered by <code>SCOPE</code>.
     * </bloackquote>
     *
     * @return .
     */
    static Comparator<BestRowIdentifier> comparingInSpecifiedOrder() {
        return Comparator.comparing(BestRowIdentifier::getScope, Comparator.naturalOrder());
    }

    // ----------------------------------------------------------------------------------------------------------- SCOPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE = "SCOPE";

    /**
     * A column value of {@link DatabaseMetaData#bestRowTemporary}({@value DatabaseMetaData#bestRowTemporary}) for the
     * {@value #COLUMN_LABEL_SCOPE} column.
     */
    public static final int COLUMN_VALUE_SCOPE_BEST_ROW_TEMPORARY = DatabaseMetaData.bestRowTemporary;     // 0

    /**
     * A column value of {@link DatabaseMetaData#bestRowTransaction}({@value DatabaseMetaData#bestRowTransaction}) for
     * the {@value #COLUMN_LABEL_SCOPE} column.
     */
    public static final int COLUMN_VALUE_SCOPE_BEST_ROW_TRANSACTION = DatabaseMetaData.bestRowTransaction; // 1

    /**
     * A column value of {@link DatabaseMetaData#bestRowSession}({@value DatabaseMetaData#bestRowSession}) for the
     * {@value #COLUMN_LABEL_SCOPE} column.
     */
    public static final int COLUMN_VALUE_SCOPE_BEST_ROW_SESSION = DatabaseMetaData.bestRowSession;         // 2

    /**
     * A list of values for the {@value #COLUMN_LABEL_SCOPE} column.
     */
    static final List<Integer> COLUMN_VALUES_SCOPE = List.of(
            COLUMN_VALUE_SCOPE_BEST_ROW_TEMPORARY,   // 0
            COLUMN_VALUE_SCOPE_BEST_ROW_TRANSACTION, // 1
            COLUMN_VALUE_SCOPE_BEST_ROW_SESSION      // 2
    );

    // ----------------------------------------------------------------------------------------------------- COLUMN_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // ------------------------------------------------------------------------------------------------------- DATA_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // --------------------------------------------------------------------------------------------------- PSEUDO_COLUMN

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PSEUDO_COLUMN = "PSEUDO_COLUMN";

    // ------------------------------------------------------------------------------------------------------- TYPE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // ----------------------------------------------------------------------------------------------------- COLUMN_SIZE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_SIZE = "COLUMN_SIZE";

    // --------------------------------------------------------------------------------------------------- BUFFER_LENGTH

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_BUFFER_LENGTH = "BUFFER_LENGTH";

    // -------------------------------------------------------------------------------------------------- DECIMAL_DIGITS

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DECIMAL_DIGITS = "DECIMAL_DIGITS";

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    BestRowIdentifier() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "scope=" + scope +
               ",columnName=" + columnName +
               ",dataType=" + dataType +
               ",typeName=" + typeName +
               ",columnSize=" + columnSize +
               ",bufferLength=" + bufferLength +
               ",decimalDigits=" + decimalDigits +
               ",pseudoColumn=" + pseudoColumn +
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
        final var that = (BestRowIdentifier) obj;
        return Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), columnName);
    }

    // ----------------------------------------------------------------------------------------------------------- scope

    /**
     * Returns the value of {@value #COLUMN_LABEL_SCOPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SCOPE} column.
     */
    public Integer getScope() {
        return scope;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SCOPE} column.
     *
     * @param scope the value of {@value #COLUMN_LABEL_SCOPE} column.
     */
    void setScope(final Integer scope) {
        this.scope = scope;
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

    // -------------------------------------------------------------------------------------------------------- typeName

    /**
     * Returns the value of {@code TYPE_NAME} column.
     *
     * @return the value of {@code TYPE_NAME} column.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the value of {@code TYPE_NAME} column.
     *
     * @param typeName the value of {@code TYPE_NAME} column.
     */
    void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------ columnSize

    /**
     * Returns the value of {@code COLUMN_SIZE} column.
     *
     * @return the value of {@code COLUMN_SIZE} column.
     */
    @Nullable
    public Integer getColumnSize() {
        return columnSize;
    }

    /**
     * Sets the value of {@code COLUMN_SIZE} column.
     *
     * @param columnSize the value of {@code COLUMN_SIZE} column.
     */
    void setColumnSize(final Integer columnSize) {
        this.columnSize = columnSize;
    }

    // ---------------------------------------------------------------------------------------------------- bufferLength

    /**
     * Returns the value of {@code BUFFER_LENGTH} column.
     *
     * @return the value of {@code BUFFER_LENGTH} column.
     */
    @Nullable
    public Integer getBufferLength() {
        return bufferLength;
    }

    /**
     * Sets the value of {@code BUFFER_LENGTH} column.
     *
     * @param bufferLength the value of {@code BUFFER_LENGTH} column.
     */
    void setBufferLength(final Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits

    /**
     * Returns the value of {@code DECIMAL_DIGITS} column.
     *
     * @return the value of {@code DECIMAL_DIGITS} column.
     */
    @Nullable
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    /**
     * Sets the value of {@code DECIMAL_DIGITS} column.
     *
     * @param decimalDigits the value of {@code DECIMAL_DIGITS} column.
     */
    void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- pseudoColumn

    /**
     * Returns the value of {@value #COLUMN_LABEL_PSEUDO_COLUMN} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PSEUDO_COLUMN} column.
     */
    public Integer getPseudoColumn() {
        return pseudoColumn;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PSEUDO_COLUMN} column.
     *
     * @param pseudoColumn the value of {@value #COLUMN_LABEL_PSEUDO_COLUMN} column.
     */
    void setPseudoColumn(final Integer pseudoColumn) {
        this.pseudoColumn = pseudoColumn;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_SCOPE)
    private Integer scope;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_SIZE)
    private Integer columnSize;

    @Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_BUFFER_LENGTH)
    private Integer bufferLength;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_DECIMAL_DIGITS)
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private Integer pseudoColumn;
}
