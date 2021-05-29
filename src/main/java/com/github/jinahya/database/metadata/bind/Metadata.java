package com.github.jinahya.database.metadata.bind;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@XmlRootElement
public class Metadata {

    public static Metadata newInstance(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final Metadata instance = new Metadata();
        instance.deletesAreDetecteds = DeletesAreDetected.getAllInstances(context, new ArrayList<>());
        instance.insertsAreDetecteds = InsertsAreDetected.getAllInstances(context, new ArrayList<>());
        instance.updatesAreDetecteds = UpdatesAreDetected.getAllInstances(context, new ArrayList<>());
        instance.catalogs = context.getCatalogs(new ArrayList<>());
        if (instance.catalogs.isEmpty()) {
            instance.catalogs.add(Catalog.newVirtualInstance());
        }
        for (final Catalog catalog : instance.catalogs) {
            context.getSchemas(catalog, null, catalog.getSchemas());
            if (catalog.getSchemas().isEmpty()) {
                catalog.getSchemas().add(Schema.newVirtualInstance(catalog));
            }
            for (final Schema schema : catalog.getSchemas()) {
                context.getFunctions(schema, null, schema.getFunctions());
                for (final Function function : schema.getFunctions()) {
                    context.getFunctionColumns(function, null, function.getFunctionColumns());
                }
                context.getProcedures(schema, null, schema.getProcedures());
                for (final Procedure procedure : schema.getProcedures()) {
                    context.getProcedureColumns(procedure, null, procedure.getProcedureColumns());
                }
                context.getTables(schema, null, null, schema.getTables());
                for (final Table table : schema.getTables()) {
                    for (final BestRowIdentifier.Scope value : BestRowIdentifier.Scope.values()) {
                        context.getBestRowIdentifier(table, value.getRawValue(), true, table.getBestRowIdentifiers());
                    }
                    context.getColumns(table, null, table.getColumns());
                    context.getColumnPrivileges(table, null, table.getColumnPrivileges());
                    context.getIndexInfo(table, false, true, table.getIndexInfo());
                    context.getPrimaryKeys(table, table.getPrimaryKeys());
                    context.getPseudoColumns(table, null, table.getPseudoColumns());
                    context.getSuperTables(table, table.getSuperTables());
                    context.getTablePrivileges(table, table.getTablePrivileges());
                    context.getVersionColumns(table, table.getVersionColumns());
                }
                context.getUDTs(schema, null, null, schema.getUDTs());
                for (final UDT udt : schema.getUDTs()) {
                    context.getAttributes(udt, null, udt.getAttributes());
                    context.getSuperTypes(udt, udt.getSuperTypes());
                }
            }
        }
        instance.tableTypes = context.getTableTypes(new ArrayList<>());
        // -------------------------------------------------------------------------------------------------------------
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    Metadata() {
        super();
    }

    // --------------------------------------------------------------------------------------------------------------- \
    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<DeletesAreDetected> deletesAreDetecteds;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<InsertsAreDetected> insertsAreDetecteds;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<UpdatesAreDetected> updatesAreDetecteds;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<Catalog> catalogs;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<TableType> tableTypes;
}
