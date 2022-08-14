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

/**
 * Constants for XML binding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class XmlConstants {

    /**
     * The XML namespace URI.
     */
    public static final String NS_URI_DATABASE_METADATA_BIND = "http://github.com/jinahya/database/metadata/bind";

    static final String NS_PREFIX_DATABASE_MEATDATA_BIND = "dmb";

    /**
     * Creates a new instance.
     */
    private XmlConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
