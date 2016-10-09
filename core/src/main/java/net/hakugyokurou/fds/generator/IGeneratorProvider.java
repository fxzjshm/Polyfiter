package net.hakugyokurou.fds.generator;

import java.util.Random;

import net.hakugyokurou.fds.MathExpression;

public interface IGeneratorProvider {
	public MathExpression generate(int n, int max, Random random);
}
