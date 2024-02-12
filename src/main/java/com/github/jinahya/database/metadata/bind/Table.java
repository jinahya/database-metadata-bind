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
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the
 * {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTables(String, String, String, String[])
 */
@XmlRootElement
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Table extends AbstractMetadataType {

    private static final long serialVersionUID = 6590036695540141125L;

    // -----------------------------------------------------------------------------------------------------------------
    static final Comparator<Table> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Table::getTableType, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Table::getTableCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Table::getTableSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Table::getTableName, String.CASE_INSENSITIVE_ORDER);

    static final Comparator<Table> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Table::getTableType, naturalOrder())
                    .thenComparing(Table::getTableCat, nullsFirst(naturalOrder()))
                    .thenComparing(Table::getTableSchem, nullsFirst(naturalOrder()))
                    .thenComparing(Table::getTableName);

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_TYPE = "TABLE_TYPE";

    // -----------------------------------------------------------------------------------------------------------------
    static final BiPredicate<Table, Catalog> IS_OF_CATALOG = (t, c) -> {
        return Objects.equals(t.tableCat, c.getTableCat());
    };

    static final BiPredicate<Table, Schema> IS_OF_SCHEMA = (t, s) -> {
        return Objects.equals(t.tableCat, s.getTableCatalog()) &&
               Objects.equals(t.tableSchem, s.getTableSchem());
    };

    // -----------------------------------------------------------------------------------------------------------------
    static Table of(final String tableCat, final String tableSchem, final String tableName) {
        final Table instance = new Table();
        instance.setTableCat(tableCat);
        instance.setTableSchem(tableSchem);
        instance.setTableName(tableName);
        return instance;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    // ------------------------------------------------------------------------------------------------------- tableShem

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true)
    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    @EqualsAndHashCode.Include
    private String tableCat;

    @XmlElement(nillable = true)
    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    @EqualsAndHashCode.Include
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    @EqualsAndHashCode.Include
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_TABLE_TYPE)
    private String tableType;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("TYPE_CAT")
    private String typeCat;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @_MissingByVendor("Microsoft SQL Server") // https://github.com/microsoft/mssql-jdbc/issues/406
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("REF_GENERATION")
    private String refGeneration;

    // -----------------------------------------------------------------------------------------------------------------
//    @JsonbTypeAdapter(BestRowIdentifierJsonAdapter.class)
//    @XmlJavaTypeAdapter(BestRowIdentifierXmlAdapter.class)
//    @XmlElement(name = "bestRowIdentifierWrapperList")
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private final Map<Integer, Map<Boolean, List<BestRowIdentifier>>> bestRowIdentifier = new HashMap<>();
//
//    @XmlElementWrapper
//    @XmlElementRef
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private final List<ColumnPrivilege> columnPrivileges = new ArrayList<>();
//
//    @XmlElementWrapper
//    @XmlElementRef
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private final List<CrossReference> crossReferenceList = new ArrayList<>();
}
