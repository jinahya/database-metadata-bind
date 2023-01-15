package com.github.jinahya.database.metadata.bind;

abstract class AreDetectedTest<T extends AreDetected> extends MetadataTypeTest<T> {

    AreDetectedTest(final Class<T> typeClass) {
        super(typeClass);
    }
}
