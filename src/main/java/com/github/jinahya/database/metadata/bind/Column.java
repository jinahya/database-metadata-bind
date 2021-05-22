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
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumns(String, String, String, String, Collection)
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class Column implements MetadataType {

    private static final long serialVersionUID = -409653682729081530L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Constants for {@code NULLABLE} column values from {@link DatabaseMetaData#getColumns(String, String, String,
     * String)}.
     *
     * @see DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public enum Nullable implements IntFieldEnum<Nullable> {

        /**
         * Constant for {@link DatabaseMetaData#columnNoNulls}({@value java.sql.DatabaseMetaData#columnNoNulls}).
         */
        COLUMN_NO_NULLS(DatabaseMetaData.columnNoNulls),

        /**
         * Constant for {@link DatabaseMetaData#columnNullable}({@value java.sql.DatabaseMetaData#columnNullable}).
         */
        COLUMN_NULLABLE(DatabaseMetaData.columnNullable),

        /**
         * Constant for {@link DatabaseMetaData#columnNullableUnknown}({@value java.sql.DatabaseMetaData#columnNullableUnknown}).
         */
        COLUMN_NULLABLE_UNKNOWN(DatabaseMetaData.columnNullableUnknown);

        /**
         * Returns the constant whose raw value equals to given. An {@link IllegalArgumentException} will be thrown if
         * no constants matches.
         *
         * @param rawValue the value value
         * @return the constant whose raw value equals to given.
         */
        public static Nullable valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Nullable.class, rawValue);
        }

        Nullable(final int rawValue) {
            this.rawValue = rawValue;
        }

        @Override
        public int getRawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_NAME_IS_AUTOINCREMENT = "IS_AUTOINCREMENT";

    public static final String ATTRIBUTE_NAME_IS_AUTOINCREMENT = "isAutoincrement";

    /**
     * Constants for {@value #COLUMN_NAME_IS_AUTOINCREMENT} colum value from the result of {@link
     * DatabaseMetaData#getColumns(String, String, String, String)}.
     */
    public enum IsAutoincrement implements FieldEnum<IsAutoincrement, String> {

        YES("YES"),

        NO("NO"),

        UNKNOWN("");

        /**
         * Returns the constant whose {@link #getRawValue() rawValue} matches to specified value.
         *
         * @param rawValue the value for {@link #getRawValue() rawValue} to match.
         * @return the constant whose {@link #getRawValue() rawValue} matches to {@code rawValue}.
         */
        public static IsAutoincrement valueOfRawValue(final String rawValue) {
            return FieldEnums.valueOfRawValue(IsAutoincrement.class, rawValue);
        }

        IsAutoincrement(final String rawValue) {
            this.rawValue = Objects.requireNonNull(rawValue, "rawValue is null");
        }

        @Override
        public String getRawValue() {
            return rawValue;
        }

        private final String rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_NAME_IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    public static final String ATTRIBUTE_NAME_IS_GENERATEDCOLUMN = "isGeneratedcolumn";

    /**
     * Constants for {@value #COLUMN_NAME_IS_GENERATEDCOLUMN} colum value from the result of {@link
     * DatabaseMetaData#getColumns(String, String, String, String)}.
     */
    public enum IsGeneratedcolumn implements FieldEnum<IsGeneratedcolumn, String> {

        YES("YES"),

        NO("NO"),

        UNKNOWN("");

        /**
         * Returns the constant whose {@link #getRawValue() rawValue} matches to specified value.
         *
         * @param rawValue the value for {@link #getRawValue() rawValue} to match.
         * @return the constant whose {@link #getRawValue() rawValue} matches to {@code rawValue}.
         */
        public static IsGeneratedcolumn valueOfRawValue(final String rawValue) {
            return FieldEnums.valueOfRawValue(IsGeneratedcolumn.class, rawValue);
        }

        IsGeneratedcolumn(final String rawValue) {
            this.rawValue = Objects.requireNonNull(rawValue, "rawValue is null");
        }

        @Override
        public String getRawValue() {
            return rawValue;
        }

        private final String rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(required = true)
    @Label("TABLE_NAME")
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true)
    @Label("COLUMN_SIZE")
    private int columnSize;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("BUFFER_LENGTH")
    private Integer bufferLength;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(required = true)
    @Label("NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement(required = true)
    @Label("NULLABLE")
    private int nullable;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("COLUMN_DEF")
    private String columnDef;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(required = true)
    @Label("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement(required = true)
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(required = true)
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCOPE_CATALOG")
    private String scopeCatalog;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCOPE_SCHEMA")
    private String scopeSchema;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCOPE_TABLE")
    private String scopeTable;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SOURCE_DATA_TYPE")
    private Short sourceDataType;

    @XmlElement(required = true)
    @Label(COLUMN_NAME_IS_AUTOINCREMENT)
    private String isAutoincrement;

    @XmlElement(required = true)
    @Label(COLUMN_NAME_IS_GENERATEDCOLUMN)
    private String isGeneratedcolumn;
}
