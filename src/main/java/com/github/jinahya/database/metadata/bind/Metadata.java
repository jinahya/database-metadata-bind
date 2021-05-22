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
        instance.deletesAreDetecteds = DeletesAreDetected.getAllInstances(context);
        instance.insertsAreDetecteds = InsertsAreDetected.getAllInstances(context);
        instance.updatesAreDetecteds = UpdatesAreDetected.getAllInstances(context);
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
                context.getFunctions(schema, null);
                for (final Function function : schema.getFunctions()) {
                    context.getFunctionColumns(function, null);
                }
                context.getProcedures(schema, null);
                for (final Procedure procedure : schema.getProcedures()) {
                    context.getProcedureColumns(procedure, null);
                }
                context.getTables(schema, null, null, schema.getTables());
                for (final Table table : schema.getTables()) {
                    for (final BestRowIdentifier.Scope value : BestRowIdentifier.Scope.values()) {
                        context.getBestRowIdentifier(table, value.getRawValue(), true);
                    }
                    context.getColumns(table, null, table.getColumns());
                    context.getColumnPrivileges(table, null, table.getColumnPrivileges());
                    context.getIndexInfo(table, false, true);
                    context.getPrimaryKeys(table);
                    context.getPseudoColumns(table, null);
                    context.getSuperTables(table);
                    context.getTablePrivileges(table);
                    context.getVersionColumns(table);
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
