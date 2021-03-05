package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String,
 * java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class TablePrivilege extends TableChild {

    private static final long serialVersionUID = -1799954363648972203L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public TablePrivilege() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "tableCat=" + tableCat
               + ",tableSchem=" + tableSchem
               + ",tableName=" + tableName
               + ",grantor=" + grantor
               + ",grantee=" + grantee
               + ",privilege=" + privilege
               + ",isGrantable=" + isGrantable
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final TablePrivilege that = (TablePrivilege) obj;
        return Objects.equals(tableCat, that.tableCat)
               && Objects.equals(tableSchem, that.tableSchem)
               && Objects.equals(tableName, that.tableName)
               && Objects.equals(grantor, that.grantor)
               && Objects.equals(grantee, that.grantee)
               && Objects.equals(privilege, that.privilege)
               && Objects.equals(isGrantable, that.isGrantable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat,
                            tableSchem,
                            tableName,
                            grantor,
                            grantee,
                            privilege,
                            isGrantable);
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // --------------------------------------------------------------------------------------------------------- grantor
    public String getGrantor() {
        return grantor;
    }

    public void setGrantor(final String grantor) {
        this.grantor = grantor;
    }

    // --------------------------------------------------------------------------------------------------------- grantee
    public String getGrantee() {
        return grantee;
    }

    public void setGrantee(final String grantee) {
        this.grantee = grantee;
    }

    // ------------------------------------------------------------------------------------------------------- privilege
    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(final String privilege) {
        this.privilege = privilege;
    }

    // ----------------------------------------------------------------------------------------------------- isGrantable
    public String getIsGrantable() {
        return isGrantable;
    }

    public void setIsGrantable(final String isGrantable) {
        this.isGrantable = isGrantable;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_CAT")
    private String tableCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(required = true)
    @Label("TABLE_NAME")
    private String tableName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("GRANTOR")
    private String grantor;

    @XmlElement(required = true)
    @Label("GRANTEE")
    private String grantee;

    @XmlElement(required = true)
    @Label("PRIVILEGE")
    private String privilege;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("IS_GRANTABLE")
    private String isGrantable;
}
