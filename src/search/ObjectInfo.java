package search;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ObjectInfo<O> implements AnnotatedElement,
		Iterable<Property<O, ?>> {
	public static <O> ObjectInfo<O> reflect(Class<O> klass) {
		try {
			return new ObjectInfo<O>(klass);
		} catch (IntrospectionException exc) {
			throw new RuntimeException(exc);
		}
	}

	private final BeanInfo info;

	private ObjectInfo(Class<O> klass) throws IntrospectionException {
		super();
		this.info = Introspector.getBeanInfo(klass);
	}

	public String name() {
		return type().getName();
	}

	@SuppressWarnings("unchecked")
	public Class<O> type() {
		return (Class<O>) this.info.getBeanDescriptor().getBeanClass();
	}

	public Iterator<Property<O, ?>> iterator() {
		// TODO avoid list creation
		final List<Property<O, ?>> properties = new ArrayList<Property<O, ?>>();
		for (final PropertyDescriptor descriptor : info
				.getPropertyDescriptors()) {
			properties.add(new ReflectedProperty<O, Object>(info, descriptor));
		}
		return Collections.unmodifiableList(properties).iterator();
	}

	public Property<O, ?> get(String name) throws NoSuchPropertyException {
		// TODO add caching
		for (final PropertyDescriptor descriptor : info
				.getPropertyDescriptors()) {
			if (!name.equals(descriptor.getName()))
				continue;
			return new ReflectedProperty<O, Object>(info, descriptor);
		}
		throw new NoSuchPropertyException(type(), name);
	}

	public <E> Property<O, E> get(String name, Class<E> propertyType)
			throws PropertyTypeMismatchException, NoSuchPropertyException {
		// TODO add caching
		for (final PropertyDescriptor descriptor : info
				.getPropertyDescriptors()) {
			if (!name.equals(descriptor.getName()))
				continue;
			if (!propertyType.equals(descriptor.getPropertyType()))
				throw new PropertyTypeMismatchException(type(), name,
						propertyType, descriptor.getPropertyType());
			return new ReflectedProperty<O, E>(info, descriptor);
		}
		throw new NoSuchPropertyException(type(), name);
	}

	public <E> Property<O, E> like(Property<?, E> property)
			throws PropertyTypeMismatchException, NoSuchPropertyException {
		return get(property.name(), property.type());
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return type().getAnnotation(annotationClass);
	}

	public Annotation[] getAnnotations() {
		return type().getAnnotations();
	}

	public Annotation[] getDeclaredAnnotations() {
		return type().getDeclaredAnnotations();
	}

	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		return type().isAnnotationPresent(annotationClass);
	}

	private static class ReflectedProperty<O, E> implements Property<O, E>,
			Externalizable {
		private static final long serialVersionUID = 4529281364261150578L;
		private BeanInfo info;
		private PropertyDescriptor descriptor;

		private ReflectedProperty(BeanInfo info, PropertyDescriptor descriptor) {
			super();
			this.info = info;
			this.descriptor = descriptor;
		}

		@SuppressWarnings("unchecked")
		public <V, Exc extends Exception> V accept(
				Ordering.Visitor<O, V, Exc> visitor) throws Exc {
			final Ordering<O> ascending = Ordering.Do
					.ascending((Property<O, Comparable>) this);
			return ascending.accept(visitor);
		}

		@SuppressWarnings("unchecked")
		public E get(O obj) {
			final Method getter = this.descriptor.getReadMethod();
			if (getter == null)
				throw new UnsupportedOperationException("Property <"
						+ descriptor.getName() + "> can't be read.");
			try {
				return (E) getter.invoke(obj);
			} catch (Exception exc) {
				throw new RuntimeException(exc);
			}
		}

		public void set(O obj, E value) {
			final Method setter = this.descriptor.getWriteMethod();
			if (setter == null)
				throw new UnsupportedOperationException("Property <"
						+ descriptor.getName() + "> can't be read.");
			try {
				setter.invoke(obj, value);
			} catch (Exception exc) {
				throw new RuntimeException(exc);
			}
		}

		@SuppressWarnings("unchecked")
		public Class<O> declarer() {
			return (Class<O>) info.getBeanDescriptor().getBeanClass();
		}

		public String name() {
			return descriptor.getName();
		}

		@SuppressWarnings("unchecked")
		public Class<E> type() {
			return (Class<E>) descriptor.getPropertyType();
		}

		public Property.Getter<O, E> getter() {
			return new Property.Getter<O, E>(this);
		}

		public Property.Setter<O, E> setter() {
			return new Property.Setter<O, E>(this);
		}

		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			final Method getter = this.descriptor.getReadMethod();
			if ((getter != null) && getter.isAnnotationPresent(annotationClass))
				return getter.getAnnotation(annotationClass);
			final Method setter = this.descriptor.getWriteMethod();
			if ((setter != null) && setter.isAnnotationPresent(annotationClass))
				return setter.getAnnotation(annotationClass);
			return null;
		}

		public Annotation[] getAnnotations() {
			final List<Annotation> annotations = new ArrayList<Annotation>();
			final Method getter = this.descriptor.getReadMethod();
			if (getter != null) {
				annotations.addAll(Arrays.asList(getter.getAnnotations()));
			}
			final Method setter = this.descriptor.getWriteMethod();
			if (setter != null) {
				annotations.addAll(Arrays.asList(setter.getAnnotations()));
			}
			return annotations.toArray(new Annotation[annotations.size()]);
		}

		public Annotation[] getDeclaredAnnotations() {
			final List<Annotation> annotations = new ArrayList<Annotation>();
			final Method getter = this.descriptor.getReadMethod();
			if (getter != null) {
				annotations.addAll(Arrays.asList(getter
						.getDeclaredAnnotations()));
			}
			final Method setter = this.descriptor.getWriteMethod();
			if (setter != null) {
				annotations.addAll(Arrays.asList(setter
						.getDeclaredAnnotations()));
			}
			return annotations.toArray(new Annotation[annotations.size()]);
		}

		public boolean isAnnotationPresent(
				Class<? extends Annotation> annotationClass) {
			final Method getter = this.descriptor.getReadMethod();
			final Method setter = this.descriptor.getWriteMethod();
			return ((getter != null) && getter
					.isAnnotationPresent(annotationClass))
					|| ((setter != null) && setter
							.isAnnotationPresent(annotationClass));
		}

		public int compareTo(Property<O, E> that) {
			int order = 0;
			order = order != 0 ? order : this.declarer().getName().compareTo(
					that.declarer().getName());
			order = order != 0 ? order : this.name().compareTo(that.name());
			return order;
		}

		@Override
		public String toString() {
			return declarer().getSimpleName() + "." + name();
		}

		@Override
		public int hashCode() {
			return declarer().hashCode() * 37 + name().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return (this == obj)
					|| ((obj instanceof Property) && equals((Property<?, ?>) obj));
		}

		private boolean equals(Property<?, ?> that) {
			return this.declarer().equals(that.declarer())
					&& this.name().equals(that.name());
		}

		public void readExternal(ObjectInput in) throws IOException,
				ClassNotFoundException {
			final Class<?> klass = (Class<?>) in.readObject();
			final String propertyName = (String) in.readObject();
			final ObjectInfo<?> info = reflect(klass);
			final ReflectedProperty<?, ?> property = (ReflectedProperty<?, ?>) info
					.get(propertyName);
			this.info = property.info;
			this.descriptor = property.descriptor;
		}

		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeObject(declarer());
			out.writeObject(name());
		}
	}

}