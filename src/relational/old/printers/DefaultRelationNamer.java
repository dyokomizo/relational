package relational.old.printers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import relational.old.Classes;
import relational.old.Relation;
import relational.old.Table;

public class DefaultRelationNamer implements Namer<Relation> {
	private static final ConcurrentMap<Relation, Name> NAMES = new ConcurrentHashMap<Relation, Name>();

	public DefaultRelationNamer() {
		super();
	}

	public Name name(Relation r) {
		return nameFor(r);
	}

	public static Name nameFor(Relation r) {
		if (!NAMES.containsKey(r)) {
			final String defaultName = defaultNameFor(r);
			final String defaultAlias = defaultAliasFor(r);
			final Name name = new NameImpl(defaultName, defaultAlias);
			NAMES.putIfAbsent(r, name);
		}
		return NAMES.get(r);
	}

	private static String defaultAliasFor(Relation r) {
		final Table table = findTableAnnotation(r);
		if ((table == null) || "".equals(table.alias())) {
			final String name = defaultNameFor(r);
			return name.split("[^0-9a-zA-Z]")[0].toLowerCase();
		} else {
			return table.alias();
		}
	}

	private static String defaultNameFor(Relation r) {
		final Table table = findTableAnnotation(r);
		if ((table == null) || "".equals(table.name())) {
			return Classes.realClass(r).getSimpleName();
		} else {
			return table.name();
		}
	}

	private static Table findTableAnnotation(Relation r) {
		return Classes.findUniqueAnnotation(r.getClass(), Table.class);

	}
}