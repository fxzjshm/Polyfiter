package net.hakugyokurou.fds.generator;

public class NormalExpressionProvider extends BasicExpressionProvider {

	public static final NormalExpressionProvider INSTANCE = new NormalExpressionProvider();
	
	public NormalExpressionProvider() {
		setNumbers(5, 9);
		setDifficulty(0.2f);
		setOperationWeights(0.4f, 0.2f, 0.3f, 0.1f);
		setValueWeights(0.7f, 0.0f, 0.3f);
	}
	
	@Override
	protected double clampReal(double d) {
		return d;
	}

	@Override
	protected int clampFractionDenominator(int d) {
		return d;
	}

}
