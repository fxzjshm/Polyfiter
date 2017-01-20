package net.hakugyokurou.fds.node;

import java.math.BigDecimal;
import java.math.MathContext;

public interface IEvaluable {

	BigDecimal eval();
	
	void verify();
}