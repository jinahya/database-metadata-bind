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

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;

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
    static <T extends PortedKey> Comparator<T> comparingPktable_(final Comparator<? super String> comparator) {
        return Comparator
                .<T, String>comparing(PortedKey::getPktableCat, comparator)
                .thenComparing(PortedKey::getPktableSchem, comparator)
                .thenComparing(PortedKey::getPktableName, comparator)
                .thenComparing(PortedKey::getKeySeq, Comparator.naturalOrder());
    }

    static <T extends PortedKey> Comparator<T> comparingPktable_(final Context context,
                                                                 final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator
                .<T, String>comparing(PortedKey::getPktableCat, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(PortedKey::getPktableSchem, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(PortedKey::getPktableName, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(PortedKey::getKeySeq, ContextUtils.nullPrecedence(context, Comparator.naturalOrder()));
    }

    static <T extends PortedKey> Comparator<T> comparingFktable_(final Comparator<? super String> comparator) {
        return Comparator
                .<T, String>comparing(PortedKey::getFktableCat, comparator)
                .thenComparing(PortedKey::getFktableSchem, comparator)
                .thenComparing(PortedKey::getFktableName, comparator)
                .thenComparing(PortedKey::getKeySeq, Comparator.naturalOrder());
    }

    static <T extends PortedKey> Comparator<T> comparingFktable_(final Context context,
                                                                 final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator
                .<T, String>comparing(PortedKey::getFktableCat, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(PortedKey::getFktableSchem, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(PortedKey::getFktableName, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(PortedKey::getKeySeq, ContextUtils.nullPrecedence(context, Comparator.naturalOrder()));
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

    @Nullable
    public String getPktableCat() {
        return pktableCat;
    }

    protected void setPktableCat(@Nullable final String pktableCat) {
        this.pktableCat = pktableCat;
    }

    // ---------------------------------------------------------------------------------------------------- pktableSchem
    @Nullable
    public String getPktableSchem() {
        return pktableSchem;
    }

    protected void setPktableSchem(@Nullable final String pktableSchem) {
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

    @Nullable
    public String getFktableCat() {
        return fktableCat;
    }

    protected void setFktableCat(@Nullable final String fktableCat) {
        this.fktableCat = fktableCat;
    }

    // ---------------------------------------------------------------------------------------------------- fktableSchem
    @Nullable
    public String getFktableSchem() {
        return fktableSchem;
    }

    protected void setFktableSchem(@Nullable final String fktableSchem) {
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

    // ------------------------------------------------------------------------------------------------------ updateRule

    // ------------------------------------------------------------------------------------------------------ deleteRule

    // ---------------------------------------------------------------------------------------------------------- fkName

    // ---------------------------------------------------------------------------------------------------------- pkName

    // --------------------------------------------------------------------------------------------------- deferrability

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_CAT)
    private String pktableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_SCHEM)
    private String pktableSchem;

    @_ColumnLabel(COLUMN_NAME_PKTABLE_NAME)
    private String pktableName;

    @_ColumnLabel(COLUMN_NAME_PKCOLUMN_NAME)
    private String pkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FKTABLE_CAT)
    private String fktableCat;

    @Nullable
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
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FK_NAME)
    private String fkName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PK_NAME)
    private String pkName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_NAME_DEFERRABILITY)
    private Integer deferrability;

    // -----------------------------------------------------------------------------------------------------------------
    private transient Table pkTable_;

    private transient Table fkTable_;

    Table getPkTable_() {
        if (pkTable_ == null) {
            pkTable_ = Table.of(pktableCat, pktableSchem, pktableName);
        }
        return pkTable_;
    }

    void setPkTable_(final Table pkTable_) {
        this.pkTable_ = pkTable_;
        setPktableCat(
                Optional.ofNullable(this.pkTable_)
                        .map(Table::getTableCat)
                        .orElse(null)
        );
        setPktableSchem(
                Optional.ofNullable(pkTable_)
                        .map(Table::getTableSchem)
                        .orElse(null)
        );
        setPktableName(
                Optional.ofNullable(pkTable_)
                        .map(Table::getTableName)
                        .orElse(null)
        );
    }

    Table getFkTable_() {
        if (fkTable_ == null) {
            fkTable_ = Table.of(fktableCat, fktableSchem, fktableName);
        }
        return fkTable_;
    }

    void setFkTable_(final Table fkTable_) {
        this.fkTable_ = fkTable_;
        setFktableCat(
                Optional.ofNullable(this.fkTable_)
                        .map(Table::getTableCat)
                        .orElse(null)
        );
        setFktableSchem(
                Optional.ofNullable(fkTable_)
                        .map(Table::getTableSchem)
                        .orElse(null)
        );
        setFktableName(
                Optional.ofNullable(fkTable_)
                        .map(Table::getTableName)
                        .orElse(null)
        );
    }
}
