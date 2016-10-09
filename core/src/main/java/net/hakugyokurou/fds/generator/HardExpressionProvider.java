package net.hakugyokurou.fds.generator;

public class HardExpressionProvider extends BasicExpressionProvider {

	public static final HardExpressionProvider INSTANCE = new HardExpressionProvider();
	
	public HardExpressionProvider() {
		setNumbers(6, 11);
		setDifficulty(0.3f);
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
