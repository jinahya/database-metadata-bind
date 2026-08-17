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

/**
 * Constants for Jakarta XML Binding.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @apiNote This type is {@code public} because {@link #NAMESPACE_URI} is published API: the XML namespace of the bound
 * types is documented for consumers, who need it for namespace-prefix mappers, XPath expressions, and schema
 * validation. It is <em>not</em> over-exposed, even though no code outside this package references it - a scope sweep
 * limited to {@code src/main} and {@code src/test} will not see the reference and must not narrow this type.
 */
public final class JakartaXmlBindingConstants {

    /**
     * The XML namespace URI used by this package.
     *
     * @apiNote Published API. See the {@code XML and JSON Binding} wiki page, which documents this constant as the
     * namespace consumers should bind against.
     */
    public static final String NAMESPACE_URI = "https://github.com/jinahya/database-metadata-bind";

    /**
     * The preferred XML namespace prefix for {@value #NAMESPACE_URI}.
     *
     * @implNote Package-private, and currently unreferenced: {@code package-info}'s {@code @XmlSchema} declares no
     * {@code xmlns}, so this prefix is not actually emitted. See the {@code @XmlSchema} item in {@code _TODOS.asciidoc}.
     */
    static final String NAMESPACE_PREFIX = "dmb";

    private JakartaXmlBindingConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
