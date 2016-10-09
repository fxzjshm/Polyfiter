package net.hakugyokurou.fds.generator;

import java.util.ArrayList;
import java.util.Random;

import net.hakugyokurou.fds.MathExpression;

public class MathExpressionGenerator {

	private static final MathExpressionGenerator EASY_INSTANCE = new MathExpressionGenerator(EasyExpressionProvider.INSTANCE);
	private static final MathExpressionGenerator NORMAL_INSTANCE = new MathExpressionGenerator(NormalExpressionProvider.INSTANCE);
	private static final MathExpressionGenerator HARD_INSTANCE = new MathExpressionGenerator(HardExpressionProvider.INSTANCE);
	private static final MathExpressionGenerator LUNATIC_INSTANCE = new MathExpressionGenerator(LunaticExpressionProvider.INSTANCE);
	
	private final IGeneratorProvider provider;
	private Random random;
	
	public MathExpressionGenerator(IGeneratorProvider provider) {
		this(provider, new Random());
	}
	
	public MathExpressionGenerator(IGeneratorProvider provider, Random random) {
		this.provider = provider;
		this.random = random;
	}
	
	public MathExpression generate() {
		return provider.generate(0, 1, random);
	}
	
	public ArrayList<MathExpression> generate(int n) {
		ArrayList<MathExpression> list = new ArrayList<MathExpression>(n);
		for(int i = 0; i < n; i++)
			list.add(provider.generate(i, n, random));
		return list;
	}
	
	public static MathExpression generateEasy() {
		return EASY_INSTANCE.generate();
	}
	
	public static MathExpression generateNormal() {
		return NORMAL_INSTANCE.generate();
	}
	
	public static MathExpression generateHard() {
		return HARD_INSTANCE.generate();
	}
	
	public static MathExpression generateLunatic() {
		return LUNATIC_INSTANCE.generate();
	}
	
	public static ArrayList<MathExpression> generateEasy(int n) {
		return EASY_INSTANCE.generate(n);
	}
	
	public static ArrayList<MathExpression> generateNormal(int n) {
		return NORMAL_INSTANCE.generate(n);
	}
	
	public static ArrayList<MathExpression> generateHard(int n) {
		return HARD_INSTANCE.generate(n);
	}
	
	public static ArrayList<MathExpression> generateLunatic(int n) {
		return LUNATIC_INSTANCE.generate(n);
	}
}