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

abstract class YesNoConstants {

    static final String YES = "YES";

    static final String NO = "NO";

    // -----------------------------------------------------------------------------------------------------------------
    static final String REGEXP_YES_NO_ = YES + '|' + NO;

    static final String REGEXP_YES_NO = "^(" + REGEXP_YES_NO_ + ")$";

    // -----------------------------------------------------------------------------------------------------------------
    YesNoConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
