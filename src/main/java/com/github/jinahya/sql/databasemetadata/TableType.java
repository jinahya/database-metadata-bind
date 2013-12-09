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
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
@XmlRootElement
public class TableType {


    public static TableType retrieve(final Suppression suppression,
                                     final ResultSet resultSet)
        throws SQLException {

        if (suppression == null) { throw new NullPointerException("null suppression");}

        if (resultSet == null) { throw new NullPointerException("resultSet"); }

        final TableType instance = new TableType();

        ColumnRetriever.retrieve(TableType.class, instance, suppression,
                                 resultSet);

        return instance;
    }


    /**
     *
     * @param database
     * @param suppression
     * @param tableTypes
     *
     * @throws SQLException
     *
     * @see DatabaseMetaData#getTableTypes()
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Collection<? super TableType> tableTypes)
        throws SQLException {

        if (database == null) { throw new NullPointerException("null database");}

        if (suppression == null) { throw new NullPointerException("null suppression");}

        if (tableTypes == null) { throw new NullPointerException("tableTypes"); }

        if (suppression.isSuppressed(Metadata.SUPPRESSION_PATH_TABLE_TYPES)) {
            return;
        }

        final ResultSet resultSet = database.getTableTypes();
        try {
            while (resultSet.next()) {
                tableTypes.add(retrieve(suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Metadata metadata)
        throws SQLException {

        if (database == null) { throw new NullPointerException("null database");}

        if (suppression == null) { throw new NullPointerException("null suppression");}

        if (metadata == null) { throw new NullPointerException("metadata"); }

        retrieve(database, suppression, metadata.getTableTypes());

        for (final TableType tableType : metadata.getTableTypes()) {
            tableType.setMetadata(metadata);
        }
    }


    // ---------------------------------------------------------------- metadata
    public Metadata getMetadata() {

        return metadata;
    }


    public void setMetadata(final Metadata metadata) {

        this.metadata = metadata;
    }


    // --------------------------------------------------------------- tableType
    public String getTableType() {

        return tableType;
    }


    public void setTableType(final String tableType) {

        this.tableType = tableType;
    }


    @XmlTransient
    private Metadata metadata;


    @ColumnLabel("TABLE_TYPE")
    //@SuppressionPath("tableType/tableType")
    @XmlElement(required = true)
    private String tableType;


}

