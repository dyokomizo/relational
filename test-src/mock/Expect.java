package mock;

import org.jmock.Expectations;

public interface Expect<E, Exc extends Throwable> {
	public void expect(Expectations e, E obj) throws Exc;
}