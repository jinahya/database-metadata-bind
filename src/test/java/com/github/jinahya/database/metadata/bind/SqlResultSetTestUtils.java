package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2023 Jinahya, Inc.
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

import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.sql.ResultSet;
import java.util.stream.IntStream;

final class SqlResultSetTestUtils {

    static IntStream getConcurStream() {
        return ReflectionUtils.findFields(
                        ResultSet.class, f -> {
                            if (!ModifierSupport.isStatic(f)) {
                                return false;
                            }
                            if (f.getType() != int.class) {
                                return false;
                            }
                            if (!f.getName().startsWith("CONCUR_")) {
                                return false;
                            }
                            return true;
                        },
                        ReflectionUtils.HierarchyTraversalMode.BOTTOM_UP)
                .stream()
                .mapToInt(f -> {
                    try {
                        return f.getInt(null);
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(roe);
                    }
                });
    }

    static IntStream getTypeStream() {
        return ReflectionUtils.findFields(
                        ResultSet.class, f -> {
                            if (!ModifierSupport.isStatic(f)) {
                                return false;
                            }
                            if (f.getType() != int.class) {
                                return false;
                            }
                            if (!f.getName().startsWith("TYPE_")) {
                                return false;
                            }
                            return true;
                        },
                        ReflectionUtils.HierarchyTraversalMode.BOTTOM_UP)
                .stream()
                .mapToInt(f -> {
                    try {
                        return f.getInt(null);
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException(roe);
                    }
                });
    }

    private SqlResultSetTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
