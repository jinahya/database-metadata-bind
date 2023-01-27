package com.github.jinahya.database.metadata.bind;

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
