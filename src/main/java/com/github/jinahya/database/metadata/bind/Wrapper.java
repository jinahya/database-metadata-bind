package com.github.jinahya.database.metadata.bind;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

// http://blog.bdoughan.com/2012/11/creating-generic-list-wrapper-in-jaxb.html
// http://blog.bdoughan.com/2010/12/jaxb-and-immutable-objects.html
public final class Wrapper<T> {

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
    public static <T> List<T> unmarshal(final Class<T> type, final Source source)
            throws JAXBException {
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
     * @param name     a local part of the wrapping root element.
     * @param target   the target to which elements are marshalled.
     * @param operator an operator for decorating a marshaller; may be {@code null};
     * @param <T>      element type parameter
     * @throws JAXBException if failed to marshal.
     * @see #marshal(Class, List, String, Object)
     */
    @SuppressWarnings({"unchecked"})
    public static <T> void marshal(final Class<T> type, final List<T> elements, final String name, final Object target,
                                   final UnaryOperator<Marshaller> operator)
            throws JAXBException {
        requireNonNull(type, "type is null");
        requireNonNull(target, "target is null");
        final JAXBContext context = JAXBContext.newInstance(Wrapper.class, type);
        final JAXBElement<Wrapper<T>> wrapped = new JAXBElement<>(
                new QName(XmlConstants.NS_URI_DATABASE_METADATA_BIND, name),
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
            throw new RuntimeException("failed to marshal to " + target, roe);
        }
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
     * @param name     a local part of the wrapping root element.
     * @param target   the target to which elements are marshalled.
     * @param <T>      element type parameter
     * @throws JAXBException if failed to marshal.
     * @see #marshal(Class, List, String, Object, UnaryOperator)
     */
    public static <T> void marshal(final Class<T> type, final List<T> elements, final String name, final Object target)
            throws JAXBException {
        marshal(type, elements, name, target, null);
    }

    private static <T> Wrapper<T> of(final List<T> elements) {
        requireNonNull(elements, "elements is null");
        final Wrapper<T> instance = new Wrapper<>();
        instance.elements = elements;
        return instance;
    }

    private Wrapper() {
        super();
    }

    @XmlAnyElement(lax = true)
    private List<T> elements;
}
