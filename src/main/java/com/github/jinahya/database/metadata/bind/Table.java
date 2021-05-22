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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class for binding results of {@link java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String,
 * java.lang.String, java.lang.String[])} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getTables(String, String, String, String[], Collection)
 * @see Context#getPrimaryKeys(Table)
 * @see Context#getPseudoColumns(Table, String)
 * @see Context#getTablePrivileges(Table)
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class Table
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = 6590036695540141125L;

    // -------------------------------------------------------------------------------------------- TABLE_CAT / tableCat
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String ATTRIBUTE_NAME_TABLE_CAT = "tableCat";

    // ---------------------------------------------------------------------------------------- TABLE_SCHEM / tableSchem
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String ATTRIBUTE_NAME_TABLE_SCHEM = "tableSchem";

    // ------------------------------------------------------------------------------------------ TABLE_NAME / tableName
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String ATTRIBUTE_NAME_TABLE_NAME = "tableName";

    // ---------------------------------------------------------------------------------------------- bestRowIdentifiers
    public List<BestRowIdentifier> getBestRowIdentifiers() {
        if (bestRowIdentifiers == null) {
            bestRowIdentifiers = new ArrayList<>();
        }
        return bestRowIdentifiers;
    }

    // --------------------------------------------------------------------------------------------------------- columns
    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        return columns;
    }

    // ------------------------------------------------------------------------------------------------ columnPrivileges
    public List<ColumnPrivilege> getColumnPrivileges() {
        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<>();
        }
        return columnPrivileges;
    }

    // ------------------------------------------------------------------------------------------------------- indexInfo
    public List<IndexInfo> getIndexInfo() {
        if (indexInfo == null) {
            indexInfo = new ArrayList<>();
        }
        return indexInfo;
    }

    // ----------------------------------------------------------------------------------------------------- primaryKeys
    public List<PrimaryKey> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<>();
        }
        return primaryKeys;
    }

    // --------------------------------------------------------------------------------------------------- pseudoColumns
    public List<PseudoColumn> getPseudoColumns() {
        if (pseudoColumns == null) {
            pseudoColumns = new ArrayList<>();
        }
        return pseudoColumns;
    }

    // ----------------------------------------------------------------------------------------------------- superTables
    public List<SuperTable> getSuperTables() {
        if (superTables == null) {
            superTables = new ArrayList<>();
        }
        return superTables;
    }

    // ------------------------------------------------------------------------------------------------- tablePrivileges
    public List<TablePrivilege> getTablePrivileges() {
        if (tablePrivileges == null) {
            tablePrivileges = new ArrayList<>();
        }
        return tablePrivileges;
    }

    // -------------------------------------------------------------------------------------------------- versionColumns
    public List<VersionColumn> getVersionColumns() {
        if (versionColumns == null) {
            versionColumns = new ArrayList<>();
        }
        return versionColumns;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @XmlElement(required = true)
    @Label(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("TABLE_TYPE")
    private String tableType;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("REF_GENERATION")
    private String refGeneration;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<BestRowIdentifier> bestRowIdentifiers;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Column> columns;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ColumnPrivilege> columnPrivileges;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull IndexInfo> indexInfo;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull PrimaryKey> primaryKeys;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull PseudoColumn> pseudoColumns;

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
    private List<@Valid @NotNull TablePrivilege> tablePrivileges;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull VersionColumn> versionColumns;
}
