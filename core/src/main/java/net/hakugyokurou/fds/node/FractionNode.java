package net.hakugyokurou.fds.node;

import java.math.BigDecimal;
import java.math.MathContext;

public class FractionNode implements IEvaluable {

    private final int numerator, denominator;

    public FractionNode(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public BigDecimal eval() {
        return new BigDecimal(numerator, OperationNode.mathContext).divide(new BigDecimal(denominator, OperationNode.mathContext), OperationNode.mathContext.getPrecision(), BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return "[" + numerator + "/" + denominator + "] ";
    }

    @Override
    public void verify() {
        if (denominator == 0)
            throw new InvalidExpressionException("The denominator of a fraction can't be 0.");
    }
}
