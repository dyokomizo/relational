package relational.old.expressions;

import relational.old.Term;
import relational.old.Term.Precedence;

public class Operators {
	public static enum BinaryArithmetic {
		PLUS {
			@Override
			public Term.Precedence precedence() {
				return Term.Precedence.ADDITIVE;
			}

			@Override
			public String representation() {
				return "+";
			}
		},
		MINUS {
			@Override
			public Term.Precedence precedence() {
				return Term.Precedence.ADDITIVE;
			}

			@Override
			public String representation() {
				return "-";
			}
		},
		TIMES {
			@Override
			public Term.Precedence precedence() {
				return Term.Precedence.MULTIPLICATIVE;
			}

			@Override
			public String representation() {
				return "*";
			}
		},
		DIVIDE {
			@Override
			public Term.Precedence precedence() {
				return Term.Precedence.MULTIPLICATIVE;
			}

			@Override
			public String representation() {
				return "/";
			}
		};
		public abstract Precedence precedence();

		public abstract String representation();
	}

	public static enum BinaryComparison {
		LT("<"), LE("<="), EQ("="), NE("<>"), GE(">="), GT(">");
		private final String representation;

		private BinaryComparison(String representation) {
			this.representation = representation;
		}

		public String representation() {
			return representation;
		}

	}

	public static enum TernaryComparison {
		BETWEEN;
		public String representation1() {
			return "between";
		}

		public String representation2() {
			return "and";
		}
	}

	public static enum BinaryStringMatching {
		LIKE("like"), NOT_LIKE("not like");
		private final String representation;

		private BinaryStringMatching(String representation) {
			this.representation = representation;
		}

		public String representation() {
			return representation;
		}

		public Precedence precedence() {
			return Term.Precedence.COMPARISON;
		}
	}

	public static enum UnaryComparison {
		IS_NULL("is null"), IS_NOT_NULL("is not null");
		private final String representation;

		private UnaryComparison(String representation) {
			this.representation = representation;
		}

		public String representation() {
			return representation;
		}

		public Precedence precedence() {
			return Term.Precedence.COMPARISON;
		}
	}
}