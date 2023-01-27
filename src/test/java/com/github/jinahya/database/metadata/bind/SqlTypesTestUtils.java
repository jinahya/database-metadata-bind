package com.github.jinahya.database.metadata.bind;

import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.sql.Types;
import java.util.stream.IntStream;

final class SqlTypesTestUtils {

    static IntStream getAllValues() {
        return ReflectionUtils.findFields(
                        Types.class, f -> {
                            final var modifiers = f.getModifiers();
                            if (!ModifierSupport.isStatic(f)) {
                                return false;
                            }
                            if (f.getType() != int.class) {
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

    private SqlTypesTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
