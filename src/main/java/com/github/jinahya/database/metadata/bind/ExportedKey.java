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

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;

/**
 * An abstract class for binding results of the {@link DatabaseMetaData#getExportedKeys(String, String, String)}
 * method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getExportedKeys(String, String, String)
 * @see ImportedKey
 */
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExportedKey extends TableKey<ExportedKey> {

    private static final long serialVersionUID = -6561660015694928357L;

    // -----------------------------------------------------------------------------------------------------------------
    static final Comparator<ExportedKey> CASE_INSENSITIVE_ORDER = comparingFktableCaseInsensitive();

    static final Comparator<ExportedKey> LEXICOGRAPHIC_ORDER = comparingFktableLexicographic();
}
