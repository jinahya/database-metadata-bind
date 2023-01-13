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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ParentOf(ProcedureColumn.class)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Procedure
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = -6262056388403934829L;

    public static final Comparator<Procedure> COMPARATOR =
            Comparator.comparing(Procedure::getProcedureCat, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Procedure::getProcedureSchem, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Procedure::getProcedureName)
                    .thenComparing(Procedure::getSpecificName);

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        {
            context.getProcedureColumns(
                    getProcedureCat(),
                    getProcedureSchem(),
                    getProcedureName(),
                    "%",
                    getProcedureColumns()::add
            );
            for (final ProcedureColumn procedureColumn : getProcedureColumns()) {
                procedureColumn.retrieveChildren(context);
            }
        }
    }

    @Override
    public Schema extractParent() {
        return Schema.builder()
                .tableCatalog(getProcedureCat())
                .tableSchem(getProcedureSchem())
                .build();
    }

    public List<ProcedureColumn> getProcedureColumns() {
        if (procedureColumns == null) {
            procedureColumns = new ArrayList<>();
        }
        return procedureColumns;
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
    private short procedureType;

    @ColumnLabel("SPECIFIC_NAME")
    private String specificName;

    // -----------------------------------------------------------------------------------------------------------------
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ProcedureColumn> procedureColumns;
}
