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

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of the
 * {@link java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTablePrivileges(String, String, String)
 */
@_ChildOf(Schema.class)
@_ChildOf(Catalog.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class TablePrivilege
        extends AbstractMetadataType {

    private static final long serialVersionUID = -2142097373603478881L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<TablePrivilege> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return Comparator.comparing(TablePrivilege::getTableCat, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(TablePrivilege::getTableSchem, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(TablePrivilege::getTableName, ContextUtils.nullPrecedence(context, comparator))
                .thenComparing(TablePrivilege::getPrivilege, ContextUtils.nullPrecedence(context, comparator));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    protected TablePrivilege() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object
    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",grantor=" + grantor +
               ",grantee=" + grantee +
               ",privilege=" + privilege +
               ",isGrantable=" + isGrantable +
               '}';
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    // --------------------------------------------------------------------------------------------------------- grantor
    public String getGrantor() {
        return grantor;
    }

    // --------------------------------------------------------------------------------------------------------- grantee
    public String getGrantee() {
        return grantee;
    }

    // ------------------------------------------------------------------------------------------------------ privilege
    public String getPrivilege() {
        return privilege;
    }

    // ----------------------------------------------------------------------------------------------------- isGrantable
    public String getIsGrantable() {
        return isGrantable;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("GRANTOR")
    private String grantor;

    @_ColumnLabel("GRANTEE")
    private String grantee;

    @_ColumnLabel("PRIVILEGE")
    private String privilege;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("IS_GRANTABLE")
    private String isGrantable;
}
