package net.hakugyokurou.fds.generator;

public class EasyExpressionProvider extends BasicExpressionProvider{
	
	public static final EasyExpressionProvider INSTANCE = new EasyExpressionProvider();
	
	public EasyExpressionProvider() {
		setNumbers(3, 6);
		setDifficulty(0.1f);
		setOperationWeights(0.5f, 0.25f, 0.125f, 0.0625f);
		setValueWeights(0.8f, 0.0f, 0.2f);
	}

	@Override
	protected double clampReal(double d) {
		return Math.round(d * 10) * 0.1;
	}

	@Override
	protected int clampFractionDenominator(int d) {
		return d + (d & 1);
	}
}
