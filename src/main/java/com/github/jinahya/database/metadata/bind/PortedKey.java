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
import java.util.Comparator;
import java.util.Objects;

/**
 * An abstract class for binding results of the
 * {@link java.sql.DatabaseMetaData#getExportedKeys(String, String, String)} method or the
 * {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @see ExportedKey
 * @see ImportedKey
 */
abstract class PortedKey
        extends AbstractMetadataType {

    private static final long serialVersionUID = 6713872409315471232L;

    // -----------------------------------------------------------------------------------------------------------------
    static <T extends PortedKey> Comparator<T> comparingPktable(final Comparator<? super String> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator
                .<T, String>comparing(PortedKey::getPktableCat, comparator)
                .thenComparing(PortedKey::getPktableSchem, comparator)
                .thenComparing(PortedKey::getPktableName, comparator)
                .thenComparing(PortedKey::getPkName, comparator)
                .thenComparing(PortedKey::getKeySeq, Comparator.naturalOrder());
    }

    static <T extends PortedKey> Comparator<T> comparingFktable(final Comparator<? super String> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator
                .<T, String>comparing(PortedKey::getFktableCat, comparator)
                .thenComparing(PortedKey::getFktableSchem, comparator)
                .thenComparing(PortedKey::getFktableName, comparator)
                .thenComparing(PortedKey::getFkName, comparator)
                .thenComparing(PortedKey::getKeySeq, Comparator.naturalOrder());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PKTABLE_CAT = "PKTABLE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PKTABLE_SCHEM = "PKTABLE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PKTABLE_NAME = "PKTABLE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PKCOLUMN_NAME = "PKCOLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FKTABLE_CAT = "FKTABLE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FKTABLE_SCHEM = "FKTABLE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FKTABLE_NAME = "FKTABLE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FKCOLUMN_NAME = "FKCOLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_KEY_SEQ = "KEY_SEQ";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_UPDATE_RULE = "UPDATE_RULE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_DELETE_RULE = "DELETE_RULE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_FK_NAME = "FK_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_PK_NAME = "PK_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_DEFERRABILITY = "DEFERRABILITY";

    public static final int COLUMN_VALUE_DEFERRABILITY_IMPORTED_KEY_INITIALLY_DEFERRED =
            DatabaseMetaData.importedKeyInitiallyDeferred;

    public static final int COLUMN_VALUE_DEFERRABILITY_IMPORTED_KEY_INITIALLY_IMMEDIATE =
            DatabaseMetaData.importedKeyInitiallyImmediate;

    public static final int COLUMN_VALUE_DEFERRABILITY_IMPORTED_KEY_NOT_DEFERRABLE =
            DatabaseMetaData.importedKeyNotDeferrable;

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS
    PortedKey() {
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

    // ----------------------------------------------------------------------------------------------------- pktableCat

    /**
     * Returns the value of {@value #COLUMN_NAME_PKTABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_NAME_PKTABLE_CAT} column.
     */
    public String getPktableCat() {
        return pktableCat;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_PKTABLE_CAT} column.
     *
     * @param pktableCat the value of {@value #COLUMN_NAME_PKTABLE_CAT} column.
     */
    void setPktableCat(final String pktableCat) {
        this.pktableCat = pktableCat;
    }

    // --------------------------------------------------------------------------------------------------- pktableSchem

    /**
     * Returns the value of {@value #COLUMN_NAME_PKTABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_NAME_PKTABLE_SCHEM} column.
     */
    public String getPktableSchem() {
        return pktableSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_PKTABLE_SCHEM} column.
     *
     * @param pktableSchem the value of {@value #COLUMN_NAME_PKTABLE_SCHEM} column.
     */
    void setPktableSchem(final String pktableSchem) {
        this.pktableSchem = pktableSchem;
    }

    // ---------------------------------------------------------------------------------------------------- pktableName

    /**
     * Returns the value of {@value #COLUMN_NAME_PKTABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_NAME_PKTABLE_NAME} column.
     */
    public String getPktableName() {
        return pktableName;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_PKTABLE_NAME} column.
     *
     * @param pktableName the value of {@value #COLUMN_NAME_PKTABLE_NAME} column.
     */
    void setPktableName(final String pktableName) {
        this.pktableName = pktableName;
    }

    // --------------------------------------------------------------------------------------------------- pkcolumnName

    /**
     * Returns the value of {@value #COLUMN_NAME_PKCOLUMN_NAME} column.
     *
     * @return the value of {@value #COLUMN_NAME_PKCOLUMN_NAME} column.
     */
    public String getPkcolumnName() {
        return pkcolumnName;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_PKCOLUMN_NAME} column.
     *
     * @param pkcolumnName the value of {@value #COLUMN_NAME_PKCOLUMN_NAME} column.
     */
    void setPkcolumnName(final String pkcolumnName) {
        this.pkcolumnName = pkcolumnName;
    }

    // ----------------------------------------------------------------------------------------------------- fktableCat

    /**
     * Returns the value of {@value #COLUMN_NAME_FKTABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_NAME_FKTABLE_CAT} column.
     */
    public String getFktableCat() {
        return fktableCat;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_FKTABLE_CAT} column.
     *
     * @param fktableCat the value of {@value #COLUMN_NAME_FKTABLE_CAT} column.
     */
    void setFktableCat(final String fktableCat) {
        this.fktableCat = fktableCat;
    }

    // --------------------------------------------------------------------------------------------------- fktableSchem

    /**
     * Returns the value of {@value #COLUMN_NAME_FKTABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_NAME_FKTABLE_SCHEM} column.
     */
    public String getFktableSchem() {
        return fktableSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_FKTABLE_SCHEM} column.
     *
     * @param fktableSchem the value of {@value #COLUMN_NAME_FKTABLE_SCHEM} column.
     */
    void setFktableSchem(final String fktableSchem) {
        this.fktableSchem = fktableSchem;
    }

    // ---------------------------------------------------------------------------------------------------- fktableName

    /**
     * Returns the value of {@value #COLUMN_NAME_FKTABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_NAME_FKTABLE_NAME} column.
     */
    public String getFktableName() {
        return fktableName;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_FKTABLE_NAME} column.
     *
     * @param fktableName the value of {@value #COLUMN_NAME_FKTABLE_NAME} column.
     */
    void setFktableName(final String fktableName) {
        this.fktableName = fktableName;
    }

    // --------------------------------------------------------------------------------------------------- fkcolumnName

    /**
     * Returns the value of {@value #COLUMN_NAME_FKCOLUMN_NAME} column.
     *
     * @return the value of {@value #COLUMN_NAME_FKCOLUMN_NAME} column.
     */
    public String getFkcolumnName() {
        return fkcolumnName;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_FKCOLUMN_NAME} column.
     *
     * @param fkcolumnName the value of {@value #COLUMN_NAME_FKCOLUMN_NAME} column.
     */
    void setFkcolumnName(final String fkcolumnName) {
        this.fkcolumnName = fkcolumnName;
    }

    // ---------------------------------------------------------------------------------------------------------- keySeq

    /**
     * Returns the value of {@value #COLUMN_NAME_KEY_SEQ} column.
     *
     * @return the value of {@value #COLUMN_NAME_KEY_SEQ} column.
     */
    public Integer getKeySeq() {
        return keySeq;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_KEY_SEQ} column.
     *
     * @param keySeq the value of {@value #COLUMN_NAME_KEY_SEQ} column.
     */
    void setKeySeq(final Integer keySeq) {
        this.keySeq = keySeq;
    }

    // ------------------------------------------------------------------------------------------------------ updateRule

    /**
     * Returns the value of {@value #COLUMN_NAME_UPDATE_RULE} column.
     *
     * @return the value of {@value #COLUMN_NAME_UPDATE_RULE} column.
     */
    public Integer getUpdateRule() {
        return updateRule;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_UPDATE_RULE} column.
     *
     * @param updateRule the value of {@value #COLUMN_NAME_UPDATE_RULE} column.
     */
    void setUpdateRule(final Integer updateRule) {
        this.updateRule = updateRule;
    }

    // ------------------------------------------------------------------------------------------------------ deleteRule

    /**
     * Returns the value of {@value #COLUMN_NAME_DELETE_RULE} column.
     *
     * @return the value of {@value #COLUMN_NAME_DELETE_RULE} column.
     */
    public Integer getDeleteRule() {
        return deleteRule;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_DELETE_RULE} column.
     *
     * @param deleteRule the value of {@value #COLUMN_NAME_DELETE_RULE} column.
     */
    void setDeleteRule(final Integer deleteRule) {
        this.deleteRule = deleteRule;
    }

    // ---------------------------------------------------------------------------------------------------------- fkName

    /**
     * Returns the value of {@value #COLUMN_NAME_FK_NAME} column.
     *
     * @return the value of {@value #COLUMN_NAME_FK_NAME} column.
     */
    public String getFkName() {
        return fkName;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_FK_NAME} column.
     *
     * @param fkName the value of {@value #COLUMN_NAME_FK_NAME} column.
     */
    void setFkName(final String fkName) {
        this.fkName = fkName;
    }

    // ---------------------------------------------------------------------------------------------------------- pkName

    /**
     * Returns the value of {@value #COLUMN_NAME_PK_NAME} column.
     *
     * @return the value of {@value #COLUMN_NAME_PK_NAME} column.
     */
    public String getPkName() {
        return pkName;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_PK_NAME} column.
     *
     * @param pkName the value of {@value #COLUMN_NAME_PK_NAME} column.
     */
    void setPkName(final String pkName) {
        this.pkName = pkName;
    }

    // --------------------------------------------------------------------------------------------------- deferrability

    /**
     * Returns the value of {@value #COLUMN_NAME_DEFERRABILITY} column.
     *
     * @return the value of {@value #COLUMN_NAME_DEFERRABILITY} column.
     */
    public Integer getDeferrability() {
        return deferrability;
    }

    /**
     * Sets the value of {@value #COLUMN_NAME_DEFERRABILITY} column.
     *
     * @param deferrability the value of {@value #COLUMN_NAME_DEFERRABILITY} column.
     */
    void setDeferrability(final Integer deferrability) {
        this.deferrability = deferrability;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_CAT)
    String pktableCat;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_SCHEM)
    String pktableSchem;

    @_ColumnLabel(COLUMN_NAME_PKTABLE_NAME)
    String pktableName;

    @_ColumnLabel(COLUMN_NAME_PKCOLUMN_NAME)
    String pkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FKTABLE_CAT)
    String fktableCat;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FKTABLE_SCHEM)
    String fktableSchem;

    @_ColumnLabel(COLUMN_NAME_FKTABLE_NAME)
    String fktableName;

    @_ColumnLabel(COLUMN_NAME_FKCOLUMN_NAME)
    String fkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_NAME_KEY_SEQ)
    Integer keySeq;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_NAME_UPDATE_RULE)
    private Integer updateRule;

    @_ColumnLabel(COLUMN_NAME_DELETE_RULE)
    private Integer deleteRule;

    // -----------------------------------------------------------------------------------------------------------------
    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FK_NAME)
    private String fkName;

    @org.jspecify.annotations.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PK_NAME)
    private String pkName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_NAME_DEFERRABILITY)
    private Integer deferrability;
}
