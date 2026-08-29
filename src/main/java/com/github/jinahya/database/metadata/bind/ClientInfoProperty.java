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

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.io.Serial;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

/**
 * A class for binding results of the {@link DatabaseMetaData#getClientInfoProperties()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DatabaseMetaData#getClientInfoProperties()
 * @see Context#getClientInfoProperties()
 */
@_ChildOfNone
@XmlRootElement(name = "clientInfoProperty")
@XmlType(name = "clientInfoProperty")
public class ClientInfoProperty
        extends AbstractMetadataType {

    @Serial
    private static final long serialVersionUID = -2913230435651853254L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator ordering elements in the order documented by
     * {@link java.sql.DatabaseMetaData#getClientInfoProperties()}, placing {@code null} values as the specified
     * context's database sorts them.
     *
     * @param context    a context whose metadata determines the {@code null} ordering.
     * @param comparator a comparator for comparing (non-{@code null}) string values.
     * @return a comparator ordering elements in the order documented by
     * {@link java.sql.DatabaseMetaData#getClientInfoProperties()}.
     * @throws SQLException if a database access error occurs.
     * @see ContextUtils#withDatabaseNullOrdering(Context, Comparator, ContextConstants.SortDirection)
     */
    static Comparator<ClientInfoProperty> comparingInJdbcOrder(final Context context,
                                                               final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        final var s = ContextUtils.withDatabaseNullOrdering(context, comparator, ContextConstants.SortDirection.ASCENDING);
        return Comparator.comparing(ClientInfoProperty::getName, s);
    }

    // ------------------------------------------------------------------------------------------------------------ NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_NAME = "NAME";

    // --------------------------------------------------------------------------------------------------------- MAX_LEN

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_MAX_LEN = "MAX_LEN";

    // --------------------------------------------------------------------------------------------------- DEFAULT_VALUE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DEFAULT_VALUE = "DEFAULT_VALUE";

    // ----------------------------------------------------------------------------------------------------- DESCRIPTION

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DESCRIPTION = "DESCRIPTION";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    protected ClientInfoProperty() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        return super.toString() + '{' +
               "name=" + name +
               ",maxLen=" + maxLen +
               ",defaultValue=" + defaultValue +
               ",description=" + description +
               '}';
    }

    // ------------------------------------------------------------------------------------------------------------ name

    /**
     * Returns the value of {@value #COLUMN_LABEL_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_NAME} column.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_NAME} column.
     *
     * @param name the value of {@value #COLUMN_LABEL_NAME} column.
     */
    void setName(final String name) {
        this.name = name;
    }

    // --------------------------------------------------------------------------------------------------------- maxLen

    /**
     * Returns the value of {@value #COLUMN_LABEL_MAX_LEN} column.
     *
     * @return the value of {@value #COLUMN_LABEL_MAX_LEN} column.
     */
    public Integer getMaxLen() {
        return maxLen;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_MAX_LEN} column.
     *
     * @param maxLen the value of {@value #COLUMN_LABEL_MAX_LEN} column.
     */
    void setMaxLen(final Integer maxLen) {
        this.maxLen = maxLen;
    }

    // --------------------------------------------------------------------------------------------------- defaultValue

    /**
     * Returns the value of {@value #COLUMN_LABEL_DEFAULT_VALUE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DEFAULT_VALUE} column.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DEFAULT_VALUE} column.
     *
     * @param defaultValue the value of {@value #COLUMN_LABEL_DEFAULT_VALUE} column.
     */
    void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    // ----------------------------------------------------------------------------------------------------- description

    /**
     * Returns the value of {@value #COLUMN_LABEL_DESCRIPTION} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DESCRIPTION} column.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DESCRIPTION} column.
     *
     * @param description the value of {@value #COLUMN_LABEL_DESCRIPTION} column.
     */
    void setDescription(final String description) {
        this.description = description;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_NAME)
    @NotBlank
    private String name;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_MAX_LEN)
    private Integer maxLen;

    @_ColumnLabel(COLUMN_LABEL_DEFAULT_VALUE)
    private String defaultValue;

    @_ColumnLabel(COLUMN_LABEL_DESCRIPTION)
    private String description;
}
