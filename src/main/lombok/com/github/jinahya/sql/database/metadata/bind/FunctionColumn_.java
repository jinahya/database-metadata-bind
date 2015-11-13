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


import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


@XmlTransient
@XmlType(
    propOrder = {
        "functionName", "columnName", "columnType", "dataType", "typeName",
        "precision", "length", "scale", "radix", "nullable", "remarks",
        "charOctetLength", "ordinalPosition", "isNullable", "specificName",
        // --------------------------------------------------------------------
        "unknownColumns"
    }
)
class FunctionColumn_ extends AbstractChild<Function> {


    @Override
    public String toString() {

        return super.toString() + "{"
               + "functionCat=" + functionCat
               + ", functionSchem=" + functionSchem
               + ", functionName=" + functionName
               + ", columnName=" + columnName
               + ", columnType=" + columnType
               + ", dataType=" + dataType
               + ", typeName=" + typeName
               + ", precision=" + precision
               + ", length=" + length
               + ", scale=" + scale
               + ", radix=" + radix
               + ", nullable=" + nullable
               + ", remarks=" + remarks
               + ", charOctetLength=" + charOctetLength
               + ", ordinalPosition=" + ordinalPosition
               + ", isNullable=" + isNullable
               + ", specificName=" + specificName
               + "}";
    }


    @lombok.Getter @lombok.Setter
    @Label("FUNCTION_CAT")
    @NillableBySpecification
    @XmlAttribute
    private String functionCat;


    @lombok.Getter @lombok.Setter
    @Label("FUNCTION_SCHEM")
    @NillableBySpecification
    @XmlAttribute
    private String functionSchem;


    @lombok.Getter @lombok.Setter
    @Label("FUNCTION_NAME")
    @XmlAttribute
    private String functionName;


    @lombok.Getter @lombok.Setter
    @Label("COLUMN_NAME")
    @XmlElement(required = true)
    private String columnName;


    @lombok.Getter @lombok.Setter
    @Label("COLUMN_TYPE")
    @XmlElement(required = true)
    private short columnType;


    @lombok.Getter @lombok.Setter
    @Label("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;


    @lombok.Getter @lombok.Setter
    @Label("TYPE_NAME")
    @XmlElement(required = true)
    private String typeName;


    @lombok.Getter @lombok.Setter
    @Label("PRECISION")
    @XmlElement(required = true)
    private int precision;


    @lombok.Getter @lombok.Setter
    @Label("LENGTH")
    @XmlElement(required = true)
    private int length;


    @lombok.Getter @lombok.Setter
    @Label("SCALE")
    @XmlElement(required = true)
    private Short scale;


    @lombok.Getter @lombok.Setter
    @Label("RADIX")
    @XmlElement(required = true)
    private short radix;


    @lombok.Getter @lombok.Setter
    @Label("NULLABLE")
    @XmlElement(required = true)
    private short nullable;


    @lombok.Getter @lombok.Setter
    @Label("REMARKS")
    @XmlElement(required = true)
    private String remarks;


    @lombok.Getter @lombok.Setter
    @Label("CHAR_OCTET_LENGTH")
    @XmlElement(required = true)
    @NillableBySpecification
    private Integer charOctetLength;


    @lombok.Getter @lombok.Setter
    @Label("ORDINAL_POSITION")
    @XmlElement(required = true)
    private int ordinalPosition;


    @lombok.Getter @lombok.Setter
    @Label("IS_NULLABLE")
    @XmlElement(required = true)
    private String isNullable;


    @lombok.Getter @lombok.Setter
    @Label("SPECIFIC_NAME")
    @XmlElement(required = true)
    private String specificName;


    @XmlElement(name = "unknownColumn", nillable = true)
    private List<UnknownColumn> unknownColumns;


}

