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
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * An abstract class for binding results of the
 * {@link java.sql.DatabaseMetaData#getExportedKeys(String, String, String)} method or the
 * {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @see ExportedKey
 * @see ImportedKey
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
abstract class PortedKey
        extends AbstractMetadataType {

    private static final long serialVersionUID = 6713872409315471232L;

    // -----------------------------------------------------------------------------------------------------------------
    static <T extends PortedKey> Comparator<T> comparingPktable_(final Context context,
                                                                 final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator
                .<T, String>comparing(PortedKey::getPktableCat, ContextUtils.nulls(context, comparator))
                .thenComparing(PortedKey::getPktableSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(PortedKey::getPktableName, ContextUtils.nulls(context, comparator))
                .thenComparing(PortedKey::getKeySeq, ContextUtils.nulls(context, Comparator.naturalOrder()));
    }

    static <T extends PortedKey> Comparator<T> comparingFktable_(final Context context,
                                                                 final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator
                .<T, String>comparing(PortedKey::getFktableCat, ContextUtils.nulls(context, comparator))
                .thenComparing(PortedKey::getFktableSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(PortedKey::getFktableName, ContextUtils.nulls(context, comparator))
                .thenComparing(PortedKey::getKeySeq, ContextUtils.nulls(context, Comparator.naturalOrder()));
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

    /**
     * Constants for values of {@value PortedKey#COLUMN_NAME_UPDATE_RULE} column.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see ExportedKey
     * @see ImportedKey
     */
    public enum TableKeyUpdateRule
            implements _IntFieldEnum<TableKeyUpdateRule> {

        /**
         * Constants for {@link DatabaseMetaData#importedKeyCascade}({@value DatabaseMetaData#importedKeyCascade}).
         */
        IMPORTED_KEY_CASCADE(DatabaseMetaData.importedKeyCascade), // 0

        /**
         * Constants for {@link DatabaseMetaData#importedKeyRestrict}({@value DatabaseMetaData#importedKeyRestrict}).
         */
        IMPORTED_KEY_RESTRICT(DatabaseMetaData.importedKeyRestrict), // 1

        /**
         * Constants for {@link DatabaseMetaData#importedKeySetNull}({@value DatabaseMetaData#importedKeySetNull}).
         */
        IMPORTED_KEY_SET_NULL(DatabaseMetaData.importedKeySetNull), // 2

        /**
         * Constants for {@link DatabaseMetaData#importedKeyNoAction}({@value DatabaseMetaData#importedKeyNoAction}).
         */
        IMPORTED_KEY_NO_ACTION(DatabaseMetaData.importedKeyNoAction), // 3

        /**
         * Constants for
         * {@link DatabaseMetaData#importedKeySetDefault}({@value DatabaseMetaData#importedKeySetDefault}).
         */
        IMPORTED_KEY_SET_DEFAULT(DatabaseMetaData.importedKeySetDefault); // 4

        public static TableKeyUpdateRule valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(TableKeyUpdateRule.class, fieldValue);
        }

        TableKeyUpdateRule(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_NAME_DELETE_RULE = "DELETE_RULE";

    /**
     * Constants for values of {@value PortedKey#COLUMN_NAME_DELETE_RULE} column.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see ExportedKey
     * @see ImportedKey
     */
    public enum TableKeyDeleteRule
            implements _IntFieldEnum<TableKeyDeleteRule> {

        /**
         * Constants for {@link DatabaseMetaData#importedKeyCascade}({@value DatabaseMetaData#importedKeyCascade}).
         */
        IMPORTED_KEY_CASCADE(DatabaseMetaData.importedKeyCascade), // 0

        /**
         * Constants for {@link DatabaseMetaData#importedKeyRestrict}({@value DatabaseMetaData#importedKeyRestrict}).
         */
        IMPORTED_KEY_RESTRICT(DatabaseMetaData.importedKeyRestrict), // 1

        /**
         * Constants for {@link DatabaseMetaData#importedKeySetNull}({@value DatabaseMetaData#importedKeySetNull}).
         */
        IMPORTED_KEY_SET_NULL(DatabaseMetaData.importedKeySetNull), // 2

        /**
         * Constants for {@link DatabaseMetaData#importedKeyNoAction}({@value DatabaseMetaData#importedKeyNoAction}).
         */
        IMPORTED_KEY_NO_ACTION(DatabaseMetaData.importedKeyNoAction), // 3

        /**
         * Constants for
         * {@link DatabaseMetaData#importedKeySetDefault}({@value DatabaseMetaData#importedKeySetDefault}).
         */
        IMPORTED_KEY_SET_DEFAULT(DatabaseMetaData.importedKeySetDefault); // 4

        public static TableKeyDeleteRule valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(TableKeyDeleteRule.class, fieldValue);
        }

        TableKeyDeleteRule(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

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

    /**
     * Constants for values of {@value PortedKey#COLUMN_NAME_DEFERRABILITY} column.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see ExportedKey
     * @see ImportedKey
     */
    public enum TableKeyDeferrability
            implements _IntFieldEnum<TableKeyDeferrability> {

        /**
         * Constants for
         * {@link DatabaseMetaData#importedKeyInitiallyDeferred}({@value
         * DatabaseMetaData#importedKeyInitiallyDeferred}).
         */
        IMPORTED_KEY_INITIALLY_DEFERRED(DatabaseMetaData.importedKeyInitiallyDeferred), // 5

        /**
         * Constants for
         * {@link DatabaseMetaData#importedKeyInitiallyImmediate}({@value
         * DatabaseMetaData#importedKeyInitiallyImmediate}).
         */
        IMPORTED_KEY_INITIALLY_IMMEDIATE(DatabaseMetaData.importedKeyInitiallyImmediate), // 6

        /**
         * Constants for
         * {@link DatabaseMetaData#importedKeyNotDeferrable}({@value DatabaseMetaData#importedKeyNotDeferrable}).
         */
        IMPORTED_KEY_NOT_DEFERRABLE(DatabaseMetaData.importedKeyNotDeferrable); // 7

        public static TableKeyDeferrability valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(TableKeyDeferrability.class, fieldValue);
        }

        TableKeyDeferrability(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static final BiPredicate<PortedKey, Table> IS_OF_PKTABLE = (k, t) -> {
        return Objects.equals(k.pktableCat, t.getTableCat()) &&
               Objects.equals(k.pktableSchem, t.getTableSchem()) &&
               Objects.equals(k.pktableName, t.getTableName());
    };

    static final BiPredicate<PortedKey, Table> IS_OF_FKTABLE = (k, t) -> {
        return Objects.equals(k.fktableCat, t.getTableCat()) &&
               Objects.equals(k.fktableSchem, t.getTableSchem()) &&
               Objects.equals(k.fktableName, t.getTableName());
    };

    // ------------------------------------------------------------------------------------------------------ pktableCat

    // ---------------------------------------------------------------------------------------------------- pktableSchem

    // ------------------------------------------------------------------------------------------------------ fktableCat

    // ---------------------------------------------------------------------------------------------------- fktableSchem

    // ------------------------------------------------------------------------------------------------------ updateRule
    TableKeyUpdateRule getUpdateRuleAsEnum() {
        return Optional.ofNullable(getUpdateRule())
                .map(TableKeyUpdateRule::valueOfFieldValue)
                .orElse(null);
    }

    void setUpdateRuleAsEnum(final TableKeyUpdateRule updateRuleAsEnum) {
        setUpdateRule(
                Optional.ofNullable(updateRuleAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // ------------------------------------------------------------------------------------------------------ deleteRule
    TableKeyDeleteRule getDeleteRuleAsEnum() {
        return Optional.ofNullable(getDeleteRule())
                .map(TableKeyDeleteRule::valueOfFieldValue)
                .orElse(null);
    }

    void setDeleteRuleAsEnum(final TableKeyDeleteRule deleteRuleAsEnum) {
        setDeleteRule(
                Optional.ofNullable(deleteRuleAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // --------------------------------------------------------------------------------------------------- deferrability
    TableKeyDeferrability getDeferrabilityAsEnum() {
        return Optional.ofNullable(getDeferrability())
                .map(v -> TableKeyDeferrability.valueOfFieldValue(getDeferrability()))
                .orElse(null);
    }

    void setDeferrabilityAsEnum(final TableKeyDeferrability deferrabilityAsEnum) {
        setDeferrability(
                Optional.ofNullable(deferrabilityAsEnum)
                        .map(TableKeyDeferrability::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_CAT)
    @EqualsAndHashCode.Include
    private String pktableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PKTABLE_SCHEM)
    @EqualsAndHashCode.Include
    private String pktableSchem;

    @_ColumnLabel(COLUMN_NAME_PKTABLE_NAME)
    @EqualsAndHashCode.Include
    private String pktableName;

    @_ColumnLabel(COLUMN_NAME_PKCOLUMN_NAME)
    @EqualsAndHashCode.Include
    private String pkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FKTABLE_CAT)
    @EqualsAndHashCode.Include
    private String fktableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FKTABLE_SCHEM)
    @EqualsAndHashCode.Include
    private String fktableSchem;

    @_ColumnLabel(COLUMN_NAME_FKTABLE_NAME)
    @EqualsAndHashCode.Include
    private String fktableName;

    @_ColumnLabel(COLUMN_NAME_FKCOLUMN_NAME)
    @EqualsAndHashCode.Include
    private String fkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @Positive
    @_ColumnLabel(COLUMN_NAME_KEY_SEQ)
    private Integer keySeq;

    @_ColumnLabel(COLUMN_NAME_UPDATE_RULE)
    private Integer updateRule;

    @_ColumnLabel(COLUMN_NAME_DELETE_RULE)
    private Integer deleteRule;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_FK_NAME)
    private String fkName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_NAME_PK_NAME)
    private String pkName;

    @_ColumnLabel(COLUMN_NAME_DEFERRABILITY)
    private Integer deferrability;
}
