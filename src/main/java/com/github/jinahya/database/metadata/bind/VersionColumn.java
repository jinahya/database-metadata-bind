package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding results of the {@link DatabaseMetaData#getVersionColumns(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getVersionColumns(String, String, String)
 */
@_ChildOf(Table.class)
public class VersionColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = 3587959398829593292L;

    // ----------------------------------------------------------------------------------------------------------- SCOPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE = "SCOPE";

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

    // --------------------------------------------------------------------------------------------------- PSEUDO_COLUMN

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PSEUDO_COLUMN = "PSEUDO_COLUMN";

    /**
     * A column value of {@link DatabaseMetaData#versionColumnUnknown}({@value}) for the
     * {@value #COLUMN_LABEL_PSEUDO_COLUMN} column.
     */
    public static final int COLUMN_VALUE_PSEUDO_COLUMN_VERSION_COLUMN_UNKNOWN =
            DatabaseMetaData.versionColumnUnknown;   // 0

    public static final int COLUMN_VALUE_PSEUDO_COLUMN_VERSION_COLUMN_NOT_PSEUDO =
            DatabaseMetaData.versionColumnNotPseudo; // 1

    public static final int COLUMN_VALUE_PSEUDO_COLUMN_VERSION_COLUMN_PSEUDO =
            DatabaseMetaData.versionColumnPseudo;    // 2

    static final List<Integer> COLUMN_VALUES_PSEUDO_COLUMN = List.of(
            COLUMN_VALUE_PSEUDO_COLUMN_VERSION_COLUMN_UNKNOWN,    // 0
            COLUMN_VALUE_PSEUDO_COLUMN_VERSION_COLUMN_NOT_PSEUDO, // 1
            COLUMN_VALUE_PSEUDO_COLUMN_VERSION_COLUMN_PSEUDO      // 2
    );

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    VersionColumn() {
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
        final var that = (VersionColumn) obj;
        return Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), columnName);
    }

    // ----------------------------------------------------------------------------------------------------------- scope

    /**
     * Returns the value of {@value COLUMN_LABEL_SCOPE} column.
     *
     * @return the value of {@value COLUMN_LABEL_SCOPE} column.
     */
    @Nullable
    public Integer getScope() {
        return scope;
    }

    /**
     * Sets the value of {@value COLUMN_LABEL_SCOPE} column.
     *
     * @param scope the value of {@value COLUMN_LABEL_SCOPE} column.
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
     * Returns the value of {@code DATA_TYPE} column.
     *
     * @return the value of {@code DATA_TYPE} column.
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * Sets the value of {@code DATA_TYPE} column.
     *
     * @param dataType the value of {@code DATA_TYPE} column.
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
    @Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SCOPE)
    private Integer scope;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_SIZE)
    private Integer columnSize;

    @_ColumnLabel(COLUMN_LABEL_BUFFER_LENGTH)
    private Integer bufferLength;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_DECIMAL_DIGITS)
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_PSEUDO_COLUMN)
    private Integer pseudoColumn;
}
