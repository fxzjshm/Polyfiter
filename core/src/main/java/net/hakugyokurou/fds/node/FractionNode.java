package net.hakugyokurou.fds.node;

public class FractionNode implements IEvaluable {

	private final int numerator, denominator;
	
	public FractionNode(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	@Override
	public double eval() {
		return (double)numerator / (double)denominator; //Just return NaN when denominator is 0
	}

	@Override
	public String toString() {
		return "[" + numerator + "/" + denominator + "] ";
	}

	@Override
	public void verify() {
		if(denominator == 0)
			throw new InvalidExpressionException("The denominator of a fraction can't be 0.");
	}
}
