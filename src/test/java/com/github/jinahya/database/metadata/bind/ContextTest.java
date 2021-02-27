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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A class for testing {@link Metadata} class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class ContextTest {

    @Test
    void newInstance_NullPointerException_Null() {
        assertThatThrownBy(() -> Context.newInstance(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Disabled
    @Test
    void checkMethodBinding() {
        Arrays.stream(DatabaseMetaData.class.getMethods()).filter(m -> {
            final int modifiers = m.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                return false;
            }
            return true;
        }).forEach(m -> {
            try {
                Context.class.getMethod(m.getName(), m.getParameterTypes());
            } catch (final NoSuchMethodException nsme) {
                log.debug("method not covered: {}", m);
            }
        });
    }
}
