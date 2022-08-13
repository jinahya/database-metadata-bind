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
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding a result of {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSchemas(Collection)
 * @see Context#getSchemas(String, String, Collection)
 */
@XmlRootElement
@ParentOf(UDT.class)
@ParentOf(Table.class)
@ParentOf(Procedure.class)
@ParentOf(Function.class)
@ChildOf__(Catalog.class)
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Schema
        implements MetadataType,
                   ChildOf<Catalog> {

    private static final long serialVersionUID = 7457236468401244963L;

    public static final Comparator<Schema> COMPARATOR =
            Comparator.comparing(Schema::extractParent, Comparator.nullsFirst(Catalog.COMPARATOR))
                    .thenComparing(Schema::getTableSchem);

    public static final String LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    public static final String LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String VALUE_TABLE_SCHEM_EMPTY = "";

    /**
     * Creates a new <em>virtual</em> instance with specified {@code tableCat} property value.
     *
     * @param tableCatalog the value for {@code tableCatalog} property.
     * @return a new <em>virtual</em> instance.
     */
    public static Schema newVirtualInstance(final String tableCatalog) {
        Objects.requireNonNull(tableCatalog, "tableCatalog is null");
        return builder()
                .virtual(Boolean.TRUE)
                .tableCatalog(tableCatalog)
                .tableSchem(VALUE_TABLE_SCHEM_EMPTY)
                .build();
    }

    /**
     * Creates a new <em>virtual</em> instance whose {@code tableCat} property is {@value Catalog#VALUE_TABLE_CAT_EMPTY}
     * and {@code tableSchem} property is {@value #VALUE_TABLE_SCHEM_EMPTY}.
     *
     * @return a new <em>virtual</em> instance.
     */
    public static Schema newVirtualInstance() {
        return newVirtualInstance(Catalog.VALUE_TABLE_CAT_EMPTY);
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        {
            context.getFunctions(getTableCatalog(), getTableSchem(), "%", getFunctions());
            for (final Function function : getFunctions()) {
                function.retrieveChildren(context);
            }
        }
        {
            context.getProcedures(getTableCatalog(), getTableSchem(), "%", getProcedures());
            for (final Procedure procedure : getProcedures()) {
                procedure.retrieveChildren(context);
            }
        }
        {
            context.getSuperTables(getTableCatalog(), getTableSchem(), "%", getSuperTables());
            for (final SuperTable superTable : getSuperTables()) {
                superTable.retrieveChildren(context);
            }
        }
        {
            context.getSuperTypes(getTableCatalog(), getTableSchem(), "%", getSuperTypes());
            for (final SuperType superType : getSuperTypes()) {
                superType.retrieveChildren(context);
            }
        }
        {
            context.getTables(getTableCatalog(), getTableSchem(), "%", null, getTables());
            for (final Table table : getTables()) {
                table.retrieveChildren(context);
            }
        }
        {
            context.getUDTs(getTableCatalog(), getTableSchem(), "%", null, getUDTs());
            for (final UDT udt : getUDTs()) {
                udt.retrieveChildren(context);
            }
        }
    }

    @Override
    public Catalog extractParent() {
        return Catalog.builder()
                .tableCat(getTableCatalog())
                .build();
    }

    public boolean isVirtual() {
        return virtual != null && virtual;
    }

    public List<Function> getFunctions() {
        if (functions == null) {
            functions = new ArrayList<>();
        }
        return functions;
    }

    public List<Procedure> getProcedures() {
        if (procedures == null) {
            procedures = new ArrayList<>();
        }
        return procedures;
    }

    public List<SuperTable> getSuperTables() {
        if (superTables == null) {
            superTables = new ArrayList<>();
        }
        return superTables;
    }

    public List<SuperType> getSuperTypes() {
        if (superTypes == null) {
            superTypes = new ArrayList<>();
        }
        return superTypes;
    }

    public List<Table> getTables() {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        return tables;
    }

    public List<UDT> getUDTs() {
        if (UDTs == null) {
            UDTs = new ArrayList<>();
        }
        return UDTs;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = false)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Boolean virtual;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label(LABEL_TABLE_CATALOG)
    private String tableCatalog;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label(LABEL_TABLE_SCHEM)
    private String tableSchem;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull Function> functions;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull Procedure> procedures;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull SuperTable> superTables;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull SuperType> superTypes;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull Table> tables;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull UDT> UDTs;
}
