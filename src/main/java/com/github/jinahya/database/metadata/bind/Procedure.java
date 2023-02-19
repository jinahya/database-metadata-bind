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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Optional;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ParentOf(ProcedureColumn.class)
@ChildOf(Schema.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@SuperBuilder(toBuilder = true)
public class Procedure extends AbstractMetadataType {

    private static final long serialVersionUID = -6262056388403934829L;

    public static final Comparator<Procedure> CASE_INSENSITIVE_ORDER
            = Comparator.comparing(Procedure::getProcedureId, ProcedureId.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Procedure> LEXICOGRAPHIC_ORDER
            = Comparator.comparing(Procedure::getProcedureId, ProcedureId.LEXICOGRAPHIC_ORDER);

    public ProcedureId getProcedureId() {
        return ProcedureId.of(
                getProcedureCatNonNull(),
                getProcedureSchemNonNull(),
                getProcedureName(),
                getSpecificName()
        );
    }

    String getProcedureCatNonNull() {
        return Optional.ofNullable(getProcedureCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    String getProcedureSchemNonNull() {
        return Optional.ofNullable(getProcedureSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    @NullableBySpecification
    @ColumnLabel("PROCEDURE_CAT")
    private String procedureCat;

    @NullableBySpecification
    @ColumnLabel("PROCEDURE_SCHEM")
    private String procedureSchem;

    @ColumnLabel("PROCEDURE_NAME")
    private String procedureName;

    @NullableByVendor("HSQL")
    @ColumnLabel("REMARKS")
    private String remarks;

    @ColumnLabel("PROCEDURE_TYPE")
    private int procedureType;

    @ColumnLabel("SPECIFIC_NAME")
    private String specificName;
}
