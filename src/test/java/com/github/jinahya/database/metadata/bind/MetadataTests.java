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

import lombok.extern.slf4j.Slf4j;

/**
 * A utility class for testing {@link Metadata}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
final class MetadataTests {

    static void writeToFiles(final Metadata metadata, final String name) throws Exception {
        JaxbTests.writeToFile(Metadata.class, metadata, name);
        JsonbTests.writeToFile(metadata, name);
    }

    private MetadataTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
