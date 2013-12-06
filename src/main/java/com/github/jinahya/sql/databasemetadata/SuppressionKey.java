/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
@XmlRootElement
@XmlType(propOrder = {
    "databaseMajorVersion", "databaseMinorVersion", "databaseProductName",
    "databaseProductVersion",
    "driverMajorVersion", "driverMinorVersion", "driverName", "driverVersion"
})
public class SuppressionKey {


    public static SuppressionKey newInstance(final DatabaseMetaData database)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        final SuppressionKey instance = new SuppressionKey();

        instance.setDatabaseMajorVersion(database.getDatabaseMajorVersion());
        instance.setDatabaseMinorVersion(database.getDatabaseMinorVersion());
        instance.setDatabaseProductName(database.getDatabaseProductName());
        instance.setDatabaseProductVersion(database.getDatabaseProductName());

        instance.setDriverMajorVersion(database.getDriverMajorVersion());
        instance.setDriverMinorVersion(database.getDriverMinorVersion());
        instance.setDriverName(database.getDriverName());
        instance.setDriverVersion(database.getDriverVersion());

        return instance;
    }


    @Override
    public int hashCode() {

        int hash = 7;

        hash = 29 * hash + this.databaseMajorVersion;
        hash = 29 * hash + this.databaseMinorVersion;
        hash = 29 * hash + Objects.hashCode(this.databaseProductName);
        hash = 29 * hash + Objects.hashCode(this.databaseProductVersion);
        hash = 29 * hash + this.driverMajorVersion;
        hash = 29 * hash + this.driverMinorVersion;
        hash = 29 * hash + Objects.hashCode(this.driverName);
        hash = 29 * hash + Objects.hashCode(this.driverVersion);

        return hash;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final SuppressionKey that = (SuppressionKey) obj;

        if (this.databaseMajorVersion != that.databaseMajorVersion) {
            return false;
        }

        if (this.databaseMinorVersion != that.databaseMinorVersion) {
            return false;
        }

        if (!Objects.equals(this.databaseProductName,
                            that.databaseProductName)) {
            return false;
        }

        if (!Objects.equals(this.databaseProductVersion,
                            that.databaseProductVersion)) {
            return false;
        }

        if (this.driverMajorVersion != that.driverMajorVersion) {
            return false;
        }

        if (this.driverMinorVersion != that.driverMinorVersion) {
            return false;
        }

        if (!Objects.equals(this.driverName, that.driverName)) {
            return false;
        }

        if (!Objects.equals(this.driverVersion, that.driverVersion)) {
            return false;
        }

        return true;
    }


    @Override
    public String toString() {

        final Map<String, Object> values = new LinkedHashMap<String, Object>();
        values.put("databaseMajorVersion", databaseMajorVersion);
        values.put("databaseMinorVersion", databaseMinorVersion);
        values.put("databaseProductName", databaseProductName);
        values.put("databaseProductVersion", databaseProductVersion);
        values.put("driverMajorVersion", driverMajorVersion);
        values.put("driverMinorVersion", driverMinorVersion);
        values.put("driverName", driverName);
        values.put("driverVersion", driverVersion);

        return super.toString() + "?" + values.toString();
    }


    public int getDatabaseMajorVersion() {

        return databaseMajorVersion;
    }


    public void setDatabaseMajorVersion(final int databaseMajorVersion) {

        this.databaseMajorVersion = databaseMajorVersion;
    }


    public int getDatabaseMinorVersion() {

        return databaseMinorVersion;
    }


    public void setDatabaseMinorVersion(final int databaseMinorVersion) {

        this.databaseMinorVersion = databaseMinorVersion;
    }


    public String getDatabaseProductName() {

        return databaseProductName;
    }


    public void setDatabaseProductName(final String databaseProductName) {

        this.databaseProductName = databaseProductName;
    }


    public String getDatabaseProductVersion() {

        return databaseProductVersion;
    }


    public void setDatabaseProductVersion(final String databaseProductVersion) {

        this.databaseProductVersion = databaseProductVersion;
    }


    public int getDriverMajorVersion() {

        return driverMajorVersion;
    }


    public void setDriverMajorVersion(final int driverMajorVersion) {

        this.driverMajorVersion = driverMajorVersion;
    }


    public int getDriverMinorVersion() {

        return driverMinorVersion;
    }


    public void setDriverMinorVersion(final int driverMinorVersion) {

        this.driverMinorVersion = driverMinorVersion;
    }


    public String getDriverName() {

        return driverName;
    }


    public void setDriverName(final String driverName) {

        this.driverName = driverName;
    }


    public String getDriverVersion() {

        return driverVersion;
    }


    public void setDriverVersion(final String driverVersion) {

        this.driverVersion = driverVersion;
    }


    @XmlElement(nillable = true, required = true)
    private int databaseMajorVersion;


    @XmlElement(nillable = true, required = true)
    private int databaseMinorVersion;


    @XmlElement(nillable = true, required = true)
    private String databaseProductName;


    @XmlElement(nillable = true, required = true)
    private String databaseProductVersion;


    @XmlElement(nillable = true, required = true)
    private int driverMajorVersion;


    @XmlElement(nillable = true, required = true)
    private int driverMinorVersion;


    @XmlElement(nillable = true, required = true)
    private String driverName;


    @XmlElement(nillable = true, required = true)
    private String driverVersion;


}

