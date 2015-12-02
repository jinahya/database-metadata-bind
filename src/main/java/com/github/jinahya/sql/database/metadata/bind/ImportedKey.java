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
 * {@link java.sql.DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see MetadataContext#getImportedKeys(java.lang.String, java.lang.String,
 * java.lang.String)
 */
@XmlRootElement
public class ImportedKey extends TableKey {


    public static Comparator<ImportedKey> natural() {

        return new Comparator<ImportedKey>() {

            @Override
            public int compare(final ImportedKey o1, final ImportedKey o2) {

                // by PKTABLE_CAT, PKTABLE_SCHEM, PKTABLE_NAME, and KEY_SEQ.
                return new CompareToBuilder()
                    .append(o1.getPktableCat(), o2.getPktableCat())
                    .append(o1.getPktableSchem(), o2.getPktableSchem())
                    .append(o1.getPktableName(), o2.getPktableName())
                    .append(o1.getKeySeq(), o2.getKeySeq())
                    .build();
            }

        };
    }


    // ------------------------------------------------------------------- table
    // for class diagrams
    private Table getTable() {

        return getParent();
    }

}

