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

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.sql.SQLException;
import java.util.Objects;

/**
 * An abstract class for binding results of {@link java.sql.DatabaseMetaData#getExportedKeys(String, String, String)}
 * method or {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @see ExportedKey
 * @see ImportedKey
 */
@XmlSeeAlso({ExportedKey.class, ImportedKey.class})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public abstract class TableKey
        implements MetadataType,
                   ChildOf<Table> {

    private static final long serialVersionUID = 6713872409315471232L;

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @Override
    public Table extractParent() {
        return Table.builder()
                .tableCat(getPktableCat())
                .tableCat(getPktableSchem())
                .tableCat(getPktableName())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Column extractPkColumn() {
        return Column.builder()
                .tableCat(getPktableCat())
                .tableSchem(getPktableSchem())
                .tableName(getPktableName())
                .columnName(getPkcolumnName())
                .build();
    }

    public Column extractFkColumn() {
        return Column.builder()
                .tableCat(getFktableCat())
                .tableSchem(getFktableSchem())
                .tableName(getFktableName())
                .columnName(getFkcolumnName())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public ImportedKeyRule getUpdateRuleAsEnum() {
        return ImportedKeyRule.valueOfRawValue(getUpdateRule());
    }

    public void setUpdateRuleAsEnum(final ImportedKeyRule updateRuleAsEnum) {
        Objects.requireNonNull(updateRuleAsEnum, "updateRuleAsEnum is null");
        setUpdateRule(updateRuleAsEnum.rawValue());
    }

    public ImportedKeyRule getDeleteRuleAsEnum() {
        return ImportedKeyRule.valueOfRawValue(getDeleteRule());
    }

    public void setDeleteRuleAsEnum(final ImportedKeyRule deleteRuleAsEnum) {
        Objects.requireNonNull(deleteRuleAsEnum, "deleteRuleAsEnum is null");
        setDeleteRule(deleteRuleAsEnum.rawValue());
    }

    public ImportedKeyDeferrability getDeferrabilityAsEnum() {
        return ImportedKeyDeferrability.valueOfRawValue(getDeferrability());
    }

    public void setDeferrabilityAsEnum(final ImportedKeyDeferrability deferrabilityAsEnum) {
        setDeferrability(deferrabilityAsEnum.rawValue());
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PKTABLE_CAT")
    private String pktableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PKTABLE_SCHEM")
    private String pktableSchem;

    @XmlElement(required = true)
    @NotBlank
    @Label("PKTABLE_NAME")
    private String pktableName;

    @XmlElement(required = true)
    @NotBlank
    @Label("PKCOLUMN_NAME")
    private String pkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("FKTABLE_CAT")
    private String fktableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("FKTABLE_NAME")
    private String fktableSchem;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("FKTABLE_NAME")
    private String fktableName;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("FKCOLUMN_NAME")
    private String fkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = false, required = true)
    @Positive
    @Label("FKCOLUMN_NAME")
    private int keySeq;

    @XmlElement(nillable = false, required = true)
    @PositiveOrZero
    @Label("UPDATE_RULE")
    private int updateRule;

    @XmlElement(nillable = false, required = true)
    @PositiveOrZero
    @Label("DELETE_RULE")
    private int deleteRule;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("FK_NAME")
    private String fkName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PK_NAME")
    private String pkName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = false, required = true)
    @Label("DEFERRABILITY")
    private int deferrability;
}
