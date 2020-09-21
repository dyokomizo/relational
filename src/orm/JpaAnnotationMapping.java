package orm;

import java.beans.IntrospectionException;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import relational.old.Classes;
import relational.old.Relation;
import relational.old.Variable;
import relational.old.VariableImpl;
import search.ObjectInfo;
import search.Property;

public class JpaAnnotationMapping<O, R extends Relation> extends
		MappingImpl<O, R> {
	public JpaAnnotationMapping(Class<O> type, R relation) {
		super(type, relation);
		try {
			final List<Class<? super O>> classes = Classes.classes(type);
			for (Class<? super O> klass : classes) {
				addAnnotationMappings(ObjectInfo.reflect(klass));
			}
		} catch (IntrospectionException exc) {
			throw new RuntimeException(exc);
		}
	}

	private void addAnnotationMappings(ObjectInfo<? super O> info)
			throws IntrospectionException {
		final Entity entity = info.getAnnotation(Entity.class);
		if (entity != null) {
			addEntityMappings(info, entity);
		}
		final MappedSuperclass mappedSuperclass = info
				.getAnnotation(MappedSuperclass.class);
		if (mappedSuperclass != null) {
			addMappedSuperclassMappings(info, mappedSuperclass);
		}
	}

	private void addEntityMappings(ObjectInfo<? super O> info, Entity entity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(info.name());
	}

	private void addMappedSuperclassMappings(ObjectInfo<? super O> info,
			MappedSuperclass mappedSuperclass) throws IntrospectionException {
		for (final Property<? super O, ?> property : info) {
			addPropertyMapping(property);
		}
	}

	private <E> void addPropertyMapping(Property<? super O, E> property) {
		if (!property.isAnnotationPresent(Transient.class)) {
			bind(property, variableFor(property));
		}
	}

	private <E> Variable<? super R, E> variableFor(
			Property<? super O, E> property) {
		return newVariable(name(property));
	}

	private <E> String name(Property<? super O, E> property) {
		final Column column = property.getAnnotation(Column.class);
		if ((column == null) || "".equals(column.name().trim()))
			return property.name();
		return column.name();
	}

	private <E> Variable<? super R, E> newVariable(String name) {
		return new VariableImpl<R, E>(relation(), name);
	}
}