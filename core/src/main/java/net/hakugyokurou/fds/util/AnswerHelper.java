package net.hakugyokurou.fds.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AnswerHelper {
	
	private static final Pattern RATIONAL = Pattern.compile("([-]?[0-9]+[.]?[0-9]*)");
	private static final Pattern FRACTION = Pattern.compile("([-]?)[\\s]*[\\[]?[\\s]*([0-9]+[\\s]*)/([\\s]*[0-9]+)[\\s]*[\\]]?");
	
	private AnswerHelper() {}
	
	public static double parseSingleNumber(String number) {
		number = number.trim();
		Matcher matcher = RATIONAL.matcher(number);
		if(matcher.matches())
		{
			return Double.parseDouble(matcher.group(1));
		}
		matcher = FRACTION.matcher(number);
		if(matcher.matches())
		{
			String s = matcher.group(1);
			int n = Integer.parseInt(matcher.group(2));
			int d = Integer.parseInt(matcher.group(3));
			if(d == 0)
				throw new NumberFormatException("The denominator of a fraction can't be 0.");
			return n / (double)d * (s.equals("-") ? -1.0 : 1.0);
		}
		throw new NumberFormatException("\"" + number + "\" is not a number.");
	}
	
	public static boolean checkAnswer(double answer, double correctAnswer) {
		long iA = Math.round(answer), iC = Math.round(correctAnswer), iCabs = Math.abs(iC);
		if(iA != iC)
			return false;
		if(iCabs > 100000L)
			return true;
		double delta = Math.abs(correctAnswer - answer);
		if(iCabs < 1L)
			return delta < 0.001;
		if(iCabs < 10L)
			return delta < 0.01;
		return delta < 0.1;
	}
}
