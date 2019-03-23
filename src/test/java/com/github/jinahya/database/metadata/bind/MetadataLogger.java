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

import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;
import java.util.Arrays;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

class MetadataLogger implements InvocationHandler {

    private static final Logger logger = getLogger(lookup().lookupClass());

    static DatabaseMetaData newProxy(final DatabaseMetaData metadata) {
        return (DatabaseMetaData) Proxy.newProxyInstance(
                MetadataLogger.class.getClassLoader(),
                new Class<?>[] {DatabaseMetaData.class},
                new MetadataLogger(metadata));
    }

    // -------------------------------------------------------------------------
    MetadataLogger(final DatabaseMetaData metadata) {
        super();
        this.metadata = requireNonNull(metadata, "metadata is null");
    }

    // -------------------------------------------------------------------------
    @Override
    public Object invoke(final Object proxy, final Method method,
                         final Object[] args)
            throws Throwable {
        logger.debug("invoking {} with {}", method, Arrays.toString(args));
        return method.invoke(metadata, args);
    }

    // -------------------------------------------------------------------------
    private final DatabaseMetaData metadata;
}
