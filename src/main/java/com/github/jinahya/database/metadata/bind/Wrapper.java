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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A class for wrapping bound values.
 *
 * @param <T> the type of elements to wrap
 */
@XmlRootElement
// http://blog.bdoughan.com/2012/11/creating-generic-list-wrapper-in-jaxb.html
// http://blog.bdoughan.com/2010/12/jaxb-and-immutable-objects.html
public final class Wrapper<T> {

    /**
     * Marshals given elements of specified type to specified target.
     * <blockquote><pre>{@code
     * List<Category> categories = getCategories();
     * marshal(Category.class, categories, new File("categories.xml"), m -> m);
     * }</pre></blockquote>
     *
     * @param type     the element type.
     * @param elements the elements to marshal.
     * @param target   the target to which elements are marshalled.
     * @param operator an operator for decorating a marshaller.
     * @param <T>      element type parameter
     * @throws JAXBException if failed to marshal.
     * @see #marshal(Class, List, Object)
     */
    @SuppressWarnings({"unchecked"})
    public static <T> void marshal(final Class<T> type, final List<T> elements, final Object target,
                                   final UnaryOperator<Marshaller> operator)
            throws JAXBException {
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(target, "target is null");
        final JAXBContext context = JAXBContext.newInstance(Wrapper.class, type);
//        final JAXBElement<Wrapper<T>> wrapped = new JAXBElement<>(
//                new QName(XmlConstants.NS_URI_DATABASE_METADATA_BIND,
//                          Introspector.decapitalize(Wrapper.class.getSimpleName())),
//                (Class<Wrapper<T>>) (Class<?>) Wrapper.class, Wrapper.of(elements));
        final Wrapper<T> wrapper = Wrapper.of(elements);
        final Marshaller marshaller = operator.apply(context.createMarshaller());
        final Method method = _XmlBindingUtils.marshalMethod(target);
        try {
            method.invoke(marshaller, wrapper, target);
        } catch (final ReflectiveOperationException roe) {
            final Throwable cause = roe.getCause();
            if (roe instanceof InvocationTargetException && cause instanceof JAXBException) {
                throw (JAXBException) cause;
            }
            throw new RuntimeException("failed to marshal; target: " + target + "; method: " + method, roe);
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
        marshal(type, elements, target, UnaryOperator.identity());
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
                throw new RuntimeException(pe);
            }
            return m;
        });
    }

    private static final Map<Class<?>, Method> UNMARSHAL_METHODS = new HashMap<>();

    /**
     * Unmarshalls a list of specified type from specified source.
     * <blockquote><pre>{@code
     * List<Category> categories = Wrapper.unmarshal(Category.class, new File("categories.xml"));
     * }</pre></blockquote>
     *
     * @param type   the element type.
     * @param source the source from which elements are unmarshalled.
     * @param <T>    element type parameter
     * @return a list of unmarshalled instances of {@code type}.
     * @throws JAXBException if failed to unmarshal.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> List<T> unmarshal(final Class<T> type, final Object source) throws JAXBException {
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(source, "source is null");
        final JAXBContext context = JAXBContext.newInstance(Wrapper.class, type);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Method method = _XmlBindingUtils.unmarshalMethod(source);
        final Wrapper<T> wrapper;
        try {
            wrapper = (Wrapper<T>) method.invoke(unmarshaller, source);
        } catch (final ReflectiveOperationException roe) {
            final Throwable cause = roe.getCause();
            if (roe instanceof InvocationTargetException && cause instanceof JAXBException) {
                throw (JAXBException) cause;
            }
            throw new RuntimeException("failed to unmarshal; source: " + source + "; method: " + method, roe);
        }
        return wrapper.getElements();
    }

    private static <T> Wrapper<T> of(final List<T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Wrapper<T> instance = new Wrapper<>();
        instance.elements = elements;
        return instance;
    }

    private Wrapper() {
        super();
    }

    List<T> getElements() {
        if (elements == null) {
            elements = new ArrayList<>();
        }
        return elements;
    }

    @XmlAnyElement(lax = true)
    private List<T> elements;
}
