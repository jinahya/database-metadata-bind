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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
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
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getProcedures(String, String, String, Collection)
 */
@XmlRootElement
@ChildOf(Schema.class)
@ParentOf(ProcedureColumn.class)
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Procedure implements MetadataType {

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
                    getProcedureColumns()
            );
            for (final ProcedureColumn procedureColumn : getProcedureColumns()) {
                procedureColumn.retrieveChildren(context);
            }
        }
    }

    public List<ProcedureColumn> getProcedureColumns() {
        if (procedureColumns == null) {
            procedureColumns = new ArrayList<>();
        }
        return procedureColumns;
    }

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PROCEDURE_CAT")
    private String procedureCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("PROCEDURE_SCHEM")
    private String procedureSchem;

    @XmlElement(nillable = false, required = true)
    @Label("PROCEDURE_NAME")
    private String procedureName;

    @XmlElement(nillable = true, required = true)
    @NullableByVendor("HSQL")
    @Label("REMARKS")
    private String remarks;

    @XmlElement(nillable = false, required = true)
    @Label("PROCEDURE_TYPE")
    private short procedureType;

    @XmlElement(nillable = false, required = true)
    @Label("SPECIFIC_NAME")
    private String specificName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlTransient
    @Valid
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Schema schema;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull ProcedureColumn> procedureColumns;
}
