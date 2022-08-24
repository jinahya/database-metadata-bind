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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.json.bind.annotation.JsonbTransient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class for binding results of {@link java.sql.DatabaseMetaData#getCatalogs()} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getCatalogs(Collection)
 */
@XmlRootElement
@ParentOf(Schema.class)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Catalog
        implements MetadataType {

    private static final long serialVersionUID = 6239185259128825953L;

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String ATTRIBUTE_NAME_TABLE_CAT = "tableCat";

    /**
     * A value for {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute for virtual instances. The value is {@value}.
     */
    public static final String COLUMN_VALUE_TABLE_CAT_EMPTY = "";

    /**
     * Creates a new <em>virtual</em> instance whose {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute is
     * {@value #COLUMN_VALUE_TABLE_CAT_EMPTY}.
     *
     * @return a new <em>virtual</em> instance.
     */
    public static Catalog newVirtualInstance() {
        return builder()
                .tableCat(COLUMN_VALUE_TABLE_CAT_EMPTY)
                .virtual(Boolean.TRUE)
                .build();
    }

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        {
            context.getSchemas(getTableCat(), "%", getSchemas());
            if (getSchemas().isEmpty()) {
                getSchemas().add(Schema.newVirtualInstance(getTableCat()));
            }
            for (final Schema schema : getSchemas()) {
                schema.retrieveChildren(context);
            }
        }
    }

    /**
     * Returns a list of schemas of this catalog.
     *
     * @return a list of schemas of this catalog; never {@code null}.
     */
    @NotNull
    public List<Schema> getSchemas() {
        if (schemas == null) {
            schemas = new ArrayList<>();
        }
        return schemas;
    }

    /**
     * Replaces current schemas of this catalog with specified value.
     *
     * @param schemas new value for {@code schemas} attribute.
     * @deprecated Use {@link #getSchemas()} whenever applicable.
     */
    @Deprecated
    // https://github.com/eclipse-ee4j/jsonb-api/issues/334
    public void setSchemas(final List<Schema> schemas) {
        this.schemas = schemas;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = false)
    private Boolean virtual;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = false, required = true)
    @NotNull
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull Schema> schemas;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Indicates whether this is catalog is a {@link #newVirtualInstance() virtual} instance or not.
     *
     * @return {@code true} if this catalog is a virtual instance; {@code false} otherwise.
     */
    @JsonbTransient
    @XmlTransient
    public boolean isVirtual() {
        final Boolean virtual = getVirtual();
        return virtual != null && virtual;
    }

    /**
     * Returns current value of {@code virtual} attribute.
     *
     * @return current value of {@code virtual} attribute.
     */
    public Boolean getVirtual() {
        return virtual;
    }

    /**
     * Replaces current value of {@code virtual} attribute with specified value.
     *
     * @param virtual new value for {@code virtual} attribute.
     */
    public void setVirtual(final Boolean virtual) {
        this.virtual = virtual;
    }

    /**
     * Returns the value of {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute.
     *
     * @return the value of {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute.
     */
    public String getTableCat() {
        return tableCat;
    }

    /**
     * Replaces current value of {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute with specified value.
     *
     * @param tableCat new value for {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute.
     */
    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }
}
