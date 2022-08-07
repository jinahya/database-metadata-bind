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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An abstract class for binding results of {@link java.sql.DatabaseMetaData#getExportedKeys(String, String, String)}
 * method and {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)} method.
 *
 * @see ImportedKey
 * @see ExportedKey
 */
@XmlTransient
@Data
abstract class TableKey implements MetadataType, HasUpdateRule, HasDeleteRule, HasDeferrability {

    private static final long serialVersionUID = 6713872409315471232L;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PKTABLE_CAT")
    private String pktableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PKTABLE_SCHEM")
    private String pktableSchem;

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
    @XmlTransient
    @Valid
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Table table;
}
