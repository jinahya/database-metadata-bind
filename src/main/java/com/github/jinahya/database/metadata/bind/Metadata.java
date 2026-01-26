package com.github.jinahya.database.metadata.bind;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

class Metadata
        implements Serializable {

    private static final long serialVersionUID = 1752672827290701143L;

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS
    static Metadata newInstance(final Context context) {
        final Metadata instance = new Metadata();
        try {
            context.getCatalogsAndAddAll(instance.getCatalogs());
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "failed to collect catalogs", e);
        }
        try {
            context.getSchemasAndAddAll(instance.getSchemas());
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "failed to collect catalogs", e);
        }
        try {
            context.getTablesAndAddAll(null, null, "%", null, instance.getTables());
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "failed to collect tables", e);
        }
        for (final var table : instance.getTables()) {
            try {
                context.getPrimaryKeysAndAddAll(
                        Optional.ofNullable(table.getTableCat()).orElse(""),
                        Optional.ofNullable(table.getTableSchem()).orElse(""),
                        table.getTableName(),
                        instance.getPrimaryKeys()
                );
            } catch (final Exception e) {
                logger.log(Level.SEVERE, "failed to collect primary keys", e);
            }
        }
        for (final var table : instance.getTables()) {
            try {
                context.getColumnPrivilegesAndAddAll(
                        Optional.ofNullable(table.getTableCat()).orElse(""),
                        Optional.ofNullable(table.getTableSchem()).orElse(""),
                        table.getTableName(),
                        "%",
                        instance.getColumnPrivileges()
                );
            } catch (final Exception e) {
                logger.log(Level.SEVERE, "failed to collect column privileges", e);
            }
        }
        try {
            context.getColumnsAndAddAll(null, null, "%", "%", instance.getColumns());
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "failed to collect columns", e);
        }
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    Metadata() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public List<Catalog> getCatalogs() {
        if (catalogs == null) {
            catalogs = new ArrayList<>();
        }
        return catalogs;
    }

    void setCatalogs(final List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public List<Schema> getSchemas() {
        if (schemas == null) {
            schemas = new ArrayList<>();
        }
        return schemas;
    }

    void setSchemas(final List<Schema> schemas) {
        this.schemas = schemas;
    }

    // ---------------------------------------------------------------------------------------------------------- tables
    public List<Table> getTables() {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        return tables;
    }

    void setTables(final List<Table> table) {
        this.tables = table;
    }

    // ----------------------------------------------------------------------------------------------------- primaryKeys
    public List<PrimaryKey> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<>();
        }
        return primaryKeys;
    }

    void setPrimaryKeys(final List<PrimaryKey> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    // --------------------------------------------------------------------------------------------------------- columns
    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        return columns;
    }

    void setColumns(final List<Column> columns) {
        this.columns = columns;
    }

    // ------------------------------------------------------------------------------------------------ columnPrivileges

    public List<ColumnPrivilege> getColumnPrivileges() {
        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<>();
        }
        return columnPrivileges;
    }

    void setColumnPrivileges(final List<ColumnPrivilege> columnPrivileges) {
        this.columnPrivileges = columnPrivileges;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private List<Catalog> catalogs;

    private List<Schema> schemas;

    private List<Table> tables;

    private List<PrimaryKey> primaryKeys;

    private List<Column> columns;

    private List<ColumnPrivilege> columnPrivileges;
}
