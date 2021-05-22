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

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Collection;

/**
 * A class for binding results of {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String,
 * java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getProcedureColumns(String, String, String, String, Collection)
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class ProcedureColumn implements MetadataType {

    private static final long serialVersionUID = 3894753719381358829L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Constants for column types of procedure columns.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public enum ColumnType implements IntFieldEnum<ColumnType> {

        /**
         * Constant for {@link DatabaseMetaData#procedureColumnUnknown}({@link DatabaseMetaData#procedureColumnUnknown}).
         */
        PROCEDURE_COLUMN_UNKNOWN(DatabaseMetaData.procedureColumnUnknown), // 0

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnIn}({@value DatabaseMetaData#procedureColumnIn}).
         */
        PROCEDURE_COLUMN_IN(DatabaseMetaData.procedureColumnIn), // 1

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnInOut}({@value DatabaseMetaData#procedureColumnInOut}).
         */
        PROCEDURE_COLUMN_IN_OUT(DatabaseMetaData.procedureColumnInOut), // 2

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnResult}({@value DatabaseMetaData#procedureColumnResult}).
         */
        PROCEDURE_COLUMN_RESULT(DatabaseMetaData.functionColumnResult), // 3

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnOut}({@value DatabaseMetaData#procedureColumnOut}).
         */
        PROCEDURE_COLUMN_OUT(DatabaseMetaData.procedureColumnOut), // 4

        /**
         * Constant for {@link DatabaseMetaData#procedureColumnReturn}({@value DatabaseMetaData#procedureColumnReturn}).
         */
        PROCEDURE_COLUMN_RETURN(DatabaseMetaData.procedureColumnReturn); // 5

        /**
         * Returns the constant whose raw value equals to given. An instance of {@link IllegalArgumentException} will be
         * thrown if no constant matches.
         *
         * @param rawValue the raw value
         * @return the constant whose raw value equals to given.
         */
        public static ColumnType valueOf(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(ColumnType.class, rawValue);
        }

        ColumnType(final int rawValue) {
            this.rawValue = rawValue;
        }

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("PROCEDURE_CAT")
    private String procedureCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("PROCEDURE_SCHEM")
    private String procedureSchem;

    @XmlElement(required = true)
    @Label("PROCEDURE_NAME")
    private String procedureName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true)
    @Label("COLUMN_TYPE")
    private short columnType;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true)
    @Label("PRECISION")
    private int precision;

    @XmlElement(required = true)
    @Label("LENGTH")
    private int length;

    // https://issues.apache.org/jira/browse/DERBY-7103
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCALE")
    private Short scale;

    @XmlElement(required = true)
    @Label("RADIX")
    private short radix;

    @XmlElement(required = true)
    @Label("NULLABLE")
    private short nullable;

    @XmlElement(required = true, nillable = true)
    @MayBeNullByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7101
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("COLUMN_DEF")
    private String columnDef;

    @XmlElement(required = true, nillable = true)
    @Reserved
    @Label("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(required = true, nillable = true)
    @Reserved
    @Label("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @XmlElement(required = true)
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(required = true)
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(required = true)
    @Label("SPECIFIC_NAME")
    private String specificName;
}
