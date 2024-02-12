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

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;

import static java.util.Comparator.naturalOrder;

/**
 * A class for binding results of the {@link DatabaseMetaData#getClientInfoProperties()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getClientInfoProperties()
 */
@XmlRootElement
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class ClientInfoProperty extends AbstractMetadataType {

    private static final long serialVersionUID = -2913230435651853254L;

    // -----------------------------------------------------------------------------------------------------------------
    static final Comparator<ClientInfoProperty> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(ClientInfoProperty::getName, String.CASE_INSENSITIVE_ORDER);

    static final Comparator<ClientInfoProperty> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(ClientInfoProperty::getName, naturalOrder());

    // ------------------------------------------------------------------------------------------------------------ NAME

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_NAME = "NAME";

    // --------------------------------------------------------------------------------------------------------- MAX_LEN

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_MAX_LEN = "MAX_LEN";

    // --------------------------------------------------------------------------------------------------- DEFAULT_VALUE

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_DEFAULT_VALUE = "DEFAULT_VALUE";

    // ----------------------------------------------------------------------------------------------------- DESCRIPTION

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_DESCRIPTION = "DESCRIPTION";

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_NAME)
    @EqualsAndHashCode.Include
    private String name;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_MAX_LEN)
    private Integer maxLen;

    @_ColumnLabel(COLUMN_LABEL_DEFAULT_VALUE)
    private String defaultValue;

    @_ColumnLabel(COLUMN_LABEL_DESCRIPTION)
    private String description;
}
