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
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getSchemas()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
        "tableSchem", "tableCatalog"
})
public class SchemaName implements Serializable {

    private static final long serialVersionUID = 5784631477568740816L;

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "tableSchem=" + tableSchem
               + ",tableCatalog=" + tableCatalog
               + '}';
    }

    // -------------------------------------------------------------- tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------ tableCatalog
    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(final String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    // -------------------------------------------------------------------------
    @XmlElement
    @Bind(label = "TABLE_SCHEM")
    private String tableSchem;

    @XmlElement(nillable = true)
    @Bind(label = "TABLE_CATALOG", nillable = true)
    private String tableCatalog;
}
