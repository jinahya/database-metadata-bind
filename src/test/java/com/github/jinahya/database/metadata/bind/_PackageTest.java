package com.github.jinahya.database.metadata.bind;

import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class _PackageTest {

    @Test
    void __() throws IOException {
        final boolean unique = ClassPath.from(_PackageTest.class.getClassLoader())
                .getAllClasses()
                .stream()
                .filter(ci -> !ci.getName().equals("module-info"))
                .filter(ci -> ci.getName().startsWith(_PackageTest.class.getPackageName()))
                .map(ClassPath.ClassInfo::load)
                .filter(MetadataType.class::isAssignableFrom)
                .filter(c -> !c.isInterface())
                .map(c -> {
                    try {
                        final var field = c.getDeclaredField("serialVersionUID");
                        if (!field.canAccess(null)) {
                            field.setAccessible(true);
                        }
                        return field.getLong(null);
                    } catch (final ReflectiveOperationException roe) {
                        throw new RuntimeException("no serialVersionUID defined within " + c, roe);
                    }
                }).allMatch(new HashSet<>()::add);
        assertThat(unique).isTrue();
    }
}
