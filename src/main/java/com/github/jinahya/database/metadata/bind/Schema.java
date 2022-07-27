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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import java.util.ArrayList;
import java.util.Collection;
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
@ParentOf(Function.class)
@ParentOf(Procedure.class)
@ParentOf(Table.class)
@ParentOf(UDT.class)
@ChildOf(Catalog.class)
@Setter
@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
@NoArgsConstructor
public class Schema
        implements MetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    // ---------------------------------------------------------------------------------------- TABLE_SCHEM / tableSchem
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String ATTRIBUTE_NAME_TABLE_SCHEM = "tableSchem";

    // ------------------------------------------------------------------------------------ TABLE_CATALOG / tableCatalog
    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    public static final String ATTRIBUTE_NAME_TABLE_CATALOG = "tableCatalog";

    /**
     * Creates a new instance with specified property values.
     *
     * @param tableCatalog a value for {@code tableCatalog} property.
     * @param tableSchem   a value for {@code tableSchem} property.
     * @return a new instance of {@code tableCatalog} and {@code tableSchem}.
     */
    public static Schema of(final String tableCatalog, final String tableSchem) {
        final Schema instance = new Schema();
        instance.setTableCatalog(tableCatalog);
        instance.setTableSchem(tableSchem);
        return instance;
    }

    /**
     * Creates a new virtual instance with following property values.
     *
     * @param tableCatalog a value for {@code tableCatalog} property.
     * @return a new virtual instance.
     */
    public static Schema newVirtualInstance(final String tableCatalog) {
        Objects.requireNonNull(tableCatalog, "tableCatalog is null");
        final Schema instance = of(tableCatalog, "");
        instance.virtual = Boolean.TRUE;
        return instance;
    }

    public static Schema newVirtualInstance(final Catalog catalog) {
        Objects.requireNonNull(catalog, "catalog is null");
        return newVirtualInstance(catalog.getTableCat());
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

    @XmlAttribute(required = false)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Boolean virtual;

    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "token")
    @NullableBySpecification
    @Label(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;

    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    @NotNull
    @Label(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

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
    private List<@Valid @NotNull Table> tables;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull UDT> UDTs;
}
