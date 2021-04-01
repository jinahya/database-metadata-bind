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
 */
@XmlRootElement
public class Schema implements MetadataType {

    private static final long serialVersionUID = 7457236468401244963L;

    // ---------------------------------------------------------------------------------------- TABLE_SCHEM / tableSchem
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String ATTRIBUTE_NAME_TABLE_SCHEM = "tableSchem";

    // ------------------------------------------------------------------------------------ TABLE_CATALOG / tableCatalog
    public static final String COLUMN_LABEL_TABLE_CATALOG = "TABLE_CATALOG";

    public static final String ATTRIBUTE_NAME_TABLE_CATALOG = "tableCatalog";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new virtual instance with following property values.
     *
     * @param tableCatalog a value for {@code tableCatalog} property.
     * @param tableSchem   a value for {@code tableSchem} property.
     * @return a new virtual instance.
     * @see Catalog#newVirtualInstance()
     */
    static Schema newVirtualInstance(final String tableCatalog, final String tableSchem) {
        final Schema instance = new Schema();
        instance.virtual = Boolean.TRUE;
        instance.tableCatalog = tableCatalog;
        instance.tableSchem = tableSchem;
        return instance;
    }

    /**
     * Creates a new virtual instance with specified table catalog.
     *
     * @param tableCatalog the value for {@code tableCatalog} property.
     * @return a new virtual instance.
     * @see Catalog#newVirtualInstance()
     */
    static Schema newVirtualInstance(final String tableCatalog) {
        return newVirtualInstance(tableCatalog, "");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public Schema() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + ATTRIBUTE_NAME_TABLE_SCHEM + '=' + tableCatalog
               + ',' + ATTRIBUTE_NAME_TABLE_SCHEM + '=' + tableSchem
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Schema that = (Schema) obj;
        return Objects.equals(tableCatalog, that.tableCatalog)
               && Objects.equals(tableSchem, that.tableSchem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCatalog,
                            tableSchem);
    }

    // --------------------------------------------------------------------------------------------------------- virtual

    /**
     * Indicates whether this schema is a virtual instance.
     *
     * @return {@code true} if this schema instance is a virtual instance; {@code false} otherwise.
     */
    public boolean isVirtual() {
        return virtual != null && virtual;
    }

    // ---------------------------------------------------------------------------------------------------- tableCatalog
    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(final String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- functions
    public List<Function> getFunctions() {
        if (functions == null) {
            functions = new ArrayList<>();
        }
        return functions;
    }

    // ------------------------------------------------------------------------------------------------------ procedures
    public List<Procedure> getProcedures() {
        if (procedures == null) {
            procedures = new ArrayList<>();
        }
        return procedures;
    }

    // ---------------------------------------------------------------------------------------------------------- tables

    /**
     * Returns tables of this schema.
     *
     * @return a list of table of this schema.
     */
    public @NotNull List<@Valid @NotNull Table> getTables() {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        return tables;
    }

    // ------------------------------------------------------------------------------------------------------------ UDTs

    /**
     * Returns user defined types of this schema.
     *
     * @return a list of user defined types of this schema.
     */
    public @NotNull List<@Valid @NotNull UDT> getUDTs() {
        if (UDTs == null) {
            UDTs = new ArrayList<>();
        }
        return UDTs;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute
    private Boolean virtual;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "token")
    @MayBeNull
    @Label(COLUMN_LABEL_TABLE_CATALOG)
    private String tableCatalog;

    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    @NotNull
    @Label(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull Function> functions;

    @XmlElementRef
    private List<@Valid @NotNull Procedure> procedures;

    @XmlElementRef
    private List<@Valid @NotNull Table> tables;

    @XmlElementRef
    private List<@Valid @NotNull UDT> UDTs;
}
