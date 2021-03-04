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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getProcedures(java.lang.String,
 * java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class Procedure extends AbstractChild<Schema> {

    private static final long serialVersionUID = -6262056388403934829L;

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "procedureCat=" + procedureCat
               + ",procedureSchem=" + procedureSchem
               + ",procedureName=" + procedureName
               + ",remarks=" + remarks
               + ",procedureType=" + procedureType
               + ",specificName=" + specificName
               + '}';
    }

    // ---------------------------------------------------------------------------------------------------- procedureCat
    public String getProcedureCat() {
        return procedureCat;
    }

    public void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    // ---------------------------------------------------------- procedureSchem
    public String getProcedureSchem() {
        return procedureSchem;
    }

    public void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    // ----------------------------------------------------------- procedureName
    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    // ----------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ----------------------------------------------------------- procedureType
    public short getProcedureType() {
        return procedureType;
    }

    public void setProcedureType(final short procedureType) {
        this.procedureType = procedureType;
    }

    // ------------------------------------------------------------ specificName
    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // ------------------------------------------------------------------------------------------------ procedureColumns
    List<ProcedureColumn> getProcedureColumns() {
        if (procedureColumns == null) {
            procedureColumns = new ArrayList<>();
        }
        return procedureColumns;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute
    @MayBeNull
    @Label("PROCEDURE_CAT")
    @Bind(label = "PROCEDURE_CAT", nillable = true)
    private String procedureCat;

    @XmlAttribute
    @MayBeNull
    @Label("PROCEDURE_SCHEM")
    @Bind(label = "PROCEDURE_SCHEM", nillable = true)
    private String procedureSchem;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement
    @Label("PROCEDURE_NAME")
    @Bind(label = "PROCEDURE_NAME")
    private String procedureName;

    @XmlElement
    @Label("REMARKS")
    @Bind(label = "REMARKS")
    private String remarks;

    @XmlElement
    @Label("PROCEDURE_TYPE")
    @Bind(label = "PROCEDURE_TYPE")
    private short procedureType;

    @XmlElement
    @Label("SPECIFIC_NAME")
    @Bind(label = "SPECIFIC_NAME")
    private String specificName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull ProcedureColumn> procedureColumns;
}
