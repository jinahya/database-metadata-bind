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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@ParentOf(ProcedureColumn.class)
@ChildOf(Schema.class)
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Procedure extends AbstractMetadataType {

    private static final long serialVersionUID = -6262056388403934829L;

    public static final Comparator<Procedure> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Procedure::getSchemaId, SchemaId.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Procedure::getProcedureName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Procedure::getSpecificName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    public static final Comparator<Procedure> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Procedure::getSchemaId, SchemaId.LEXICOGRAPHIC_ORDER)
                    .thenComparing(Procedure::getProcedureName, nullsFirst(naturalOrder()))
                    .thenComparing(Procedure::getSpecificName, nullsFirst(naturalOrder()));

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Procedure)) return false;
        final Procedure that = (Procedure) obj;
        return Objects.equals(procedureCat, that.procedureCat) &&
               Objects.equals(procedureSchem, that.procedureSchem) &&
               Objects.equals(procedureName, that.procedureName) &&
               Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                procedureCat,
                procedureSchem,
                procedureName,
                specificName
        );
    }

    // ---------------------------------------------------------------------------------------------------- procedureCat
    String getProcedureCatNonNull() {
        return Optional.ofNullable(getProcedureCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
        procedureId = null;
    }

    // -------------------------------------------------------------------------------------------------- procedureSchem
    String getProcedureSchemNonNull() {
        return Optional.ofNullable(getProcedureSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
        procedureId = null;
    }

    // ---------------------------------------------------------------------------------------------------- specificName
    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
        procedureId = null;
    }

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
    public ProcedureId getProcedureId() {
        if (procedureId == null) {
            procedureId = ProcedureId.of(
                    getProcedureCatNonNull(),
                    getProcedureSchemNonNull(),
                    getSpecificName()
            );
        }
        return procedureId;
    }

    private SchemaId getSchemaId() {
        return getProcedureId().getSchemaId();
    }

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient ProcedureId procedureId;
}
