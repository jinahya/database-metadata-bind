/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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

import static java.lang.invoke.MethodHandles.lookup;
import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.util.Arrays;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MetadataContextTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Test(expectedExceptions = {NullPointerException.class})
    public void constructWithNullDatabaseMetaData() {
        final MetadataContext context = new MetadataContext(null);
    }

    @Test(enabled = false)
    public void checkMethodBinding() throws JAXBException {
        Arrays.stream(DatabaseMetaData.class.getMethods()).filter(m -> {
            final int modifiers = m.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                return false;
            }
            return true;
        }).forEach(m -> {
            try {
                MetadataContext.class.getMethod(
                        m.getName(), m.getParameterTypes());
            } catch (final NoSuchMethodException nsme) {
                logger.info("method not covered: {}", m);
            }
        });
    }
}
