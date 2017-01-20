package net.hakugyokurou.fds.node;

import java.math.BigDecimal;

public class BracketNode implements IEvaluable {

	private IEvaluable child;
	
	public BracketNode(IEvaluable child) {
		super();
		this.child = child;
	}

	public void setChild(IEvaluable child) {
		this.child = child;
	}
	
	@Override
	public BigDecimal eval() {
		return child.eval();
	}

	@Override
	public String toString() {
		return "( " + child.toString() + ") ";
	}

	@Override
	public void verify() {
		if(child == null)
			throw new InvalidExpressionException("A empty bracket is not allowed.");
		child.verify();
	}
}
