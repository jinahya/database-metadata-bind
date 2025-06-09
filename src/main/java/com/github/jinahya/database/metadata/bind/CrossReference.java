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
import java.util.List;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CrossReference
        extends AbstractMetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<CrossReference> comparingSpecifiedOrder(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(CrossReference::getFktableCat, comparator)
                .thenComparing(CrossReference::getFktableSchem, comparator)
                .thenComparing(CrossReference::getFktableName, comparator)
                .thenComparing(CrossReference::getKeySeq, Comparator.naturalOrder());
    }

    static Comparator<CrossReference> comparingSpecifiedOrder(final Context context,
                                                              final Comparator<? super String> comparator)
            throws SQLException {
        return comparingSpecifiedOrder(ContextUtils.nullPrecedence(context, comparator));
    }

    // ----------------------------------------------------------------------------------------------------- PKTABLE_CAT
    public static final String COLUMN_LABEL_PKTABLE_CAT = "PKTABLE_CAT";

    // --------------------------------------------------------------------------------------------------- PKTABLE_SCHEM
    public static final String COLUMN_LABEL_PKTABLE_SCHEM = "PKTABLE_SCHEM";

    // ---------------------------------------------------------------------------------------------------- PKTABLE_NAME
    public static final String COLUMN_LABEL_PKTABLE_NAME = "PKTABLE_NAME";

    // --------------------------------------------------------------------------------------------------- PKCOLUMN_NAME
    public static final String COLUMN_LABEL_PKCOLUMN_NAME = "PKCOLUMN_NAME";

    // ----------------------------------------------------------------------------------------------------- FKTABLE_CAT
    public static final String COLUMN_LABEL_FKTABLE_CAT = "FKTABLE_CAT";

    // --------------------------------------------------------------------------------------------------- FKTABLE_SCHEM
    public static final String COLUMN_LABEL_FKTABLE_SCHEM = "FKTABLE_SCHEM";

    // ---------------------------------------------------------------------------------------------------- FKTABLE_NAME
    public static final String COLUMN_LABEL_FKTABLE_NAME = "FKTABLE_NAME";

    // --------------------------------------------------------------------------------------------------- FKCOLUMN_NAME
    public static final String COLUMN_LABEL_FKCOLUMN_NAME = "FKCOLUMN_NAME";

    // --------------------------------------------------------------------------------------------------------- KEY_SEQ
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
    public static final String COLUMN_LABEL_FK_NAME = "FK_NAME";

    // --------------------------------------------------------------------------------------------------------- PK_NAME
    public static final String COLUMN_LABEL_PK_NAME = "PK_NAME";

    // --------------------------------------------------------------------------------------------------- DEFERRABILITY
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

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    // ------------------------------------------------------------------------------------------------------ pkTableCat

    // ---------------------------------------------------------------------------------------------------- pkTableSchem

    // ----------------------------------------------------------------------------------------------------- pkTableName

    // ---------------------------------------------------------------------------------------------------- pkColumnName

    // ------------------------------------------------------------------------------------------------------ fktableCat

    // ---------------------------------------------------------------------------------------------------- fktableSchem

    // ----------------------------------------------------------------------------------------------------- fkTableName

    // ---------------------------------------------------------------------------------------------------- fkColumnName

    // ---------------------------------------------------------------------------------------------------------- keySeq

    // ------------------------------------------------------------------------------------------------------ updateRule

    // ------------------------------------------------------------------------------------------------------ deleteRule

    // ---------------------------------------------------------------------------------------------------------- fkName

    // ---------------------------------------------------------------------------------------------------------- pkName

    // --------------------------------------------------------------------------------------------------- deferrability

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
    @Positive
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
    private Integer deferrability;
}
