package search;

import java.io.Serializable;

public class Page implements Serializable {
	private static final long serialVersionUID = 8814824816973240591L;
	public final int index;
	public final int size;

	public Page(int index, int size) {
		super();
		if (index < 0)
			throw new IllegalArgumentException(
					"Index must be non-negative, but was " + index);
		if (size < 1)
			throw new IllegalArgumentException(
					"Size must be positive, but was " + size);
		this.index = index;
		this.size = size;
	}

	public static Page page(int index, int size) {
		return new Page(index, size);
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj) || equals((Page) obj);
	}

	private boolean equals(Page that) {
		return (this.index == that.index) && (this.size == that.size);
	}

	@Override
	public int hashCode() {
		return (this.index * 37) + this.size;
	}

	@Override
	public String toString() {
		return "Page{index=" + this.index + ",size=" + this.size + "}";
	}

	public int before() {
		return index * size;
	}
}