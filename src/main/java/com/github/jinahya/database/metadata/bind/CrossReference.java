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
import java.util.Comparator;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class CrossReference extends AbstractMetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    static final Comparator<CrossReference> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(CrossReference::fktableCatNonNull, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(CrossReference::fktableSchemNonNull, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(CrossReference::getFktableName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparingInt(CrossReference::getKeySeq);

    static final Comparator<CrossReference> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(CrossReference::fktableCatNonNull, naturalOrder())
                    .thenComparing(CrossReference::fktableSchemNonNull, naturalOrder())
                    .thenComparing(CrossReference::getFktableName, nullsFirst(naturalOrder()))
                    .thenComparingInt(CrossReference::getKeySeq);

    // ----------------------------------------------------------------------------------------------------- UPDATE_RULE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_UPDATE_RULE = "UPDATE_RULE";

    // ------------------------------------------------------------------------------------------------------ pkTableCat
    @EqualsAndHashCode.Include
    String pktableCatNonNull() {
        if (pktableCat == null) {
            return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
        }
        return pktableCat;
    }

    // ---------------------------------------------------------------------------------------------------- pkTableSchem
    @EqualsAndHashCode.Include
    String pktableSchemNonNull() {
        if (pktableSchem == null) {
            return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
        }
        return pktableSchem;
    }

    // ------------------------------------------------------------------------------------------------------ fktableCat
    @EqualsAndHashCode.Include
    String fktableCatNonNull() {
        if (fktableCat == null) {
            return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
        }
        return fktableCat;
    }

    // ---------------------------------------------------------------------------------------------------- fktableSchem
    @EqualsAndHashCode.Include
    String fktableSchemNonNull() {
        if (fktableSchem == null) {
            return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
        }
        return fktableSchem;
    }

    // ------------------------------------------------------------------------------------------------------ updateRule
    TableKey.TableKeyUpdateRule getUpdateRuleAsEnum() {
        return Optional.ofNullable(getUpdateRule())
                .map(TableKey.TableKeyUpdateRule::valueOfUpdateRule)
                .orElse(null);
    }

    void setUpdateRuleAsEnum(final TableKey.TableKeyUpdateRule updateRuleAsEnum) {
        setUpdateRule(
                Optional.ofNullable(updateRuleAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // ------------------------------------------------------------------------------------------------------ deleteRule

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("PKTABLE_CAT")
    private String pktableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("PKTABLE_SCHEM")
    private String pktableSchem;

    @_ColumnLabel("PKTABLE_NAME")
    @EqualsAndHashCode.Include
    private String pktableName;

    @_ColumnLabel("PKCOLUMN_NAME")
    @EqualsAndHashCode.Include
    private String pkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("FKTABLE_CAT")
    private String fktableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("FKTABLE_SCHEM")
    private String fktableSchem;

    @_ColumnLabel("FKTABLE_NAME")
    @EqualsAndHashCode.Include
    private String fktableName;

    @_ColumnLabel("FKCOLUMN_NAME")
    @EqualsAndHashCode.Include
    private String fkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @Positive
    @_ColumnLabel("KEY_SEQ")
    private Integer keySeq;

    @_ColumnLabel(COLUMN_LABEL_UPDATE_RULE)
    private Integer updateRule;

    @_ColumnLabel("DELETE_RULE")
    private Integer deleteRule;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("FK_NAME")
    private String fkName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("PK_NAME")
    private String pkName;

    @_ColumnLabel("DEFERRABILITY")
    private Integer deferrability;
}
