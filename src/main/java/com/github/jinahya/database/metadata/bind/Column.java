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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumns(String, String, String, String, Collection)
 */
@XmlRootElement
@ChildOf(Table.class)
@Data
public class Column implements MetadataType {

    private static final long serialVersionUID = -409653682729081530L;

    /**
     * Constants for {@code NULLABLE} column values from
     * {@link DatabaseMetaData#getColumns(String, String, String, String)}.
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
         * Constant for
         * {@link DatabaseMetaData#columnNullableUnknown}({@value java.sql.DatabaseMetaData#columnNullableUnknown}).
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
        public int rawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    public static final String COLUMN_NAME_IS_AUTOINCREMENT = "IS_AUTOINCREMENT";

    public static final String COLUMN_NAME_IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    public Nullable getNullableAsEnum() {
        return Nullable.valueOfRawValue(getNullable());
    }

    public void setNullableAsEnum(final Nullable nullableAsEnum) {
        setNullable(Objects.requireNonNull(nullableAsEnum, "nullableAsEnum is null").rawValue());
    }

    public List<ColumnPrivilege> getColumnPrivileges() {
        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<>();
        }
        return columnPrivileges;
    }

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(nillable = false, required = true)
    @Label("TABLE_NAME")
    private String tableName;

    @XmlElement(nillable = false, required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = false, required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = false, required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("COLUMN_SIZE")
    private Integer columnSize;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("BUFFER_LENGTH")
    private Integer bufferLength;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(nillable = false, required = true)
    @Label("NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement(nillable = true, required = true)
    @Label("NULLABLE")
    private int nullable;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("REMARKS")
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("COLUMN_DEF")
    private String columnDef;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(nillable = false, required = true)
    @Label("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement(nillable = false, required = true)
    @Positive
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SCOPE_CATALOG")
    private String scopeCatalog;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SCOPE_SCHEMA")
    private String scopeSchema;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SCOPE_TABLE")
    private String scopeTable;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SOURCE_DATA_TYPE")
    private Integer sourceDataType;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label(COLUMN_NAME_IS_AUTOINCREMENT)
    private String isAutoincrement;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label(COLUMN_NAME_IS_GENERATEDCOLUMN)
    private String isGeneratedcolumn;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull ColumnPrivilege> columnPrivileges;
}
