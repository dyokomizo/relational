package search;

import static search.criteria.Criteria.and;
import static search.criteria.Criteria.constant;

import java.util.Map;

import search.criteria.Criteria;
import search.criteria.Criterion;
import search.criteria.Criterion.Parser;


public class SearchParser<O> {
	private final ObjectInfo<O> info;

	public SearchParser(ObjectInfo<O> info) {
		super();
		this.info = info;
	}

	@SuppressWarnings("unchecked")
	public Criterion<O> parseCriteria(Map<String, Object[]> data) {
		Criterion<O> result = constant(true);
		for (final Map.Entry<String, Object[]> entry : data.entrySet()) {
			final String name = entry.getKey();
			final Property<O, ?> property = info.get(name);
			final Object[] values = entry.getValue();
			final Criterion<O> criteria = parseCriteria(property, values);
			result = and(result, criteria);
		}
		return result;
	}

	private <E> Criterion<O> parseCriteria(Property<O, E> property,
			Object[] values) {
		final Criterion.Type type = Criterion.Type.valueOf((String) values[0]);
		final Parser<O, Criterion<O>> parser = Criteria.parserFor(type);
		return parser.parse(property, values);
	}

	@SuppressWarnings("unchecked")
	public Ordering<O> parseOrdering(String[] values) {
		if ((values.length != 1) && (values.length != 2))
			throw new IllegalArgumentException("Invalid values");
		final Ordering.Type type = (values.length == 1) ? Ordering.Type.Ascending
				: Ordering.Type.valueOf(values[0]);
		final Property<O, ?> property = (values.length == 1) ? info
				.get(values[0]) : info.get(values[1]);
		return type.create((Property<O, Comparable>) property);
	}
}