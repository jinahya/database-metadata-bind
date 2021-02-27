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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getCatalogs()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getCatalogs(Collection)
 */
@XmlRootElement
public class Catalog implements MetadataType {

    // -----------------------------------------------------------------------------------------------------------------
    private static final long serialVersionUID = 6239185259128825953L;

    // -------------------------------------------------------------------------------------------- TABLE_CAT / tableCat
    public static final String COLUMN_NAME_TABLE_CAT = "TABLE_CAT";

    public static final String ATTRIBUTE_NAME_TABLE_CAT = "tableCat";

    // --------------------------------------------------------------------------------------------------------- schemas
    public static final String ATTRIBUTE_NAME_SCHEMAS = "schemas";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A comparator comparing {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute.
     *
     * @see #COMPARING_TABLE_CAT_CASE_INSENSITIVE
     * @see #comparingTableCat(Collator)
     */
    public static final Comparator<Catalog> COMPARING_TABLE_CAT = Comparator.comparing(Catalog::getTableCat);

    /**
     * A comparator comparing {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute in {@link String#CASE_INSENSITIVE_ORDER case
     * insensitive order}.
     *
     * @see #COMPARING_TABLE_CAT
     * @see #comparingTableCat(Collator)
     */
    // https://stackoverflow.com/a/49821834/330457
    public static final Comparator<Catalog> COMPARING_TABLE_CAT_CASE_INSENSITIVE
            = Comparator.comparing(Catalog::getTableCat, String.CASE_INSENSITIVE_ORDER);

    /**
     * Returns a comparator comparing {@value #ATTRIBUTE_NAME_TABLE_CAT} attribute using specified collator.
     *
     * @param collator the collator.
     * @return a comparator.
     * @see #COMPARING_TABLE_CAT
     * @see #COMPARING_TABLE_CAT_CASE_INSENSITIVE
     */
    // https://stackoverflow.com/a/49821830/330457
    public static Comparator<Catalog> comparingTableCat(final Collator collator) {
        requireNonNull(collator, "collator is null");
        return Comparator.comparing(Catalog::getTableCat, collator);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Catalog newVirtualInstance() {
        final Catalog instance = new Catalog();
        instance.virtual = Boolean.TRUE;
        instance.setTableCat("");
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public Catalog() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + ATTRIBUTE_NAME_TABLE_CAT + '=' + tableCat
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Catalog that = (Catalog) obj;
        return Objects.equals(tableCat, that.tableCat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat);
    }

    // --------------------------------------------------------------------------------------------------------- virtual

    /**
     * Indicates whether this instance is a virtual instance.
     *
     * @return {@code true} if this instance is a virtual instance; {@code false} otherwise.
     */
    public boolean isVirtual() {
        return virtual != null && virtual;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    /**
     * Returns the current value of {@code tableCat} property.
     *
     * @return the current value of {@code tableCat} property.
     */
    public String getTableCat() {
        return tableCat;
    }

    /**
     * Replaces the value of {@code tableCat} property with given.
     *
     * @param tableCat new value for {@code tableCat} property.
     */
    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // --------------------------------------------------------------------------------------------------------- schemas

    /**
     * Returns schemas of this catalog.
     *
     * @return a list of schemas
     */
    List<Schema> getSchemas() {
        if (schemas == null) {
            schemas = new ArrayList<>();
        }
        return schemas;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute(required = false)
    Boolean virtual = Boolean.FALSE;

    @XmlElement(required = true)
    @NotBlank
    @Label(COLUMN_NAME_TABLE_CAT)
    @Bind(label = COLUMN_NAME_TABLE_CAT)
    private String tableCat;

    @XmlElementWrapper(required = true)
    @XmlElementRef
    private List<@Valid @NotNull Schema> schemas;
}
