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

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.java.Log;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlRootElement
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Log
public class Metadata {

    public static Metadata newInstance(final Context context) throws SQLException {
        Objects.requireNonNull(context, "context is null");
        final Metadata instance = new Metadata();
        // -------------------------------------------------------------------------------------------------------------
        instance.deletesAreDetected = DeletesAreDetected.getAllInstances(context, new ArrayList<>());
        instance.insertsAreDetected = InsertsAreDetected.getAllInstances(context, new ArrayList<>());
        instance.updatesAreDetected = UpdatesAreDetected.getAllInstances(context, new ArrayList<>());
        // -------------------------------------------------------------------------------------------------------------
        instance.othersDeletesAreVisible = OthersDeletesAreVisible.getAllInstances(context, new ArrayList<>());
        instance.othersInsertsAreVisible = OthersInsertsAreVisible.getAllInstances(context, new ArrayList<>());
        instance.othersUpdatesAreVisible = OthersUpdatesAreVisible.getAllInstances(context, new ArrayList<>());
        instance.ownDeletesAreVisible = OwnDeletesAreVisible.getAllInstances(context, new ArrayList<>());
        instance.ownInsertsAreVisible = OwnInsertsAreVisible.getAllInstances(context, new ArrayList<>());
        instance.ownUpdatesAreVisible = OwnUpdatesAreVisible.getAllInstances(context, new ArrayList<>());
        // -------------------------------------------------------------------------------------------------------------
        {
            instance.catalogs = new ArrayList<>();
            context.getCatalogs(instance.catalogs::add);
            if (instance.catalogs.isEmpty()) {
                instance.catalogs.add(Catalog.newVirtualInstance());
            }
            for (final Catalog catalog : instance.catalogs) {
                catalog.retrieveChildren(context);
            }
        }
        {
            instance.clientInfoProperties = new ArrayList<>();
            context.getClientInfoProperties(instance.clientInfoProperties::add);
            for (final ClientInfoProperty each : instance.clientInfoProperties) {
                each.retrieveChildren(context);
            }
        }
        {
            instance.crossReferences = CrossReference.getAllInstances(context, new ArrayList<>());
            for (final CrossReference each : instance.crossReferences) {
                each.retrieveChildren(context);
            }
        }
        // -------------------------------------------------------------------------------------------------------------
        instance.supportsConverts = SupportsConvert.getAllInstances(context, new ArrayList<>());
        instance.supportsResultSetConcurrency
                = SupportsResultSetConcurrency.getAllInstances(context, new ArrayList<>());
        instance.supportsResultSetHoldability
                = SupportsResultSetHoldability.getAllInstances(context, new ArrayList<>());
        instance.supportsResultSetTypes = SupportsResultSetType.getAllInstances(context, new ArrayList<>());
        instance.supportsTransactionIsolationLevels
                = SupportsTransactionIsolationLevel.getAllInstances(context, new ArrayList<>());
        // -------------------------------------------------------------------------------------------------------------
        {
            instance.tableTypes = context.getTableTypes();
            for (final TableType each : instance.tableTypes) {
                each.retrieveChildren(context);
            }
        }
        // -------------------------------------------------------------------------------------------------------------
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<@Valid @NotNull DeletesAreDetected> deletesAreDetected;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<InsertsAreDetected> insertsAreDetected;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<UpdatesAreDetected> updatesAreDetected;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<OthersDeletesAreVisible> othersDeletesAreVisible;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<@Valid @NotNull OthersInsertsAreVisible> othersInsertsAreVisible;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<@Valid @NotNull OthersUpdatesAreVisible> othersUpdatesAreVisible;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<OwnDeletesAreVisible> ownDeletesAreVisible;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<@Valid @NotNull OwnInsertsAreVisible> ownInsertsAreVisible;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<@Valid @NotNull OwnUpdatesAreVisible> ownUpdatesAreVisible;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<Catalog> catalogs;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<ClientInfoProperty> clientInfoProperties;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<CrossReference> crossReferences;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<SupportsConvert> supportsConverts;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<SupportsResultSetConcurrency> supportsResultSetConcurrency;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<SupportsResultSetHoldability> supportsResultSetHoldability;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<SupportsResultSetType> supportsResultSetTypes;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<SupportsTransactionIsolationLevel> supportsTransactionIsolationLevels;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    private List<TableType> tableTypes;
}
