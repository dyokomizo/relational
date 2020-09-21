package relational.old.printers;

import relational.old.printers.Namer.Name;

public class NameImpl implements Namer.Name {
	private static final long serialVersionUID = 3961644936488155742L;
	private final String name;
	private final String alias;

	public NameImpl(String name, String alias) {
		super();
		this.name = name;
		this.alias = alias;
	}

	public String name() {
		return this.name;
	}

	public String alias() {
		return this.alias;
	}

	@Override
	public int hashCode() {
		return name.hashCode() * 37 + alias.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof Name) && equals((Name) obj));
	}

	private boolean equals(Name that) {
		return this.name.equals(that.name()) && this.alias.equals(that.alias());
	}

	public int compareTo(Name that) {
		int order = 0;
		order = order != 0 ? order : this.name.compareTo(that.name());
		order = order != 0 ? order : this.alias.compareTo(that.alias());
		return order;
	}

	@Override
	public String toString() {
		return (this.name + " as " + this.alias);
	}
}