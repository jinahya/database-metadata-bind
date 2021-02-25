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

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getSchemas(java.lang.String,
 * java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class Schema extends AbstractChild<Catalog> {

    private static final long serialVersionUID = 7457236468401244963L;

    // ---------------------------------------------------------------------------------------- TABLE_SCHEM / tableSchem
    public static final String COLUMN_NAME_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String ATTRIBUTE_NAME_TABLE_SCHEM = "tableSchem";

    // ------------------------------------------------------------------------------------ TABLE_CATALOG / tableCatalog
    public static final String COLUMN_NAME_TABLE_CATALOG = "TABLE_CATALOG";

    public static final String ATTRIBUTE_NAME_TABLE_CATALOG = "tableCatalog";

    // -----------------------------------------------------------------------------------------------------------------
    public static Schema newVirtualInstance(final Catalog catalog) {
        final Schema instance = new Schema();
        instance.virtual = Boolean.TRUE;
        instance.setParent(catalog);
        instance.setTableSchem("");
        return instance;
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
     * Indicates whether this instance is a virtual instance.
     *
     * @return {@code true} if this instance is a virtual instance; {@code false} otherwise.
     */
    public boolean isVirtual() {
        return virtual != null && virtual;
    }

    // --------------------------------------------------------------------------------------------------------- catalog
    public Catalog getCatalog() {
        return getParent();
    }

    void setCatalog(final Catalog catalog) {
        setParent(catalog);
    }

    // ---------------------------------------------------------------------------------------------------- tableCatalog
    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(final String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    Schema tableCatalog(final String tableCatalog) {
        setTableCatalog(tableCatalog);
        return this;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    Schema tableSchem(final String tableSchem) {
        setTableSchem(tableSchem);
        return this;
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
    public List<Table> getTables() {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        return tables;
    }

    // ------------------------------------------------------------------------------------------------------------ UDTs
    public List<UDT> getUDTs() {
        if (UDTs == null) {
            UDTs = new ArrayList<>();
        }
        return UDTs;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonbProperty(nillable = true)
    @XmlAttribute(required = false)
    Boolean virtual = Boolean.FALSE;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonbProperty(nillable = true)
    @XmlAttribute(required = true)
    @MayBeNull
    @Label(COLUMN_NAME_TABLE_CATALOG)
    @Bind(label = COLUMN_NAME_TABLE_CATALOG, nillable = true)
    private String tableCatalog;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement
    @NotNull
    @Label(COLUMN_NAME_TABLE_SCHEM)
    @Bind(label = COLUMN_NAME_TABLE_SCHEM)
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
