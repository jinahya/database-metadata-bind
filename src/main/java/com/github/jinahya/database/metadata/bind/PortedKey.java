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

import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
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

    // ------------------------------------------------------------------------------------------------------ pktableCat

    public String getPktableCat() {
        return pktableCat;
    }

    protected void setPktableCat(final String pktableCat) {
        this.pktableCat = pktableCat;
    }

    // ---------------------------------------------------------------------------------------------------- pktableSchem
    public String getPktableSchem() {
        return pktableSchem;
    }

    protected void setPktableSchem(final String pktableSchem) {
        this.pktableSchem = pktableSchem;
    }

    // ----------------------------------------------------------------------------------------------------- pktableName
    public String getPktableName() {
        return pktableName;
    }

    protected void setPktableName(final String pktableName) {
        this.pktableName = pktableName;
    }

    // ---------------------------------------------------------------------------------------------------- pkcolumnName
    public String getPkcolumnName() {
        return pkcolumnName;
    }

    protected void setPkcolumnName(final String pkcolumnName) {
        this.pkcolumnName = pkcolumnName;
    }

    // ------------------------------------------------------------------------------------------------------ fktableCat

    public String getFktableCat() {
        return fktableCat;
    }

    protected void setFktableCat(final String fktableCat) {
        this.fktableCat = fktableCat;
    }

    // ---------------------------------------------------------------------------------------------------- fktableSchem
    public String getFktableSchem() {
        return fktableSchem;
    }

    protected void setFktableSchem(final String fktableSchem) {
        this.fktableSchem = fktableSchem;
    }

    // ----------------------------------------------------------------------------------------------------- fktableName
    public String getFktableName() {
        return fktableName;
    }

    protected void setFktableName(final String fktableName) {
        this.fktableName = fktableName;
    }

    // ---------------------------------------------------------------------------------------------------- fkcolumnName
    public String getFkcolumnName() {
        return fkcolumnName;
    }

    protected void setFkcolumnName(final String fkcolumnName) {
        this.fkcolumnName = fkcolumnName;
    }

    // ---------------------------------------------------------------------------------------------------------- keySeq
    public Integer getKeySeq() {
        return keySeq;
    }

    protected void setKeySeq(final Integer keySeq) {
        this.keySeq = keySeq;
    }

    // ------------------------------------------------------------------------------------------------------ updateRule
    public Integer getUpdateRule() {
        return updateRule;
    }

    protected void setUpdateRule(final Integer updateRule) {
        this.updateRule = updateRule;
    }

    // ------------------------------------------------------------------------------------------------------ deleteRule
    public Integer getDeleteRule() {
        return deleteRule;
    }

    protected void setDeleteRule(final Integer deleteRule) {
        this.deleteRule = deleteRule;
    }

    // ---------------------------------------------------------------------------------------------------------- fkName

    public String getFkName() {
        return fkName;
    }

    public void setFkName(final String fkName) {
        this.fkName = fkName;
    }

    // ---------------------------------------------------------------------------------------------------------- pkName
    public String getPkName() {
        return pkName;
    }

    public void setPkName(final String pkName) {
        this.pkName = pkName;
    }

    // --------------------------------------------------------------------------------------------------- deferrability
    public Integer getDeferrability() {
        return deferrability;
    }

    protected void setDeferrability(final Integer deferrability) {
        this.deferrability = deferrability;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_CAT)
    private String pktableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_SCHEM)
    private String pktableSchem;

    @_ColumnLabel(COLUMN_NAME_PKTABLE_NAME)
    private String pktableName;

    @_ColumnLabel(COLUMN_NAME_PKCOLUMN_NAME)
    private String pkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FKTABLE_CAT)
    private String fktableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FKTABLE_SCHEM)
    private String fktableSchem;

    @_ColumnLabel(COLUMN_NAME_FKTABLE_NAME)
    private String fktableName;

    @_ColumnLabel(COLUMN_NAME_FKCOLUMN_NAME)
    private String fkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_NAME_KEY_SEQ)
    private Integer keySeq;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_NAME_UPDATE_RULE)
    private Integer updateRule;

    @_ColumnLabel(COLUMN_NAME_DELETE_RULE)
    private Integer deleteRule;

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FK_NAME)
    private String fkName;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PK_NAME)
    private String pkName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_NAME_DEFERRABILITY)
    private Integer deferrability;
}
