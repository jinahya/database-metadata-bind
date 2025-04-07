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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;

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
public class CrossReference
        extends AbstractMetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<CrossReference> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator.comparing(CrossReference::getFktableCat, ContextUtils.nulls(context, comparator))
                .thenComparing(CrossReference::getFktableSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(CrossReference::getFktableName, ContextUtils.nulls(context, comparator))
                .thenComparing(CrossReference::getKeySeq, ContextUtils.nulls(context, Comparator.naturalOrder()));
    }

    // ----------------------------------------------------------------------------------------------------- UPDATE_RULE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_UPDATE_RULE = "UPDATE_RULE";

    // ------------------------------------------------------------------------------------------------------ pkTableCat

    // ---------------------------------------------------------------------------------------------------- pkTableSchem

    // ------------------------------------------------------------------------------------------------------ fktableCat

    // ---------------------------------------------------------------------------------------------------- fktableSchem

    // ------------------------------------------------------------------------------------------------------ updateRule
    PortedKey.TableKeyUpdateRule getUpdateRuleAsEnum() {
        return Optional.ofNullable(getUpdateRule())
                .map(PortedKey.TableKeyUpdateRule::valueOfFieldValue)
                .orElse(null);
    }

    void setUpdateRuleAsEnum(final PortedKey.TableKeyUpdateRule updateRuleAsEnum) {
        setUpdateRule(
                Optional.ofNullable(updateRuleAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // ------------------------------------------------------------------------------------------------------ deleteRule
    PortedKey.TableKeyUpdateRule getDeleteRuleAsEnum() {
        return Optional.ofNullable(getDeleteRule())
                .map(PortedKey.TableKeyUpdateRule::valueOfFieldValue)
                .orElse(null);
    }

    void setDeleteRuleAsEnum(final PortedKey.TableKeyUpdateRule deleteRuleAsEnum) {
        setDeleteRule(
                Optional.ofNullable(deleteRuleAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @_ColumnLabel("PKTABLE_CAT")
    private String pktableCat;

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
    @_NullableBySpecification
    @_ColumnLabel("FKTABLE_CAT")
    private String fktableCat;

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
    @_ColumnLabel("KEY_SEQ")
    private Integer keySeq;

    @_ColumnLabel(COLUMN_LABEL_UPDATE_RULE)
    private Integer updateRule;

    @_ColumnLabel("DELETE_RULE")
    private Integer deleteRule;

    @_NullableBySpecification
    @_ColumnLabel("FK_NAME")
    private String fkName;

    @_NullableBySpecification
    @_ColumnLabel("PK_NAME")
    private String pkName;

    @_ColumnLabel("DEFERRABILITY")
    private Integer deferrability;
}
