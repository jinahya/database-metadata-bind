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


package com.github.jinahya.sql.database.metadata.bind;


import java.util.Comparator;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.CompareToBuilder;


/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see java.sql.DatabaseMetaData#getExportedKeys(java.lang.String,
 * java.lang.String, java.lang.String)
 * @see MetadataContext#getExportedKeys(java.lang.String, java.lang.String,
 * java.lang.String)
 */
@XmlRootElement
public class ExportedKey extends TableKey {


    public static Comparator<ExportedKey> natural() {
        return new Comparator<ExportedKey>() {

            @Override
            public int compare(final ExportedKey o1, final ExportedKey o2) {

                // by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and KEY_SEQ.
                return new CompareToBuilder()
                    .append(o1.getFktableCat(), o2.getFktableCat())
                    .append(o1.getFktableSchem(), o2.getFktableSchem())
                    .append(o1.getFktableName(), o2.getFktableName())
                    .append(o1.getKeySeq(), o2.getKeySeq())
                    .build();
            }

        };
    }


    // ------------------------------------------------------------------- table
    // just for class diagram
    private Table getTable() {

        return getParent();
    }

}

