/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
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


package com.github.jinahya.sql.database.meta.data.bind;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "name", "maxLen", "defaultValue", "description"
    }
)
public class ClientInfoProperty {


    /**
     *
     * @param database
     * @param suppression
     * @param clientInfoProperties
     *
     * @throws SQLException if a database access error occurs.
     *
     * @see DatabaseMetaData#getClientInfoProperties()
     */
    public static void retrieve(
        final DatabaseMetaData database, final Suppression suppression,
        final Collection<? super ClientInfoProperty> clientInfoProperties)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (clientInfoProperties == null) {
            throw new NullPointerException("null clientInfoProperties");
        }

        if (suppression.isSuppressed(
            Metadata.SUPPRESSION_PATH_CLIENT_INFO_PROPERTIES)) {
            return;
        }

        final ResultSet resultSet = database.getClientInfoProperties();
        try {
            while (resultSet.next()) {
                clientInfoProperties.add(ColumnRetriever.retrieve(
                    ClientInfoProperty.class, suppression, resultSet));
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
     * @throws SQLException if a database access error occurs.
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Metadata metadata)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (metadata == null) {
            throw new NullPointerException("null metadata");
        }

        retrieve(database, suppression, metadata.getClientInfoProperties());

        for (final ClientInfoProperty clientInfoProperty
             : metadata.getClientInfoProperties()) {
            clientInfoProperty.setMetadata(metadata);
        }
    }


    /**
     * Creates a new instance.
     */
    public ClientInfoProperty() {

        super();
    }


    // ---------------------------------------------------------------- metadata
    public Metadata getMetadata() {

        return metadata;
    }


    public void setMetadata(final Metadata metadata) {

        this.metadata = metadata;
    }


    // -------------------------------------------------------------------- name
    public String getName() {

        return name;
    }


    public void setName(final String name) {

        this.name = name;
    }


    // ------------------------------------------------------------------ maxLen
    public int getMaxLen() {

        return maxLen;
    }


    public void setMaxLen(final int maxLen) {

        this.maxLen = maxLen;
    }


    // ------------------------------------------------------------ defaultValue
    public String getDefaultValue() {

        return defaultValue;
    }


    public void setDefaultValue(final String defaultValue) {

        this.defaultValue = defaultValue;
    }


    // ------------------------------------------------------------- description
    public String getDescription() {

        return description;
    }


    public void setDescription(final String description) {

        this.description = description;
    }


    /**
     * parent metadata.
     */
    @XmlTransient
    private Metadata metadata;


    /**
     * The name of the client info property.
     */
    @ColumnLabel("NAME")
    @XmlElement(required = true)
    String name;


    /**
     * The maximum length of the value for the property.
     */
    @ColumnLabel("MAX_LEN")
    @XmlElement(required = true)
    int maxLen;


    /**
     * The default value of the property.
     */
    @ColumnLabel("DEFAULT_VALUE")
    @XmlElement(required = true)
    String defaultValue;


    /**
     * A description of the property. This will typically contain information as
     * to where this property is stored in the database.
     */
    @ColumnLabel("DESCRIPTION")
    @XmlElement(required = true)
    String description;


}

