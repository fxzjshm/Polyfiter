package net.hakugyokurou.fds.test;

import static org.junit.Assert.*;
import static java.lang.System.out;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.hakugyokurou.fds.MathExpression;
import net.hakugyokurou.fds.parser.MathExpressionParser;
import net.hakugyokurou.fds.parser.ParseException;

public class MathExpressionParserTest {

	private static final double delta = 0.0001;
	private static HashMap<String, Double> testCases;

	@BeforeClass
	public static void setUpClass() {
		testCases = new HashMap<String, Double>();
		testCases.put("15 + 4.5 * 7.5 / (5 - 3) + [4/8]", 32.375);
		testCases.put("6.6 + 0.5 * -4.4 - 4 - 1 / 15", 0.33333);
		testCases.put("8*(1 + [1/2] + [1/4]+[1/8])", 15.0);
		testCases.put("-1.", -1.0);
		testCases.put("1.", 1.0);
		testCases.put("-1.0", -1.0);
		testCases.put("[1/2]", 0.5);
	}
	
	@Test
	public void testParse() throws ParseException {
		StringBuilder sb = new StringBuilder();
		ArrayList<Double> results = new ArrayList<Double>();
		int count = 0;
		for(Entry<String, Double> entry : testCases.entrySet())
		{
			sb.append(entry.getKey()).append("\r\n");
			results.add(entry.getValue());
			count++;
		}
		sb.append("\n\r\n\r");
		StringReader reader = new StringReader(sb.toString());
		try {
			ArrayList<MathExpression> exprs = MathExpressionParser.parse(reader);
			assertEquals(count, exprs.size());
			for(int i = 0; i < count; i++)
			{
				assertEquals(results.get(i), exprs.get(i).eval(), delta);
			}
		} finally {
			reader.close();
		}
	}

	@Test
	public void testParseLine() throws ParseException {
		for(Entry<String, Double> entry : testCases.entrySet())
		{
			StringReader reader = new StringReader(entry.getKey());
			try {
				MathExpression expression = MathExpressionParser.parseLine(reader);
				assertEquals(entry.getValue(), expression.eval(), delta);
			} finally {
				reader.close();
			}
		}
	}
}