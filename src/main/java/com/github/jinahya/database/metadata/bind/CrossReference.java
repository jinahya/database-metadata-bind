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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding the result of
 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getCrossReference(String, String, String, String, String, String, Collection)
 */
@XmlRootElement
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class CrossReference
        implements MetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    public static <C extends Collection<? super CrossReference>> C getAllInstance(final Context context,
                                                                                  final C collection)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(collection, "collection is null");
        final List<Table> tables = context.getTables(null, null, "%", null, new ArrayList<>());
        for (final Table parentTable : tables) {
            for (final Table foreignTable : tables) {
                context.getCrossReference(
                        parentTable.getTableCat(),
                        parentTable.getTableSchem(),
                        parentTable.getTableName(),
                        foreignTable.getTableCat(),
                        foreignTable.getTableSchem(),
                        foreignTable.getTableName(),
                        collection
                );
            }
        }
        return collection;
    }

    public static final String COLUMN_NAME_UPDATE_RULE = "UPDATE_RULE";

    public static final String ATTRIBUTE_NAME_UPDATE_RULE = "updateRule";

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no chidren.
    }

    // -----------------------------------------------------------------------------------------------------------------
    public UpdateRule getUpdateRuleAsEnum() {
        return UpdateRule.valueOfRawValue(getUpdateRule());
    }

    public void setUpdateRuleAsEnum(UpdateRule updateRuleAsEnum) {
        setUpdateRule(Objects.requireNonNull(updateRuleAsEnum, "updateRuleAsEnum is null").rawValueAsInt());
    }

    public ImportedKeyRule getDeleteRuleAsEnum() {
        return ImportedKeyRule.valueOfRawValue(getDeleteRule());
    }

    public void setDeleteRuleAsEnum(final ImportedKeyRule deleteRuleAsEnum) {
        setDeleteRule(Objects.requireNonNull(deleteRuleAsEnum, "deleteRuleAsEnum is null").rawValueAsInt());
    }

    public ImportedKeyDeferrability getDeferrabilityAsEnum() {
        return ImportedKeyDeferrability.valueOfRawValue(getDeferrability());
    }

    public void setDeferrabilityAsEnum(final ImportedKeyDeferrability deferrabilityAsEnum) {
        setDeferrability(Objects.requireNonNull(deferrabilityAsEnum, "deferrabilityAsEnum is null").rawValueAsInt());
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
    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PKTABLE_CAT")
    private String pktableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PKTABLE_SCHEM")
    private String pktableSchem;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label("PKTABLE_NAME")
    private String pktableName;

    @XmlElement(nillable = false, required = true)
    @NotNull
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
    @NotNull
    @Label("FKTABLE_NAME")
    private String fktableName;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label("FKCOLUMN_NAME")
    private String fkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @Positive
    @XmlElement(nillable = false, required = true)
    @Positive
    @Label("FKCOLUMN_NAME")
    private int keySeq;

    @XmlElement(nillable = false, required = true)
    @Label("UPDATE_RULE")
    private int updateRule;

    @XmlElement(nillable = false, required = true)
    @Label("DELETE_RULE")
    private int deleteRule;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("FK_NAME")
    private String fkName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PK_NAME")
    private String pkName;

    @XmlElement(nillable = false, required = true)
    @Positive
    @Label("DEFERRABILITY")
    private int deferrability;
}
