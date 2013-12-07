/*
 * Copyright 2011 Jin Kwon <jinahya at gmail.com>.
 *
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
 */


package com.github.jinahya.sql.databasemetadata;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "tableCat", "schemas"
    }
)
public class Catalog implements Comparable<Catalog> {


    public static final String SUPPRESSION_PATH_SCHEMAS
        = "catalog/schemas";


    /**
     *
     * @param database
     * @param suppression
     * @param catalogs
     *
     * @throws SQLException if a database access error occurs.
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Collection<? super Catalog> catalogs)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(catalogs, "null catalogs");

        if (suppression.isSuppressed(Metadata.SUPPRESSION_PATH_CATALOGS)) {
            return;
        }

        final ResultSet resultSet = database.getCatalogs();
        try {
            while (resultSet.next()) {
                catalogs.add(ColumnRetriever.retrieve(
                    Catalog.class, suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    /**
     *
     * @param database
     * @param suppression
     * @param metadata
     *
     * @throws SQLException if a database error occurs.
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Metadata metadata)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(metadata, "null metadata");

        retrieve(database, suppression, metadata.getCatalogs());

        if (metadata.getCatalogs().isEmpty()) {
            final Catalog instance = new Catalog();
            instance.setTableCat("");
            metadata.getCatalogs().add(instance);
        }

        for (final Catalog catalog : metadata.getCatalogs()) {
            catalog.setMetadata(metadata);
        }

        for (final Catalog catalog : metadata.getCatalogs()) {
            Schema.retrieve(database, suppression, catalog);
        }
    }


    /**
     * Creates a new instance.
     */
    public Catalog() {

        super();
    }


    @Override
    public int compareTo(final Catalog o) {

        Objects.requireNonNull(o, "null object");

        return tableCat.compareTo(o.tableCat);
    }


    // ---------------------------------------------------------------- metadata
    public Metadata getMetadata() {

        return metadata;
    }


    public void setMetadata(final Metadata metadata) {

        this.metadata = metadata;
    }


    // ---------------------------------------------------------------- tableCat
    /**
     *
     * @return
     *
     */
    public String getTableCat() {

        return tableCat;
    }


    /**
     *
     * @param tableCat
     *
     */
    public void setTableCat(final String tableCat) {

        this.tableCat = tableCat;
    }


    // ----------------------------------------------------------------- schemas
    public List<Schema> getSchemas() {

        if (schemas == null) {
            schemas = new ArrayList<Schema>();
        }

        return schemas;
    }


    public List<String> getSchemaNames() {

        final List<String> schemaNames = new ArrayList<String>();

        for (final Schema schema : getSchemas()) {
            final String schemaName = schema.getTableSchem();
            assert !schemaNames.contains(schemaName);
            schemaNames.add(schemaName);
        }

        return schemaNames;
    }


    public Schema getSchemaByName(final String schemaName) {

        Objects.requireNonNull(schemaName, "null schemaName");

        for (final Schema schema : getSchemas()) {
            if (schemaName.equals(schema.getTableSchem())) {
                return schema;
            }
        }

        return null;
    }


    /**
     * metadata.
     */
    @XmlTransient
    private Metadata metadata;


    /**
     * catalog name.
     */
    @ColumnLabel("TABLE_CAT")
    @XmlElement(required = true)
    protected String tableCat;


    /**
     * schemas.
     */
    @SuppressionPath(SUPPRESSION_PATH_SCHEMAS)
    @XmlElement(name = "schema")
    private List<Schema> schemas;


}

