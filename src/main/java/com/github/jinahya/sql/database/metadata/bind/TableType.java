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

import java.util.Comparator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getTableTypes()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
        propOrder = {
            "tableType"
        }
)
public class TableType extends AbstractChild<Metadata> {

    public static Comparator<TableType> natural() {
        return new Comparator<TableType>() {
            @Override
            public int compare(final TableType o1, final TableType o2) {
                // by table type
                return new CompareToBuilder()
                        .append(o1.getTableType(), o2.getTableType())
                        .build();
            }
        };
    }

    @Override
    public String toString() {
        return super.toString() + "{"
               + "tableType=" + tableType
               + "}";
    }

    // --------------------------------------------------------------- tableType
    public String getTableType() {
        return tableType;
    }

    public void setTableType(final String tableType) {
        this.tableType = tableType;
    }

    // ---------------------------------------------------------------- metadata
    // just for class diagram
    @Deprecated
    private Metadata getMetadata() {
        return getParent();
    }

//    public void setMetadata(final Metadata metadata) {
//
//        setParent(metadata);
//    }
    // -------------------------------------------------------------------------
    @Label("TABLE_TYPE")
    @XmlElement(required = true)
    private String tableType;
}
