/*
 * Copyright 2011 Jin Kwon <jinahya at gmail.com>.
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


import java.util.Comparator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "scope", "columnName", "dataType", "typeName", "columnSize",
        "bufferLength", "decimalDigits", "pseudoColumn"
    }
)
public class BestRowIdentifier extends AbstractChild<Table> {


    public static Comparator<BestRowIdentifier> natural() {

        return new Comparator<BestRowIdentifier>() {

            @Override
            public int compare(final BestRowIdentifier o1,
                               final BestRowIdentifier o2) {

                // They are ordered by SCOPE.
                return o1.getScope() - o2.getScope();
            }

        };
    }


    @Override
    public String toString() {

        if (true) {
            return new ToStringBuilder(this)
                .append("scope", getScope())
                .append("columnName", getColumnName())
                .append("dataType", getDataType())
                .append("typeName", getTypeName())
                .append("columnSize", getColumnSize())
                .append("bufferLength", getBufferLength())
                .append("decimalDigits", getDecimalDigits())
                .append("pseudoColumn", getPseudoColumn())
                .build();
        }

        return super.toString() + "{"
               + "scope=" + getScope()
               + ", columnName=" + columnName
               + ", dataType=" + dataType
               + ", typeName=" + typeName
               + ", columnSize=" + columnSize
               + ", bufferLength=" + bufferLength
               + ", decimalDigits=" + decimalDigits
               + ", pseudoColumn=" + pseudoColumn
               + "}";
    }


    // ------------------------------------------------------------------- scope
    public short getScope() {

        return scope;
    }


    public void setScope(final short scope) {

        this.scope = scope;
    }


    // -------------------------------------------------------------- columnName
    public String getColumnName() {

        return columnName;
    }


    public void setColumnName(final String columnName) {

        this.columnName = columnName;
    }


    // ---------------------------------------------------------------- dataType
    public int getDataType() {

        return dataType;
    }


    public void setDataType(final int dataType) {

        this.dataType = dataType;
    }


    // ---------------------------------------------------------------- typeName
    public String getTypeName() {

        return typeName;
    }


    public void setTypeName(final String typeName) {

        this.typeName = typeName;
    }


    // -------------------------------------------------------------- columnSize
    public int getColumnSize() {

        return columnSize;
    }


    public void setColumnSize(final int columnSize) {

        this.columnSize = columnSize;
    }


    // ------------------------------------------------------------ bufferLength
    public Integer getBufferLength() {

        return bufferLength;
    }


    public void setBufferLength(final Integer bufferLength) {

        this.bufferLength = bufferLength;
    }


    // ----------------------------------------------------------- decimalDigits
    public Short getDecimalDigits() {

        return decimalDigits;
    }


    public void setDecimalDigits(final Short decimalDigits) {

        this.decimalDigits = decimalDigits;
    }


    // ------------------------------------------------------------ pseudoColumn
    public short getPseudoColumn() {

        return pseudoColumn;
    }


    public void setPseudoColumn(final short pseudoColumn) {

        this.pseudoColumn = pseudoColumn;
    }


    // ------------------------------------------------------------------- table
    // just for class diagram
    private Table getTable() {

        return getParent();
    }


//    public void setTable(final Table table) {
//
//        setParent(table);
//    }
    // -------------------------------------------------------------------------
    @Label("SCOPE")
    @XmlElement(required = true)
    private short scope;


    @Label("COLUMN_NAME")
    @XmlElement(required = true)
    private String columnName;


    @Label("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;


    @Label("TYPE_NAME")
    @XmlElement(required = true)
    private String typeName;


    @Label("COLUMN_SIZE")
    @XmlElement(required = true)
    private int columnSize;


    @Label("BUFFER_LENGTH")
    @Unused
    @XmlElement(nillable = true, required = true)
    private Integer bufferLength;


    @Label("DECIMAL_DIGITS")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private Short decimalDigits;


    @Label("PSEUDO_COLUMN")
    @XmlElement(required = true)
    private short pseudoColumn;

}

