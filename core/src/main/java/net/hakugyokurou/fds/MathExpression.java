package net.hakugyokurou.fds;

import net.hakugyokurou.fds.node.IEvaluable;
import net.hakugyokurou.fds.node.InvalidExpressionException;

public class MathExpression {

	private IEvaluable root;
	
	public MathExpression() {
		this(null);
	}
	
	public MathExpression(IEvaluable root) {
		this.root = root;
	}
	
	public void setRoot(IEvaluable root) {
		this.root = root;
	}
	
	public double eval() throws InvalidExpressionException {
		return root.eval();
	}
	
	public void verify() throws InvalidExpressionException {
		root.verify();
	}

	@Override
	public String toString() {
		return root.toString();
	}
}