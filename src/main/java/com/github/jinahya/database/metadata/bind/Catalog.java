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
import java.util.Comparator;
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

    public static final Comparator<Catalog> COMPARATOR = Comparator.comparing(Catalog::getTableCat);

    public static final String LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String VALUE_TABLE_CAT_EMPTY = "";

    /**
     * Creates a new <em>virtual</em> instance whose {@code tableCat} property is {@value #VALUE_TABLE_CAT_EMPTY}.
     *
     * @return a new <em>virtual</em> instance.
     */
    public static Catalog newVirtualInstance() {
        return builder()
                .tableCat(VALUE_TABLE_CAT_EMPTY)
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
     * Indicates whether this catalog is a <em>virtual</em> instance.
     *
     * @return {@code true} if this catalog is a <em>virtual</em> instance; {@code false} otherwise.
     */
    @XmlTransient
    public boolean isVirtual() {
        return virtual != null && virtual;
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

    @XmlAttribute(required = false)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Boolean virtual;

    @XmlElement(nillable = false, required = true)
    @NotNull
    @Label(LABEL_TABLE_CAT)
    private String tableCat;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull Schema> schemas;
}
