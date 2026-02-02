package com.github.jinahya.database.metadata.bind;

import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */

public class CrossReference
        extends AbstractMetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS
    static Comparator<CrossReference> comparingInSpecifiedOrder(final Context context,
                                                                final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final var nullSafe = ContextUtils.nullPrecedence(context, comparator);
        return Comparator
                .comparing(CrossReference::getFktableCat, nullSafe)        // nullable
                .thenComparing(CrossReference::getFktableSchem, nullSafe)  // nullable
                .thenComparing(CrossReference::getFktableName, comparator) // NOT nullable
                .thenComparing(CrossReference::getKeySeq, Comparator.naturalOrder()); // NOT nullable
    }

    static Comparator<CrossReference> comparingInSpecifiedOrder(final Context context)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        return comparingInSpecifiedOrder(context, String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PKTABLE_CAT = "PKTABLE_CAT";

    // --------------------------------------------------------------------------------------------------- PKTABLE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PKTABLE_SCHEM = "PKTABLE_SCHEM";

    // ---------------------------------------------------------------------------------------------------- PKTABLE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PKTABLE_NAME = "PKTABLE_NAME";

    // --------------------------------------------------------------------------------------------------- PKCOLUMN_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PKCOLUMN_NAME = "PKCOLUMN_NAME";

    // ----------------------------------------------------------------------------------------------------- FKTABLE_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FKTABLE_CAT = "FKTABLE_CAT";

    // --------------------------------------------------------------------------------------------------- FKTABLE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FKTABLE_SCHEM = "FKTABLE_SCHEM";

    // ---------------------------------------------------------------------------------------------------- FKTABLE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FKTABLE_NAME = "FKTABLE_NAME";

    // --------------------------------------------------------------------------------------------------- FKCOLUMN_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FKCOLUMN_NAME = "FKCOLUMN_NAME";

    // --------------------------------------------------------------------------------------------------------- KEY_SEQ

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_KEY_SEQ = "KEY_SEQ";

    // ----------------------------------------------------------------------------------------------------- UPDATE_RULE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_UPDATE_RULE = "UPDATE_RULE";

    public static final int COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_NO_ACTION = DatabaseMetaData.importedKeyNoAction;

    public static final int COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_CASCADE = DatabaseMetaData.importedKeyCascade;

    public static final int COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_SET_NULL = DatabaseMetaData.importedKeySetNull;

    public static final int COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_SET_DEFAULT = DatabaseMetaData.importedKeySetDefault;

    public static final int COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_RESTRICT = DatabaseMetaData.importedKeyRestrict;

    static final List<Integer> COLUMN_VALUES_UPDATE_RULES = List.of(
            COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_CASCADE,    // 0
            COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_RESTRICT,   // 1
            COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_SET_NULL,   // 2
            COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_NO_ACTION,  // 3
            COLUMN_VALUE_UPDATE_RULE_IMPORTED_KEY_SET_DEFAULT // 4
    );

    // ----------------------------------------------------------------------------------------------------- DELETE_RULE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DELETE_RULE = "DELETE_RULE";

    public static final int COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_NO_ACTION = DatabaseMetaData.importedKeyNoAction;

    public static final int COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_CASCADE = DatabaseMetaData.importedKeyCascade;

    public static final int COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_SET_NULL = DatabaseMetaData.importedKeySetNull;

    public static final int COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_SET_DEFAULT = DatabaseMetaData.importedKeySetDefault;

    public static final int COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_RESTRICT = DatabaseMetaData.importedKeyRestrict;

    static final List<Integer> COLUMN_VALUES_DELETE_RULES = List.of(
            COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_CASCADE,    // 0
            COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_RESTRICT,   // 1
            COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_SET_NULL,   // 2
            COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_NO_ACTION,  // 3
            COLUMN_VALUE_DELETE_RULE_IMPORTED_KEY_SET_DEFAULT // 4
    );

    // --------------------------------------------------------------------------------------------------------- FK_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_FK_NAME = "FK_NAME";

    // --------------------------------------------------------------------------------------------------------- PK_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_PK_NAME = "PK_NAME";

    // --------------------------------------------------------------------------------------------------- DEFERRABILITY

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DEFERRABILITY = "DEFERRABILITY";

    public static final int COLUMN_VALUE_DEFERRABILITY_IMPORTED_KEY_CASCADE =
            DatabaseMetaData.importedKeyInitiallyDeferred;

    public static final int COLUMN_VALUE_DEFERRABILITY_IMPORTED_KEY_RESTRICT =
            DatabaseMetaData.importedKeyInitiallyImmediate;

    public static final int COLUMN_VALUE_DEFERRABILITY_IMPORTED_KEY_SET_NULL =
            DatabaseMetaData.importedKeyNotDeferrable;

    static final List<Integer> COLUMN_VALUES_DEFERRABILITY = List.of(
            COLUMN_VALUE_DEFERRABILITY_IMPORTED_KEY_CASCADE,  // 5
            COLUMN_VALUE_DEFERRABILITY_IMPORTED_KEY_RESTRICT, // 6
            COLUMN_VALUE_DEFERRABILITY_IMPORTED_KEY_SET_NULL  // 7
    );

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    CrossReference() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "pktableCat=" + pktableCat +
               ",pktableSchem=" + pktableSchem +
               ",pktableName=" + pktableName +
               ",pkcolumnName=" + pkcolumnName +
               ",fktableCat=" + fktableCat +
               ",fktableSchem=" + fktableSchem +
               ",fktableName=" + fktableName +
               ",fkcolumnName=" + fkcolumnName +
               ",keySeq=" + keySeq +
               ",updateRule=" + updateRule +
               ",deleteRule=" + deleteRule +
               ",fkName=" + fkName +
               ",pkName=" + pkName +
               ",deferrability=" + deferrability +
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
        final var that = (CrossReference) obj;
        return Objects.equals(fktableCat, that.fktableCat) &&
               Objects.equals(fktableSchem, that.fktableSchem) &&
               Objects.equals(fktableName, that.fktableName) &&
               Objects.equals(fkcolumnName, that.fkcolumnName) &&
               Objects.equals(pktableCat, that.pktableCat) &&
               Objects.equals(pktableSchem, that.pktableSchem) &&
               Objects.equals(pktableName, that.pktableName) &&
               Objects.equals(pkcolumnName, that.pkcolumnName) &&
               Objects.equals(keySeq, that.keySeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                fktableCat,
                fktableSchem,
                fktableName,
                fkcolumnName,
                pktableCat,
                pktableSchem,
                pktableName,
                pkcolumnName,
                keySeq
        );
    }

    // ----------------------------------------------------------------------------------------------------- pktableCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_PKTABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PKTABLE_CAT} column.
     */
    @Nullable
    public String getPktableCat() {
        return pktableCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PKTABLE_CAT} column.
     *
     * @param pktableCat the value of {@value #COLUMN_LABEL_PKTABLE_CAT} column.
     */
    void setPktableCat(final String pktableCat) {
        this.pktableCat = pktableCat;
    }

    // --------------------------------------------------------------------------------------------------- pktableSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_PKTABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PKTABLE_SCHEM} column.
     */
    @Nullable
    public String getPktableSchem() {
        return pktableSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PKTABLE_SCHEM} column.
     *
     * @param pktableSchem the value of {@value #COLUMN_LABEL_PKTABLE_SCHEM} column.
     */
    void setPktableSchem(final String pktableSchem) {
        this.pktableSchem = pktableSchem;
    }

    // ---------------------------------------------------------------------------------------------------- pktableName

    /**
     * Returns the value of {@value #COLUMN_LABEL_PKTABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PKTABLE_NAME} column.
     */
    public String getPktableName() {
        return pktableName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PKTABLE_NAME} column.
     *
     * @param pktableName the value of {@value #COLUMN_LABEL_PKTABLE_NAME} column.
     */
    void setPktableName(final String pktableName) {
        this.pktableName = pktableName;
    }

    // --------------------------------------------------------------------------------------------------- pkcolumnName

    /**
     * Returns the value of {@value #COLUMN_LABEL_PKCOLUMN_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PKCOLUMN_NAME} column.
     */
    public String getPkcolumnName() {
        return pkcolumnName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PKCOLUMN_NAME} column.
     *
     * @param pkcolumnName the value of {@value #COLUMN_LABEL_PKCOLUMN_NAME} column.
     */
    void setPkcolumnName(final String pkcolumnName) {
        this.pkcolumnName = pkcolumnName;
    }

    // ----------------------------------------------------------------------------------------------------- fktableCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_FKTABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FKTABLE_CAT} column.
     */
    @Nullable
    public String getFktableCat() {
        return fktableCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FKTABLE_CAT} column.
     *
     * @param fktableCat the value of {@value #COLUMN_LABEL_FKTABLE_CAT} column.
     */
    void setFktableCat(final String fktableCat) {
        this.fktableCat = fktableCat;
    }

    // --------------------------------------------------------------------------------------------------- fktableSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_FKTABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FKTABLE_SCHEM} column.
     */
    @Nullable
    public String getFktableSchem() {
        return fktableSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FKTABLE_SCHEM} column.
     *
     * @param fktableSchem the value of {@value #COLUMN_LABEL_FKTABLE_SCHEM} column.
     */
    void setFktableSchem(final String fktableSchem) {
        this.fktableSchem = fktableSchem;
    }

    // ---------------------------------------------------------------------------------------------------- fktableName

    /**
     * Returns the value of {@value #COLUMN_LABEL_FKTABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FKTABLE_NAME} column.
     */
    public String getFktableName() {
        return fktableName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FKTABLE_NAME} column.
     *
     * @param fktableName the value of {@value #COLUMN_LABEL_FKTABLE_NAME} column.
     */
    void setFktableName(final String fktableName) {
        this.fktableName = fktableName;
    }

    // --------------------------------------------------------------------------------------------------- fkcolumnName

    /**
     * Returns the value of {@value #COLUMN_LABEL_FKCOLUMN_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FKCOLUMN_NAME} column.
     */
    public String getFkcolumnName() {
        return fkcolumnName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FKCOLUMN_NAME} column.
     *
     * @param fkcolumnName the value of {@value #COLUMN_LABEL_FKCOLUMN_NAME} column.
     */
    void setFkcolumnName(final String fkcolumnName) {
        this.fkcolumnName = fkcolumnName;
    }

    // ---------------------------------------------------------------------------------------------------------- keySeq

    /**
     * Returns the value of {@value #COLUMN_LABEL_KEY_SEQ} column.
     *
     * @return the value of {@value #COLUMN_LABEL_KEY_SEQ} column.
     */
    public Integer getKeySeq() {
        return keySeq;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_KEY_SEQ} column.
     *
     * @param keySeq the value of {@value #COLUMN_LABEL_KEY_SEQ} column.
     */
    void setKeySeq(final Integer keySeq) {
        this.keySeq = keySeq;
    }

    // ------------------------------------------------------------------------------------------------------ updateRule

    /**
     * Returns the value of {@value #COLUMN_LABEL_UPDATE_RULE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_UPDATE_RULE} column.
     */
    public Integer getUpdateRule() {
        return updateRule;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_UPDATE_RULE} column.
     *
     * @param updateRule the value of {@value #COLUMN_LABEL_UPDATE_RULE} column.
     */
    void setUpdateRule(final Integer updateRule) {
        this.updateRule = updateRule;
    }

    // ------------------------------------------------------------------------------------------------------ deleteRule

    /**
     * Returns the value of {@value #COLUMN_LABEL_DELETE_RULE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DELETE_RULE} column.
     */
    public Integer getDeleteRule() {
        return deleteRule;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DELETE_RULE} column.
     *
     * @param deleteRule the value of {@value #COLUMN_LABEL_DELETE_RULE} column.
     */
    void setDeleteRule(final Integer deleteRule) {
        this.deleteRule = deleteRule;
    }

    // ---------------------------------------------------------------------------------------------------------- fkName

    /**
     * Returns the value of {@value #COLUMN_LABEL_FK_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_FK_NAME} column.
     */
    @Nullable
    public String getFkName() {
        return fkName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_FK_NAME} column.
     *
     * @param fkName the value of {@value #COLUMN_LABEL_FK_NAME} column.
     */
    void setFkName(final String fkName) {
        this.fkName = fkName;
    }

    // ---------------------------------------------------------------------------------------------------------- pkName

    /**
     * Returns the value of {@value #COLUMN_LABEL_PK_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_PK_NAME} column.
     */
    @Nullable
    public String getPkName() {
        return pkName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_PK_NAME} column.
     *
     * @param pkName the value of {@value #COLUMN_LABEL_PK_NAME} column.
     */
    void setPkName(final String pkName) {
        this.pkName = pkName;
    }

    // --------------------------------------------------------------------------------------------------- deferrability

    /**
     * Returns the value of {@value #COLUMN_LABEL_DEFERRABILITY} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DEFERRABILITY} column.
     */
    public Integer getDeferrability() {
        return deferrability;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DEFERRABILITY} column.
     *
     * @param deferrability the value of {@value #COLUMN_LABEL_DEFERRABILITY} column.
     */
    void setDeferrability(final Integer deferrability) {
        this.deferrability = deferrability;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PKTABLE_CAT)
    private String pktableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PKTABLE_SCHEM)
    private String pktableSchem;

    @_ColumnLabel(COLUMN_LABEL_PKTABLE_NAME)
    private String pktableName;

    @_ColumnLabel(COLUMN_LABEL_PKCOLUMN_NAME)
    private String pkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FKTABLE_CAT)
    private String fktableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FKTABLE_SCHEM)
    private String fktableSchem;

    @_ColumnLabel(COLUMN_LABEL_FKTABLE_NAME)
    private String fktableName;

    @_ColumnLabel(COLUMN_LABEL_FKCOLUMN_NAME)
    private String fkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_KEY_SEQ)
    private Integer keySeq;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_UPDATE_RULE)
    private Integer updateRule;

    @_ColumnLabel(COLUMN_LABEL_DELETE_RULE)
    private Integer deleteRule;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_FK_NAME)
    private String fkName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_PK_NAME)
    private String pkName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DEFERRABILITY)
    //noinspection NotNullFieldNotInitialized
    private Integer deferrability;
}
