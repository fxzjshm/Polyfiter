package net.hakugyokurou.fds.node;

public class NegNode implements IEvaluable{

	private IEvaluable child;
	
	public NegNode(IEvaluable child) {
		super();
		this.child = child;
	}

	public void setChild(IEvaluable child) {
		this.child = child;
	}
	
	@Override
	public double eval() {
		return -child.eval();
	}

	@Override
	public String toString() {
		return "-" + child.toString() + " ";
	}

	@Override
	public void verify() {
		if(child == null)
			throw new InvalidExpressionException("A empty negtive number is not allowed.");
		child.verify();
	}
}
