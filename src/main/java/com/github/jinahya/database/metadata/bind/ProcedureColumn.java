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
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String,
 * java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
//@ChildOf(Procedure.class)
//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class ProcedureColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = 3894753719381358829L;

    public static final Comparator<ProcedureColumn> COMPARING_PROCEDURE_CAT_PROCEDURE_SCHEM_PROCEDURE_NAME_SPECIFIC_NAME
            = Comparator.comparing(ProcedureColumn::getProcedureCat, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(ProcedureColumn::getProcedureSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(ProcedureColumn::getProcedureName)
            .thenComparing(ProcedureColumn::getSpecificName);

    public ProcedureColumnId getProcedureColumnId() {
        return ProcedureColumnId.builder()
                .procedureId(
                        ProcedureId.builder()
                                .schemaId(
                                        SchemaId.builder()
                                                .catalogId(CatalogId.builder().tableCat(getProcedureCat()).build())
                                                .tableSchem(getProcedureSchem()).build())
                                .specificName(getSpecificName()).build())
                .columnName(getColumnName())
                .build();
    }

    @NullableBySpecification
    @ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

    @NullableBySpecification
    @ColumnLabel("PROCEDURE_SCHEM")
    private String procedureSchem;

    @ColumnLabel("PROCEDURE_NAME")
    private String procedureName;

    @ColumnLabel("COLUMN_NAME")
    private String columnName;

    @ColumnLabel("COLUMN_TYPE")
    private int columnType;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @NullableBySpecification
    @ColumnLabel("PRECISION")
    private Integer precision;

    @ColumnLabel("LENGTH")
    private int length;

    // https://issues.apache.org/jira/browse/DERBY-7103
    @NullableBySpecification
    @ColumnLabel("SCALE")
    private Integer scale;

    @ColumnLabel("RADIX")
    private int radix;

    @ColumnLabel("NULLABLE")
    private int nullable;

    @NullableByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7101
    @ColumnLabel("REMARKS")
    private String remarks;

    @NullableBySpecification
    @ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @Reserved
    @ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @Reserved
    @ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @NullableBySpecification
    @ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @ColumnLabel("SPECIFIC_NAME")
    private String specificName;
}
