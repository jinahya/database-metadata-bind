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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * .
 *
 * @see ImportedKey
 * @see ExportedKey
 */
@XmlTransient
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
abstract class TableKey implements MetadataType {

    private static final long serialVersionUID = 6713872409315471232L;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("PKTABLE_CAT")
    private String pktableCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("PKTABLE_SCHEM")
    private String pktableSchem;

    @XmlElement(required = true)
    @Label("PKTABLE_NAME")
    private String pktableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("PKCOLUMN_NAME")
    private String pkcolumnName;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("FKTABLE_CAT")
    private String fktableCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("FKTABLE_NAME")
    private String fktableSchem;

    @XmlElement(required = true)
    @Label("FKTABLE_NAME")
    private String fktableName;

    @XmlElement(required = true)
    @Label("FKCOLUMN_NAME")
    private String fkcolumnName;

    @XmlElement(required = true)
    @Label("FKCOLUMN_NAME")
    private short keySeq;

    @XmlElement(required = true)
    @Label("UPDATE_RULE")
    private short updateRule;

    @XmlElement(required = true)
    @Label("DELETE_RULE")
    private short deleteRule;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("FK_NAME")
    private String fkName;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("PK_NAME")
    private String pkName;

    @XmlElement(required = true)
    @Label("DEFERRABILITY")
    private short deferrability;
}
