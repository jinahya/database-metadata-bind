package com.github.jinahya.database.metadata.bind;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
    public static Metadata newInstance(final Context context) {
        final Metadata instance = new Metadata();
        try {
            context.addCatalogs(instance.getCatalogs());
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "failed to collect catalogs", e);
        }
        try {
            context.addSchemas(instance.getSchemas());
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "failed to collect catalogs", e);
        }
        try {
            context.addTables(null, null, "%", null, instance.getTables());
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "failed to collect tables", e);
        }
        for (final var table : instance.getTables()) {
            try {
                context.addPrimaryKeys(
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
                context.addColumnPrivileges(
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
            context.addColumns(null, null, "%", "%", instance.getColumns());
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
    @Nonnull
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
    @Nonnull
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
    @Nonnull
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
    @Nonnull
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
    @Nonnull
    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        return columns;
    }

    void setColumns(final List<Column> columns) {
        this.columns = columns;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nonnull
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
    private List<@Valid @NotNull Catalog> catalogs;

    private List<@Valid @NotNull Schema> schemas;

    private List<@Valid @NotNull Table> tables;

    private List<@Valid @NotNull PrimaryKey> primaryKeys;

    private List<@Valid @NotNull Column> columns;

    private List<@Valid @NotNull ColumnPrivilege> columnPrivileges;
}
