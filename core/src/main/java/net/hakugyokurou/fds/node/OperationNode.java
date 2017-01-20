package net.hakugyokurou.fds.node;

import java.math.BigDecimal;
import java.math.MathContext;

public class OperationNode implements IEvaluable {
    public static MathContext mathContext=MathContext.DECIMAL32;
	public enum Operation {
		ADD {
			@Override
			protected BigDecimal eval(BigDecimal l, BigDecimal r) {
				return l.add(r);
            }

			@Override
			protected String toString(String l, String r) {
				return l + "+ " + r;
			}

			@Override
			public int comparePriority(Operation other) {
				switch (other) {
				case MUL:
				case DIV:
					return -1;
				default:
					return 0;
				}
			}
		},
		SUB {
			@Override
			protected BigDecimal eval(BigDecimal l, BigDecimal r) {
				return l.subtract(r);
			}

			@Override
			protected String toString(String l, String r) {
				return l + "- " + r;
			}

			@Override
			public int comparePriority(Operation other) {
				switch (other) {
				case MUL:
				case DIV:
					return -1;
				default:
					return 0;
				}
			}
		},
		MUL {
			@Override
			protected BigDecimal eval(BigDecimal l, BigDecimal r) {
				return l.multiply(r);
			}

			@Override
			protected String toString(String l, String r) {
				return l + "* " + r;
			}

			@Override
			public int comparePriority(Operation other) {
				switch (other) {
				case ADD:
				case SUB:
					return 1;
				default:
					return 0;
				}
			}
		},
		DIV {
			@Override
			protected BigDecimal eval(BigDecimal l, BigDecimal r) {
                return l.divide(r, mathContext);
            }

			@Override
			protected String toString(String l, String r) {
				return l + "/ " + r;
			}

			@Override
			public int comparePriority(Operation other) {
				switch (other) {
				case ADD:
				case SUB:
					return 1;
				default:
					return 0;
				}
			}
		};

		protected abstract BigDecimal eval(BigDecimal l, BigDecimal r);
		protected abstract String toString(String l, String r);
		public abstract int comparePriority(Operation other);
	}

	private final Operation operation;
	private IEvaluable left, right;

	public OperationNode(Operation operation) {
		this.operation = operation;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setLeft(IEvaluable left) {
		this.left = left;
	}

	public void setRight(IEvaluable right) {
		this.right = right;
	}

	@Override
	public BigDecimal eval() {
		return operation.eval(left.eval(), right.eval());
	}

	@Override
	public void verify() {
		if(operation == null)
			throw new InvalidExpressionException("The operation can't be null.");
		if(left == null)
			throw new InvalidExpressionException("The left child expression can't be null.");
		if(right == null)
			throw new InvalidExpressionException("The right child expression can't be null.");
		left.verify();
		right.verify();
	}

	@Override
	public String toString() {
		return operation.toString(left.toString(), right.toString());
	}
}
