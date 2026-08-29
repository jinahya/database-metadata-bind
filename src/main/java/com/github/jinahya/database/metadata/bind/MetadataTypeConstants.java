package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

/**
 * Constants shared by more than one metadata type.
 * <p>
 * The JDBC specification uses the same {@code "YES"} / {@code "NO"} / {@code ""} vocabulary for several unrelated
 * columns - {@code IS_NULLABLE}, {@code IS_AUTOINCREMENT}, {@code IS_GENERATEDCOLUMN}, and {@code IS_GRANTABLE} among
 * them - so the literals and the regular expressions matching them are declared once here rather than repeated by each
 * binding type.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class MetadataTypeConstants {

    static final String YES = "YES";

    static final String NO = "NO";

    static final String EMPTY = "";

    static final String PATTERN_REGEXP_YES_OR_NO = YES + "|" + NO;

    static final String PATTERN_REGEXP_YES_NO_OR_EMPTY = "^$|" + PATTERN_REGEXP_YES_OR_NO;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance, which is not allowed.
     */
    private MetadataTypeConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
