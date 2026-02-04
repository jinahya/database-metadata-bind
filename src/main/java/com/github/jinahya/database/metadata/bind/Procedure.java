package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getFunctionColumns(String, String, String, String)
 * @see ProcedureColumn
 */
public class Procedure
        extends AbstractMetadataType {

    private static final long serialVersionUID = -6262056388403934829L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * They are ordered by <code>PROCEDURE_CAT</code>, <code>PROCEDURE_SCHEM</code>, <code>PROCEDURE_NAME</code> and
     * <code>SPECIFIC_NAME</code>.
     * </blockquote>
     *
     * @param operator   a null-safe unary operator for adjusting string values.
     * @param comparator a null-safe string comparator for comparing values.
     * @return a comparator comparing values in the specified order.
     * @see ContextUtils#nullOrdered(Context, Comparator)
     */
    static Comparator<Procedure> comparingInSpecifiedOrder(final UnaryOperator<String> operator,
                                                           final Comparator<? super String> comparator) {
        Objects.requireNonNull(operator, "operator is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator
                .<Procedure, String>comparing(v -> operator.apply(v.getProcedureCat()), comparator)
                .thenComparing(v -> operator.apply(v.getProcedureSchem()), comparator)
                .thenComparing(v -> operator.apply(v.getProcedureName()), comparator)
                .thenComparing(v -> operator.apply(v.getSpecificName()), comparator);
    }

    // They are ordered by PROCEDURE_CAT, PROCEDURE_SCHEM, PROCEDURE_NAME and SPECIFIC_NAME.
    static Comparator<Procedure> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(Procedure::getProcedureCat, comparator)
                .thenComparing(Procedure::getProcedureSchem, comparator)
                .thenComparing(Procedure::getProcedureName, comparator)
                .thenComparing(Procedure::getSpecificName, comparator);
    }

    static Comparator<Procedure> comparingInSpecifiedOrder(final Context context,
                                                           final Comparator<? super String> comparator)
            throws SQLException {
        return comparingInSpecifiedOrder(ContextUtils.nullOrdered(context, comparator));
    }

    // -------------------------------------------------------------------------------------------------- PROCEDURE_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_TYPE = "PROCEDURE_TYPE";

    /**
     * A column value of
     * {@link DatabaseMetaData#procedureResultUnknown}({@value DatabaseMetaData#procedureResultUnknown}) for the
     * {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     */
    public static final int COLUMN_VALUE_PROCEDURE_TYPE_PROCEDURE_RESULT_UNKNOWN =
            DatabaseMetaData.procedureResultUnknown;

    /**
     * A column value of {@link DatabaseMetaData#procedureNoResult}({@value DatabaseMetaData#procedureNoResult}) for the
     * {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     */
    public static final int COLUMN_VALUE_PROCEDURE_TYPE_PROCEDURE_NO_RESULT = DatabaseMetaData.procedureNoResult;

    /**
     * A column value of
     * {@link DatabaseMetaData#procedureReturnsResult}({@value DatabaseMetaData#procedureReturnsResult}) for the
     * {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     */
    public static final int COLUMN_VALUE_PROCEDURE_TYPE_PROCEDURE_RETURNS_RESULT =
            DatabaseMetaData.procedureReturnsResult;

    static final List<Integer> COLUMN_VALUES_PROCEDURE_TYPE = List.of(
            COLUMN_VALUE_PROCEDURE_TYPE_PROCEDURE_RESULT_UNKNOWN, // 0
            COLUMN_VALUE_PROCEDURE_TYPE_PROCEDURE_NO_RESULT,      // 1
            COLUMN_VALUE_PROCEDURE_TYPE_PROCEDURE_RETURNS_RESULT  // 2
    );

    // --------------------------------------------------------------------------------------------------- SPECIFIC_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SPECIFIC_NAME = "SPECIFIC_NAME";

    // --------------------------------------------------------------------------------------------------- PROCEDURE_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_CAT = "PROCEDURE_CAT";

    // ------------------------------------------------------------------------------------------------- PROCEDURE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_SCHEM = "PROCEDURE_SCHEM";

    // -------------------------------------------------------------------------------------------------- PROCEDURE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PROCEDURE_NAME = "PROCEDURE_NAME";

    // --------------------------------------------------------------------------------------------------------- REMARKS

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    Procedure() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "procedureCat=" + procedureCat +
               ",procedureSchem=" + procedureSchem +
               ",procedureName=" + procedureName +
               ",remarks=" + remarks +
               ",procedureType=" + procedureType +
               ",specificName=" + specificName +
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
        final var that = (Procedure) obj;
        return Objects.equals(procedureCat, that.procedureCat) &&
               Objects.equals(procedureSchem, that.procedureSchem) &&
               Objects.equals(procedureName, that.procedureName) &&
               Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), procedureCat, procedureSchem, procedureName, specificName);
    }

    // ---------------------------------------------------------------------------------------------------- procedureCat

    /**
     * Returns the value of {@code PROCEDURE_CAT} column.
     *
     * @return the value of {@code PROCEDURE_CAT} column.
     */
    @Nullable
    public String getProcedureCat() {
        return procedureCat;
    }

    /**
     * Sets the value of {@code PROCEDURE_CAT} column.
     *
     * @param procedureCat the value of {@code PROCEDURE_CAT} column.
     */
    void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    // -------------------------------------------------------------------------------------------------- procedureSchem

    /**
     * Returns the value of {@code PROCEDURE_SCHEM} column.
     *
     * @return the value of {@code PROCEDURE_SCHEM} column.
     */
    @Nullable
    public String getProcedureSchem() {
        return procedureSchem;
    }

    /**
     * Sets the value of {@code PROCEDURE_SCHEM} column.
     *
     * @param procedureSchem the value of {@code PROCEDURE_SCHEM} column.
     */
    void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    // --------------------------------------------------------------------------------------------------- procedureName

    /**
     * Returns the value of {@code PROCEDURE_NAME} column.
     *
     * @return the value of {@code PROCEDURE_NAME} column.
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * Sets the value of {@code PROCEDURE_NAME} column.
     *
     * @param procedureName the value of {@code PROCEDURE_NAME} column.
     */
    void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    // ---------------------------------------------------------------------------------------------------------- remark

    /**
     * Returns the value of {@code REMARKS} column.
     *
     * @return the value of {@code REMARKS} column.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@code REMARKS} column.
     *
     * @param remarks the value of {@code REMARKS} column.
     */
    void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------------------------------------------------- procedureType

    /**
     * Returns the value of {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     */
    public Integer getProcedureType() {
        return procedureType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     *
     * @param procedureType the value of {@value #COLUMN_LABEL_PROCEDURE_TYPE} column.
     */
    void setProcedureType(final Integer procedureType) {
        this.procedureType = procedureType;
    }

    // ---------------------------------------------------------------------------------------------------- specificName

    /**
     * Returns the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     */
    public String getSpecificName() {
        return specificName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     *
     * @param specificName the value of {@value #COLUMN_LABEL_SPECIFIC_NAME} column.
     */
    void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_CAT)
    private String procedureCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_SCHEM)
    private String procedureSchem;

    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_NAME)
    private String procedureName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @_ColumnLabel(COLUMN_LABEL_PROCEDURE_TYPE)
    private Integer procedureType;

    // https://github.com/microsoft/mssql-jdbc/issues/2320
    @_ColumnLabel(COLUMN_LABEL_SPECIFIC_NAME)
    private String specificName;
}
