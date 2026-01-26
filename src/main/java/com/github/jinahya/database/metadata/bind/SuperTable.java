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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A class for binding results of the
 * {@link java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)}
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getSuperTables(String, String, String)
 */

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SuperTable
        extends AbstractMetadataType {

    private static final long serialVersionUID = 3579710773784268831L;

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_SUPERTABLE_NAME = "SUPERTABLE_NAME";

    // ------------------------------------------------------------------------------------------ STATIC FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    // -----------------------------------------------------------------------------------------------------------------
    public String getTableCat() {
        return tableCat;
    }

    protected void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getTableSchem() {
        return tableSchem;
    }

    protected void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getTableName() {
        return tableName;
    }

    protected void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public String getSupertableName() {
        return supertableName;
    }

    protected void setSupertableName(final String supertableName) {
        this.supertableName = supertableName;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_SUPERTABLE_NAME)

    private String supertableName;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Catalog tableCatalog_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Schema tableSchema_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Catalog supertableCatalog_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Schema supertableSchema_;
}
