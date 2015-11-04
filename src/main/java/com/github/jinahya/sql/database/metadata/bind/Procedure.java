/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
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
 */


package com.github.jinahya.sql.database.metadata.bind;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "procedureName", "remarks", "procedureType", "specificName"
    }
)
public class Procedure {


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


    // ------------------------------------------------------------------ schema
    public Schema getSchema() {

        return schema;
    }


    public void setSchema(final Schema schema) {

        this.schema = schema;
    }


    @ColumnLabel("PROCEDURE_CAT")
    @XmlAttribute
    private String procedureCat;


    @ColumnLabel("PROCEDURE_SCHEM")
    @XmlAttribute
    private String procedureSchem;


    @ColumnLabel("PROCEDURE_NAME")
    @XmlElement(required = true)
    private String procedureName;


    @ColumnLabel("REMARKS")
    @XmlElement(required = true)
    private String remarks;


    @ColumnLabel("PROCEDURE_TYPE")
    @XmlElement(required = true)
    private short procedureType;


    @ColumnLabel("SPECIFIC_NAME")
    @XmlElement(required = true)
    private String specificName;


    /**
     * parent schema.
     */
    @XmlTransient
    private Schema schema;


}

