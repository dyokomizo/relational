package relational.old;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Classes {
	private Classes() {
		super();
	}

	@SuppressWarnings("unchecked")
	public static <E, C extends Collection<Class<? super E>>> C interfaces(
			Class<E> klass, C ifaces) {
		if (klass == null)
			return ifaces;
		if (klass.isInterface())
			ifaces.add(klass);
		for (final Class<?> iface : klass.getInterfaces()) {
			interfaces((Class<E>) iface, ifaces);
		}
		interfaces((Class<E>) klass.getSuperclass(), ifaces);
		return ifaces;
	}

	public static <E> List<Class<? super E>> interfaces(Class<E> klass) {
		final List<Class<? super E>> ifaces = new ArrayList<Class<? super E>>();
		return interfaces(klass, ifaces);
	}

	@SuppressWarnings("unchecked")
	public static <E, C extends Collection<Class<? super E>>> C classes(
			Class<E> klass, C classes) {
		if ((klass == null) || klass.isInterface())
			return classes;
		classes.add(klass);
		classes((Class<E>) klass.getSuperclass(), classes);
		return classes;
	}

	public static <E> List<Class<? super E>> classes(Class<E> klass) {
		final List<Class<? super E>> classes = new ArrayList<Class<? super E>>();
		return classes(klass, classes);
	}

	@SuppressWarnings("unchecked")
	public static <E, C extends Collection<Class<? super E>>> C types(
			Class<E> klass, C types) {
		if (klass == null)
			return types;
		types.add(klass);
		for (final Class<?> iface : klass.getInterfaces()) {
			types((Class<E>) iface, types);
		}
		types((Class<E>) klass.getSuperclass(), types);
		return types;
	}

	public static <E> List<Class<? super E>> types(Class<E> klass) {
		final List<Class<? super E>> types = new ArrayList<Class<? super E>>();
		return types(klass, types);
	}

	public static <A extends Annotation> A findUniqueAnnotation(Class<?> klass,
			Class<A> annotationClass) {
		final List<A> result = annotations(klass, annotationClass);
		if (result.isEmpty()) {
			return null;
		} else if (result.size() > 1) {
			throw new IllegalArgumentException("Class <" + klass
					+ "> has too many " + annotationClass.getSimpleName()
					+ " in inheritance hierarchy.");
		} else {
			return result.get(0);
		}
	}

	public static <A extends Annotation> List<A> annotations(Class<?> klass,
			Class<A> annotationClass) {
		final List<A> result = new ArrayList<A>();
		for (final Class<?> type : types(klass)) {
			final A annotation = type.getAnnotation(annotationClass);
			if (annotation != null) {
				result.add(annotation);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <E> Class<? super E> realClass(E e) {
		if (e == null)
			return Object.class;
		return realClass((Class<E>) e.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <E> Class<? super E> realClass(Class<E> klass) {
		if (!klass.isAnonymousClass())
			return klass;
		final Class<?>[] ifaces = klass.getInterfaces();
		if ((ifaces != null) && (ifaces.length == 1))
			return (Class<? super E>) ifaces[0];
		return (Class<? super E>) klass.getSuperclass();
	}

	public static <E> List<E> list(E e, E... es) {
		final List<E> list = new ArrayList<E>(es.length + 1);
		list.add(e);
		list.addAll(Arrays.asList(es));
		return list;
	}
}