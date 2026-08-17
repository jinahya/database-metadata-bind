package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.Objects;

final class _DatabaseMetaDataUtils {

    static _SupportsConvert supportsConvert(final DatabaseMetaData metaData, final int fromType, final int toType)
            throws SQLException {
        Objects.requireNonNull(metaData, "metaData is null");
        return new _SupportsConvert(fromType, toType, metaData.supportsConvert(fromType, toType));
    }

    static <E extends Enum<E> & SQLType> _SupportsConvert supportsConvert(final DatabaseMetaData metaData,
                                                                          final E fromType, final E toType)
            throws SQLException {
        Objects.requireNonNull(fromType, "fromType is null");
        Objects.requireNonNull(toType, "toType is null");
        return supportsConvert(
                metaData,
                Objects.requireNonNull(fromType.getVendorTypeNumber(), "fromType.getVendorTypeNumber() is null"),
                Objects.requireNonNull(toType.getVendorTypeNumber(), "toType.getVendorTypeNumber() is null")
        );
    }

    static _DeletesAreDetected deletesAreDetected(final DatabaseMetaData metaData, final int type)
            throws SQLException {
        Objects.requireNonNull(metaData, "metaData is null");
        return new _DeletesAreDetected(type, metaData.deletesAreDetected(type));
    }

    _DatabaseMetaDataUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
