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


import java.io.PrintStream;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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

        if (database == null) {
            throw new NullPointerException("null database");
        }

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

        hash = 29 * hash + databaseMajorVersion;
        hash = 29 * hash + databaseMinorVersion;
        hash = 29 * hash + databaseProductName.hashCode();
        hash = 29 * hash + databaseProductVersion.hashCode();
        hash = 29 * hash + driverMajorVersion;
        hash = 29 * hash + driverMinorVersion;
        hash = 29 * hash + driverName.hashCode();
        hash = 29 * hash + driverVersion.hashCode();

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

        if (databaseMajorVersion != that.databaseMajorVersion) {
            return false;
        }

        if (databaseMinorVersion != that.databaseMinorVersion) {
            return false;
        }

        if (!databaseProductName.equals(that.databaseProductName)) {
            return false;
        }

        if (!databaseProductVersion.equals(that.databaseProductVersion)) {
            return false;
        }

        if (driverMajorVersion != that.driverMajorVersion) {
            return false;
        }

        if (driverMinorVersion != that.driverMinorVersion) {
            return false;
        }

        if (!driverName.equals(that.driverName)) {
            return false;
        }

        if (!driverVersion.equals(that.driverVersion)) {
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


    public void print(final PrintStream out) throws JAXBException {

        if (out == null) {
            throw new NullPointerException("null out");
        }

        assert SuppressionKey.class.getAnnotation(XmlRootElement.class) != null;

        final JAXBContext context
            = JAXBContext.newInstance(SuppressionKey.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(this, out);
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

