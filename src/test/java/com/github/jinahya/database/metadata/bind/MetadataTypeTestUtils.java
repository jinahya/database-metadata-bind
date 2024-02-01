package com.github.jinahya.database.metadata.bind;

import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Objects;

@Slf4j
final class MetadataTypeTestUtils {

    static <T extends MetadataType> void accessors(final Class<T> cls, final MetadataType obj) {
        Objects.requireNonNull(cls, "cls is null");
        Objects.requireNonNull(obj, "obj is null");
        final BeanInfo info;
        try {
            info = Introspector.getBeanInfo(cls);
        } catch (final IntrospectionException ie) {
            throw new RuntimeException("failed to get beanInfo from " + cls, ie);
        }
        for (final var descriptor : info.getPropertyDescriptors()) {
            final var reader = descriptor.getReadMethod();
            if (reader == null || !MetadataType.class.isAssignableFrom(reader.getDeclaringClass())) {
                continue;
            }
            if (!reader.canAccess(obj)) {
                reader.setAccessible(true);
            }
            final Object value;
            try {
                value = reader.invoke(obj);
            } catch (final ReflectiveOperationException roe) {
                log.error("failed to invoke {}, on {}", reader, obj, roe);
                continue;
            }
            final var writer = descriptor.getWriteMethod();
            if (writer == null || !MetadataType.class.isAssignableFrom(reader.getDeclaringClass())) {
                continue;
            }
            if (!writer.canAccess(obj)) {
                writer.setAccessible(true);
            }
            try {
                writer.invoke(obj, value);
            } catch (final ReflectiveOperationException roe) {
                log.error("failed to invoke {} with {}, on {}", writer, value, obj);
            }
        }
    }

    private static <T extends MetadataType> void accessorsHelper(final Class<T> cls, final Object obj) {
        Objects.requireNonNull(cls, "cls is null");
        accessors(cls, cls.cast(obj));
    }

    static void accessors(final MetadataType obj) {
        Objects.requireNonNull(obj, "obj is null");
        accessorsHelper(obj.getClass(), obj);
    }

    private MetadataTypeTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
