package net.hakugyokurou.fds.generator;

public class LunaticExpressionProvider extends BasicExpressionProvider {

	public static final LunaticExpressionProvider INSTANCE = new LunaticExpressionProvider();
	
	public LunaticExpressionProvider() {
		setNumbers(8, 11);
		setDifficulty(0.4f);
		setOperationWeights(0.4f, 0.2f, 0.3f, 0.1f);
		setValueWeights(0.65f, 0.0f, 0.35f);
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
