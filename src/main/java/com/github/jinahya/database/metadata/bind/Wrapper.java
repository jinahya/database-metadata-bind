package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2021 Jinahya, Inc.
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

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

// http://blog.bdoughan.com/2012/11/creating-generic-list-wrapper-in-jaxb.html
// http://blog.bdoughan.com/2010/12/jaxb-and-immutable-objects.html
public final class Wrapper<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Unmarshalls a list of specified type from specified source.
     * <blockquote><pre>{@code
     * List<Category> categories = unmarshal(Category.class, new StreamSource(new File("categories.xml"));
     * }</pre></blockquote>
     *
     * @param type   the element type.
     * @param source the source from which elements are unmarshalled.
     * @param <T>    element type parameter
     * @return a list of unmarshalled instances of {@code type}.
     * @throws JAXBException if failed to unmarshal.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> List<T> unmarshal(final Class<T> type, final Source source) throws JAXBException {
        requireNonNull(type, "type is null");
        requireNonNull(source, "source is null");
        final JAXBContext context = JAXBContext.newInstance(Wrapper.class, type);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Wrapper<T> wrapper = unmarshaller.unmarshal(source, Wrapper.class).getValue();
        return wrapper.elements;
    }

    /**
     * Marshals given elements of specified type to specified target.
     * <blockquote><pre>{@code
     * List<Category> categories = getCategories();
     * marshal(Category.class, categories, "categories", new File("categories.xml");
     * }</pre></blockquote>
     *
     * @param type     the element type.
     * @param elements the elements to marshal.
     * @param target   the target to which elements are marshalled.
     * @param operator an operator for decorating a marshaller; may be {@code null};
     * @param <T>      element type parameter
     * @throws JAXBException if failed to marshal.
     * @see #marshal(Class, List, Object)
     */
    @SuppressWarnings({"unchecked"})
    public static <T> void marshal(final Class<T> type, final List<T> elements, final Object target,
                                   final UnaryOperator<Marshaller> operator)
            throws JAXBException {
        requireNonNull(type, "type is null");
        requireNonNull(target, "target is null");
        final JAXBContext context = JAXBContext.newInstance(Wrapper.class, type);
        final JAXBElement<Wrapper<T>> wrapped = new JAXBElement<>(
                new QName(XmlConstants.NS_URI_DATABASE_METADATA_BIND,
                        Introspector.decapitalize(Wrapper.class.getSimpleName())),
                (Class<Wrapper<T>>) (Class<?>) Wrapper.class, Wrapper.of(elements));
        Marshaller marshaller = context.createMarshaller();
        if (operator != null) {
            marshaller = operator.apply(marshaller);
        }
        final Method method = Arrays.stream(Marshaller.class.getMethods())
                .filter(m -> "marshal".equals(m.getName()))
                .filter(m -> {
                    final Class<?>[] parameterTypes = m.getParameterTypes();
                    return parameterTypes.length == 2 && parameterTypes[1].isAssignableFrom(target.getClass());
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no 'marshal' method found for " + target));
        try {
            method.invoke(marshaller, wrapped, target);
        } catch (final ReflectiveOperationException roe) {
            final Throwable cause = roe.getCause();
            if (roe instanceof InvocationTargetException && cause instanceof JAXBException) {
                throw (JAXBException) cause;
            }
            throw new RuntimeException("failed to marshal from " + target, roe);
        }
    }

    /**
     * Marshals given elements of specified type to specified target.
     * <blockquote><pre>{@code
     * List<Category> categories = getCategories();
     * marshal(Category.class, categories, new File("categories.xml");
     * }</pre></blockquote>
     *
     * @param type     the element type.
     * @param elements the elements to marshal.
     * @param target   the target to which elements are marshalled.
     * @param <T>      element type parameter
     * @throws JAXBException if failed to marshal.
     * @see #marshal(Class, List, Object, UnaryOperator)
     */
    public static <T> void marshal(final Class<T> type, final List<T> elements, final Object target)
            throws JAXBException {
        marshal(type, elements, target, null);
    }

    /**
     * Marshals, as formatted, given elements of specified type to specified target.
     * <blockquote><pre>{@code
     * List<Category> categories = getCategories();
     * marshalFormatted(Category.class, categories, new File("categories.xml");
     * }</pre></blockquote>
     *
     * @param type     the element type.
     * @param elements the elements to marshal.
     * @param target   the target to which elements are marshalled.
     * @param <T>      element type parameter
     * @throws JAXBException if failed to marshal.
     * @see #marshal(Class, List, Object, UnaryOperator)
     */
    public static <T> void marshalFormatted(final Class<T> type, final List<T> elements, final Object target)
            throws JAXBException {
        marshal(type, elements, target, m -> {
            try {
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            } catch (final PropertyException pe) {
//                log.warn("failed to set {}", Marshaller.JAXB_FORMATTED_OUTPUT, pe);
            }
            return m;
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static <T> Wrapper<T> of(final List<T> elements) {
        requireNonNull(elements, "elements is null");
        final Wrapper<T> instance = new Wrapper<>();
        instance.elements = elements;
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Wrapper() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAnyElement(lax = true)
    private List<T> elements;
}
