package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

@XmlRootElement
public class Metadata {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public static Metadata newInstance(final Context context) throws SQLException {
        requireNonNull(context, "context is null");
        final Metadata instance = new Metadata();
        // -------------------------------------------------------------------------------------------------------------
        instance.deletesAreDetecteds = DeletesAreDetected.getAllInstances(context, new ArrayList<>());
        instance.insertsAreDetecteds = InsertsAreDetected.getAllInstances(context, new ArrayList<>());
        instance.updatesAreDetecteds = UpdatesAreDetected.getAllInstances(context, new ArrayList<>());
        // -------------------------------------------------------------------------------------------------------------
        instance.othersDeletesAreVisibles = OthersDeletesAreVisible.getAllInstances(context, new ArrayList<>());
        instance.othersInsertsAreVisibles = OthersInsertsAreVisible.getAllInstances(context, new ArrayList<>());
        instance.othersUpdatesAreVisibles = OthersUpdatesAreVisible.getAllInstances(context, new ArrayList<>());
        instance.ownDeletesAreVisibles = OwnDeletesAreVisible.getAllInstances(context, new ArrayList<>());
        instance.ownInsertsAreVisibles = OwnInsertsAreVisible.getAllInstances(context, new ArrayList<>());
        instance.ownUpdatesAreVisibles = OwnUpdatesAreVisible.getAllInstances(context, new ArrayList<>());
        // -------------------------------------------------------------------------------------------------------------
        {
            instance.catalogs = context.getCatalogs(new ArrayList<>());
            if (instance.catalogs.isEmpty()) {
                instance.catalogs.add(Catalog.newVirtualInstance()); // ""
            }
            for (final Catalog catalog : instance.catalogs) {
                catalog.retrieveChildren(context);
            }
        }
        {
            instance.clientInfoProperties = context.getClientInfoProperties(new ArrayList<>());
            for (final ClientInfoProperty each : instance.clientInfoProperties) {
                each.retrieveChildren(context);
            }
        }
        {
            instance.crossReferences = CrossReference.getAllInstance(context, new ArrayList<>());
            for (final CrossReference each : instance.crossReferences) {
                each.retrieveChildren(context);
            }
        }
        // -------------------------------------------------------------------------------------------------------------
        instance.supportsConverts = SupportsConvert.getAllInstances(context, new ArrayList<>());
        instance.supportsResultSetConcurrencies
                = SupportsResultSetConcurrency.getAllInstances(context, new ArrayList<>());
        instance.supportsResultSetHoldabilities
                = SupportsResultSetHoldability.getAllInstances(context, new ArrayList<>());
        instance.supportsResultSetTypes = SupportsResultSetType.getAllInstances(context, new ArrayList<>());
        instance.supportsTransactionIsolationLevels
                = SupportsTransactionIsolationLevel.getAllInstances(context, new ArrayList<>());
        // -------------------------------------------------------------------------------------------------------------
        {
            instance.tableTypes = context.getTableTypes(new ArrayList<>());
            for (final TableType each : instance.tableTypes) {
                each.retrieveChildren(context);
            }
        }
        // -------------------------------------------------------------------------------------------------------------
        return instance;
    }

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
    private List<OthersDeletesAreVisible> othersDeletesAreVisibles;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<OthersInsertsAreVisible> othersInsertsAreVisibles;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<OthersUpdatesAreVisible> othersUpdatesAreVisibles;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<OwnDeletesAreVisible> ownDeletesAreVisibles;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<OwnInsertsAreVisible> ownInsertsAreVisibles;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<OwnUpdatesAreVisible> ownUpdatesAreVisibles;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<Catalog> catalogs;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<ClientInfoProperty> clientInfoProperties;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<CrossReference> crossReferences;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<SupportsConvert> supportsConverts;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<SupportsResultSetConcurrency> supportsResultSetConcurrencies;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<SupportsResultSetHoldability> supportsResultSetHoldabilities;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<SupportsResultSetType> supportsResultSetTypes;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<SupportsTransactionIsolationLevel> supportsTransactionIsolationLevels;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<TableType> tableTypes;
}
