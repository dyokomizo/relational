package mock;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;

public abstract class CreateNew<E, Exc extends Throwable> extends CustomAction {
	private static int COUNTER;
	protected final Mockery context;
	protected final Class<E> klass;

	public CreateNew(Mockery context, Class<E> klass) {
		super("Create new " + klass.getSimpleName());
		this.context = context;
		this.klass = klass;
	}

	public E invoke(Invocation invocation) throws Exc {
		final E mock = context.mock(klass, klass.getSimpleName() + COUNTER++);
		final Expectations expect = new Expectations();
		expectations(expect, mock);
		context.checking(expect);
		return mock;
	}

	protected abstract void expectations(Expectations e, E obj) throws Exc;
}