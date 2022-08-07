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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Catalog implements MetadataType {

    private static final long serialVersionUID = 6239185259128825953L;

    public static final Comparator<Catalog> COMPARATOR = Comparator.comparing(Catalog::getTableCat);

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static Catalog of(final String tableCat) {
        final Catalog instance = new Catalog();
        instance.setTableCat(tableCat);
        return instance;
    }

    public static Catalog newVirtualInstance(final String tableCat) {
        final Catalog instance = of(tableCat);
        instance.virtual = Boolean.TRUE;
        return instance;
    }

    public static Catalog newVirtualInstance() {
        return newVirtualInstance("");
    }

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
    @Label(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull Schema> schemas;
}
