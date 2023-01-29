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

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;

/**
 * A class for binding results of the {@link DatabaseMetaData#getClientInfoProperties()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class ClientInfoProperty
        extends AbstractMetadataType {

    private static final long serialVersionUID = -2913230435651853254L;

    public static final String COLUMN_LABEL_NAME = "NAME";

    public static final String COLUMN_LABEL_MAX_LEN = "MAX_LEN";

    public static final String COLUMN_LABEL_DEFAULT_VALUE = "DEFAULT_VALUE";

    public static final String COLUMN_LABEL_DESCRIPTION = "DESCRIPTION";

    @ColumnLabel(COLUMN_LABEL_NAME)
    private String name;

    @ColumnLabel(COLUMN_LABEL_MAX_LEN)
    private int maxLen;

    @ColumnLabel(COLUMN_LABEL_DEFAULT_VALUE)
    private String defaultValue;

    @ColumnLabel(COLUMN_LABEL_DESCRIPTION)
    private String description;
}
