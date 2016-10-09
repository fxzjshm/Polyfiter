package net.hakugyokurou.fds.generator;

import static java.lang.Math.*;

import java.util.Random;

import net.hakugyokurou.fds.MathExpression;
import net.hakugyokurou.fds.node.BracketNode;
import net.hakugyokurou.fds.node.FractionNode;
import net.hakugyokurou.fds.node.IEvaluable;
import net.hakugyokurou.fds.node.OperationNode;
import net.hakugyokurou.fds.node.OperationNode.Operation;
import net.hakugyokurou.fds.node.RationalNode;
import net.hakugyokurou.fds.util.WeightedRandomPool;

public abstract class BasicExpressionProvider implements IGeneratorProvider {
	
	protected enum Value {
		INTEGER {
			@Override
			public IEvaluable create(Random random, BasicExpressionProvider context) {
				final int MAX = 10000;
				int l = random.nextInt(MAX);
				l = (int)(l * random.nextFloat() * context.difficulty3) + 1;
				return new RationalNode(l);
			}
		},
		RATIONAL {
			@Override
			public IEvaluable create(Random random, BasicExpressionProvider context) {
				final int MAX = 10000;
				int l = random.nextInt(MAX);
				double f = l * random.nextDouble() * context.difficulty3;
				//f = Math.round(f * 1000) * 0.001;
				return new RationalNode(context.clampReal(f));
			}
		},
		FRACTION {
			@Override
			public IEvaluable create(Random random, BasicExpressionProvider context) {
				final int MAX_D = 10;
				int d = random.nextInt(MAX_D) + 1;
				d = context.clampFractionDenominator(d);
				int n = random.nextInt(d * 2) + 1;
				return new FractionNode(n, d);
			}
		};
		
		public abstract IEvaluable create(Random random, BasicExpressionProvider context);
	}
	
	protected double JITTER = Double.MIN_NORMAL;
	
	private int numbersMin = 3, numbersMax = 3;
	private float difficulty = 0.1f, difficulty3 = difficulty * difficulty * difficulty;
	private WeightedRandomPool<Operation> operationPool = new WeightedRandomPool<Operation>();
	private WeightedRandomPool<Value> valuePool = new WeightedRandomPool<Value>();
	
	public BasicExpressionProvider() {
		operationPool.add(1.0f, Operation.ADD);
		valuePool.add(1.0f, Value.INTEGER);
	}
	
	protected void setNumbers(int numbersMin, int numbersMax) {
		this.numbersMin = max(numbersMin, 2);
		this.numbersMax = min(max(numbersMin, numbersMax), 100);
	}
	
	public void setDifficulty(float difficulty) {
		setDifficulty(difficulty, false);
	}
	
	public void setDifficulty(float difficulty, boolean canBeGreaterThanOne) {
		difficulty = max(difficulty, 0.001f);
		if(!canBeGreaterThanOne)
			difficulty = min(difficulty, 1.0f);
		this.difficulty = difficulty;
		difficulty3 = difficulty * difficulty * difficulty;
	}
	
	protected void setOperationWeights(float add, float sub, float mul, float div) {
		add = max(add, 0f);
		sub = max(sub, 0f);
		mul = max(mul, 0f);
		div = max(div, 0f);
		if(add + sub + mul + div < 0.000001)
		{
			add += 0.000001;
		}
		operationPool.clear();
		operationPool.add(add, Operation.ADD);
		operationPool.add(sub, Operation.SUB);
		operationPool.add(mul, Operation.MUL);
		operationPool.add(div, Operation.DIV);
	}
	
	protected void setValueWeights(float integer, float real, float fraction) {
		integer = max(integer, 0f);
		real = max(real, 0f);
		fraction = max(fraction, 0f);
		if(integer + real + fraction < 0.000001)
		{
			integer += 0.000001;
		}
		valuePool.clear();
		valuePool.add(integer, Value.INTEGER);
		valuePool.add(real, Value.RATIONAL);
		valuePool.add(fraction, Value.FRACTION);
	}
	
	@Override
	public MathExpression generate(int n, int max, Random random) {
		int numbers = numbersMin + random.nextInt(numbersMax - numbersMin);
		IEvaluable root = emitNode(numbers, random);
		return new MathExpression(root);
	}
	
	protected IEvaluable emitNode(int quota, Random random) {
		OperationNode operationNode = null;
		IEvaluable left = null, right = null;
		switch (quota) {
		case 1:
			return createValueNode(random);
		case 2:
			operationNode = createOperationNode(random);
			left = createValueNode(random);
			right = createValueNode(random);
		default:
			operationNode = createOperationNode(random);
			Operation operation = operationNode.getOperation();
			int quotaL = quota - 1;
			int quotaR = 1;
			left = emitNode(quotaL, random);
			right = emitNode(quotaR, random);
			if(quotaL >= 2)
				left = createBracketLeftNode((OperationNode)left, operation);
			/*if(quotaR >= 2)
				right = createBracketRightNode((OperationNode)right, operation);*/
		}
		operationNode.setLeft(left);
		operationNode.setRight(right);
		return operationNode;
	}
	
	protected IEvaluable createBracketLeftNode(OperationNode wrappedNode, Operation parentOperation) {
		if(wrappedNode.getOperation().comparePriority(parentOperation) < 0)
			return new BracketNode(wrappedNode);
		return wrappedNode;
	}
	
	protected IEvaluable createBracketRightNode(OperationNode wrappedNode, Operation parentOperation) {
		if(wrappedNode.getOperation().comparePriority(parentOperation) < 0
				|| (wrappedNode.getOperation() == Operation.SUB && parentOperation == Operation.SUB)
				|| (wrappedNode.getOperation() == Operation.DIV && parentOperation == Operation.DIV))
			return new BracketNode(wrappedNode);
		return wrappedNode;
	}
	
	protected OperationNode createOperationNode(Random random) {
		return new OperationNode(operationPool.next(random));
	}
	
	protected IEvaluable createValueNode(Random random) {
		return valuePool.next(random).create(random, this);
	}
	
	protected abstract double clampReal(double d);
	
	protected abstract int clampFractionDenominator(int d);
}
