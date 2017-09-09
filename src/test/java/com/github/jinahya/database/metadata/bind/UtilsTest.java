/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
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
package com.github.jinahya.database.metadata.bind;

import com.github.jinahya.database.metadata.bind.Utils;
import static java.lang.invoke.MethodHandles.lookup;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 * Test for {@code Utils}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class UtilsTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    @Test(enabled = false)
    public void printSqlTypes() throws ReflectiveOperationException {
        final Field field = Utils.class.getDeclaredField("SQL_TYPES");
        field.setAccessible(true);
        logger.debug("SQL_TYPES: {}", field.get(null));
    }
}
