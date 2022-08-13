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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String,
 * java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getProcedureColumns(String, String, String, String, Collection)
 */
@XmlRootElement
//@ChildOf__(Procedure.class)
@Data
public class ProcedureColumn
        implements MetadataType,
                   ChildOf<Procedure> {

    private static final long serialVersionUID = 3894753719381358829L;

    public static final Comparator<ProcedureColumn> COMPARATOR =
            Comparator.comparing(ProcedureColumn::extractParent, Procedure.COMPARATOR);

    /**
     * Constants for column types of procedure columns.
     *
     * @see DatabaseMetaData#getProcedureColumns(String, String, String, String)
     */
    public enum ColumnType implements IntFieldEnum<ColumnType> {

        /**
         * Constant for
         * {@link DatabaseMetaData#procedureColumnUnknown}({@value java.sql.DatabaseMetaData#procedureColumnUnknown}).
         */
        PROCEDURE_COLUMN_UNKNOWN(DatabaseMetaData.procedureColumnUnknown), // 0

        /**
         * Constants for
         * {@link DatabaseMetaData#procedureColumnIn}({@value java.sql.DatabaseMetaData#procedureColumnIn}).
         */
        PROCEDURE_COLUMN_IN(DatabaseMetaData.procedureColumnIn), // 1

        /**
         * Constants for
         * {@link DatabaseMetaData#procedureColumnInOut}({@value java.sql.DatabaseMetaData#procedureColumnInOut}).
         */
        PROCEDURE_COLUMN_IN_OUT(DatabaseMetaData.procedureColumnInOut), // 2

        /**
         * Constants for
         * {@link DatabaseMetaData#procedureColumnResult}({@value java.sql.DatabaseMetaData#procedureColumnResult}).
         */
        PROCEDURE_COLUMN_RESULT(DatabaseMetaData.functionColumnResult), // 3

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnOut}({@value DatabaseMetaData#procedureColumnOut}).
         */
        PROCEDURE_COLUMN_OUT(DatabaseMetaData.procedureColumnOut), // 4

        /**
         * Constant for
         * {@link DatabaseMetaData#procedureColumnReturn}({@value java.sql.DatabaseMetaData#procedureColumnReturn}).
         */
        PROCEDURE_COLUMN_RETURN(DatabaseMetaData.procedureColumnReturn); // 5

        /**
         * Returns the constant whose raw value equals to specified raw value. An instance of
         * {@link IllegalArgumentException} will be thrown if no constant matches.
         *
         * @param rawValue the raw value.
         * @return the constant whose raw value equals to {@code rawValue}.
         */
        public static ColumnType valueOfRawValue(final int rawValue) {
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
        public int rawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    @Override
    public Procedure extractParent() {
        return Procedure.builder()
                .procedureCat(getProcedureCat())
                .procedureSchem(getProcedureSchem())
                .procedureName(getProcedureName())
                .build();
    }

    public ColumnType getColumnTypeAsEnum() {
        return ColumnType.valueOfRawValue(getColumnType());
    }

    public void setColumnTypeAsEnum(final ColumnType columnTypeAsEnum) {
        Objects.requireNonNull(columnTypeAsEnum, "columnTypeAsEnum is null");
        setColumnType(columnTypeAsEnum.rawValue);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("PROCEDURE_CAT")
    private String procedureCat;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("PROCEDURE_SCHEM")
    private String procedureSchem;

    @XmlElement(required = true)
    @Label("PROCEDURE_NAME")
    private String procedureName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = false, required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(nillable = false, required = true)
    @Label("COLUMN_TYPE")
    private int columnType;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PRECISION")
    private Integer precision;

    @XmlElement(nillable = false, required = true)
    @Label("LENGTH")
    private int length;

    // https://issues.apache.org/jira/browse/DERBY-7103
    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SCALE")
    private Integer scale;

    @XmlElement(nillable = false, required = true)
    @Label("RADIX")
    private int radix;

    @XmlElement(nillable = false, required = true)
    @Label("NULLABLE")
    private int nullable;

    @XmlElement(required = true, nillable = true)
    @NullableByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7101
    @Label("REMARKS")
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
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

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @XmlElement(required = true)
    @Positive // > A value of 0 is returned if this row describes the procedure's return value.
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label("SPECIFIC_NAME")
    private String specificName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlTransient
    @Valid
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Procedure procedure;
}
