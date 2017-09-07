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
package com.github.jinahya.sql.database.metadata.bind;

import java.io.Serializable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getTableTypes()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "tableType"
})
public class TableType implements Serializable {

    private static final long serialVersionUID = -7630634982776331078L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(TableType.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableType=" + tableType
               + "}";
    }

    // --------------------------------------------------------------- tableType
//    public String getTableType() {
//        return tableType;
//    }
//
//    public void setTableType(final String tableType) {
//        this.tableType = tableType;
//    }
//    @XmlAttribute // workarounding the bug; @XmlElementRef -> @XmlValue
//    public String getNothing() {
//        return null;
//    }
    // -------------------------------------------------------------------------
    @XmlElement
    @Label("TABLE_TYPE")
    @Bind(label = "TABLE_TYPE")
    //@XmlValue
    @Setter
    @Getter
    private String tableType;
}
