package net.hakugyokurou.fds.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import static java.lang.System.out;

import org.junit.BeforeClass;
import org.junit.Test;

import net.hakugyokurou.fds.MathExpression;
import net.hakugyokurou.fds.generator.MathExpressionGenerator;
import net.hakugyokurou.fds.node.BracketNode;
import net.hakugyokurou.fds.node.FractionNode;
import net.hakugyokurou.fds.node.IEvaluable;
import net.hakugyokurou.fds.node.NegNode;
import net.hakugyokurou.fds.node.OperationNode;
import net.hakugyokurou.fds.node.RationalNode;

public class MathExpressionGeneratorTest {
	
	private static Field root, bracketChild, negChild, operationLeft, operationRight;
	
	@BeforeClass
	public static void setup() throws Exception {
		root = MathExpression.class.getDeclaredField("root");
		bracketChild = BracketNode.class.getDeclaredField("child");
		negChild = NegNode.class.getDeclaredField("child");
		operationLeft = OperationNode.class.getDeclaredField("left");
		operationRight = OperationNode.class.getDeclaredField("right");
		root.setAccessible(true);
		bracketChild.setAccessible(true);
		negChild.setAccessible(true);
		operationLeft.setAccessible(true);
		operationRight.setAccessible(true);
	}

	@Test
	public void testGenerateEasy() throws Exception {
		MathExpression expr = MathExpressionGenerator.generateEasy();
		checkExpr(expr, 3, 6);
		for(MathExpression expression : MathExpressionGenerator.generateEasy(100))
			checkExpr(expression, 3, 6);
	}
	
	@Test
	public void testGenerateNormal() throws Exception {
		MathExpression expr = MathExpressionGenerator.generateNormal();
		checkExpr(expr, 5, 9);
		for(MathExpression expression : MathExpressionGenerator.generateNormal(100))
			checkExpr(expression, 5, 9);
	}
	
	@Test
	public void testGenerateHard() throws Exception {
		MathExpression expr = MathExpressionGenerator.generateHard();
		checkExpr(expr, 6, 11);
		for(MathExpression expression : MathExpressionGenerator.generateHard(100))
			checkExpr(expression, 6, 11);
	}
	
	@Test
	public void testGenerateLunatic() throws Exception {
		MathExpression expr = MathExpressionGenerator.generateLunatic();
		checkExpr(expr, 8, 11);
		for(MathExpression expression : MathExpressionGenerator.generateLunatic(100))
			checkExpr(expression, 8, 11);
	}
	
	private void checkExpr(MathExpression expr) {
		expr.verify();
		expr.eval();
	}
	
	private void checkExpr(MathExpression expr, int minNumbers, int maxNumbers) throws Exception {
		checkExpr(expr);
		IEvaluable rootNode = (IEvaluable) root.get(expr);
		int number = getNumbers(rootNode);
		assertTrue(number >= minNumbers && number <= maxNumbers);
	}
	
	private int getNumbers(IEvaluable parent) throws Exception  {
		if(parent instanceof FractionNode || parent instanceof RationalNode)
			return 1;
		if(parent instanceof BracketNode)
			return getNumbers((IEvaluable)bracketChild.get(parent));
		if(parent instanceof NegNode)
			return getNumbers((IEvaluable)negChild.get(parent));
		if(parent instanceof OperationNode)
			return getNumbers((IEvaluable)operationLeft.get(parent)) + getNumbers((IEvaluable)operationRight.get(parent));
		throw new IllegalArgumentException("Unknown node: " + parent.getClass());
	}
}