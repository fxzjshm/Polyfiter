package com.entermoor.polyfiter.utils;

import net.hakugyokurou.fds.node.InvalidExpressionException;
import net.hakugyokurou.fds.parser.MathExpressionParser;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Polyfit {

    public static String[] funcPrefixes = new String[]{"sin", "cos", "tan", "asin", "acos",
            "atan", "arcsin", "arccos", "arctan", "exp", "ln"/* Math.log() */, "log10",
            "sqrt", "abs"};

    /**
     * /        n
     * |        ∑(xi * yi) - n * avgX * avgY
     * |      i = 1
     * | k = ----------------------------------- ;
     * |          n
     * |          ∑(xi ^ 2) - n * (avgX ^ 2)
     * \        i = 1
     * <p>
     * b = avgY - k * avgX;
     * ==> f(x) = k * x + b;
     * </p>
     *
     * @param points Given points
     * @return "k*x+b"
     */
    public static String polyfit1(Set<Point2> points) {
        int n = points.size();
        BigDecimal sigmaX = new BigDecimal(0);
        BigDecimal sigmaY = new BigDecimal(0);
        BigDecimal sigmaXY = new BigDecimal(0);
        BigDecimal sigmaX2 = new BigDecimal(0);

        for (Point2 point : points) {
            sigmaX = sigmaX.add(point.x);
            sigmaY = sigmaY.add(point.y);
            sigmaXY = sigmaXY.add(point.x.multiply(point.y));
            sigmaX2 = sigmaX2.add(point.x.multiply(point.x));
        }

        BigDecimal avgX = sigmaX.divide(new BigDecimal(n), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal avgY = sigmaY.divide(new BigDecimal(n), 10, BigDecimal.ROUND_HALF_UP);

        BigDecimal k = sigmaXY.subtract(avgX.multiply(avgY.multiply(new BigDecimal(n)))).divide(sigmaX2.subtract(avgX.multiply(avgX.multiply(new BigDecimal(n)))), BigDecimal.ROUND_HALF_EVEN);
        BigDecimal b = avgY.subtract(k.multiply(avgX));

        return "(" + k.toPlainString() + ")*(x)+(" + b.toPlainString() + ")";
    }

    /**
     * @param points Given points
     * @return "k*[funcName](x)+b"
     */
    public static String polyfitSpecialFunc(String funcName, Set<Point2> points) {
        Set<Point2> specialPoints = new LinkedHashSet<Point2>(points.size());
        for (Point2 point : points) {
            specialPoints.add(new Point2(new BigDecimal(runSpecialFunc(funcName, point.x.doubleValue())), point.y));
        }
        return polyfit1(specialPoints).replace("x", funcName + "(x)");
    }

    public static double parseSpecialFuncs(String expression/*, ReflectWrapper reflectWrapper*/) throws IOException {
        for (String funcPrefix : funcPrefixes) {
            try {
                String innerExpression = getExpressionInFunc(expression, funcPrefix);
                BigDecimal innerResult;
                try {
                    innerResult = new BigDecimal(MathExpressionParser.parseLine(new StringReader(innerExpression)).eval());
                } catch (InvalidExpressionException iee) {
                    // try {
                    innerResult = new BigDecimal(parseSpecialFuncs(innerExpression/*, reflectWrapper*/));
                    // } catch (StackOverflowError soe) {
                    //    throw new IllegalArgumentException("Unable to parse " + expression, soe);
                    //}
                }

                String funcName = funcPrefix;
                /*
                if (funcPrefix.startsWith("arc")) {
                    funcName = funcPrefix.replace("arc", "a");
                } else if (funcPrefix.startsWith("ln")) {
                    funcName = "log";
                }
                */
                double result = 0;

                // Plan A: Reflect (@Deprecated)
                /*
                double result = (Double) reflectWrapper.invoke(null, "java.lang.Math", funcName, innerResult);
                return result;*/

                // Plan B: invoke directly
                result = runSpecialFunc(funcName, innerResult.doubleValue());

                int startIndex = expression.indexOf(funcPrefix);
                int endIndex = expression.indexOf(innerExpression, startIndex) + innerExpression.length() + 1; // Avoid ")" again...
                String cleanExpression = expression.replace(expression.substring(startIndex, endIndex), new BigDecimal(result).toPlainString());
                double finalResult;
                try {
                    finalResult = MathExpressionParser.parseLine(new StringReader(cleanExpression)).eval();
                } catch (InvalidExpressionException iee) {
                    // try {
                    finalResult = parseSpecialFuncs(cleanExpression);
                    // } catch (StackOverflowError soe) {
                    //     throw new IllegalArgumentException("Unable to parse " + expression, soe);
                    // }
                }
                return finalResult;
            } catch (IllegalArgumentException ignored) {
            }
        }
        throw new IllegalArgumentException("Unable to parse " + expression + ", no supported function.");
    }

    public static double runSpecialFunc(String funcName, double input) {
        double result;
        if (funcName.equals("sin")) {
            result = Math.sin(input);
        } else if (funcName.equals("cos")) {
            result = Math.cos(input);
        } else if (funcName.equals("tan")) {
            result = Math.tan(input);
        } else if (funcName.equals("asin") || funcName.equals("arcsin")) {
            result = Math.asin(input);
        } else if (funcName.equals("acos") || funcName.equals("arccos")) {
            result = Math.acos(input);
        } else if (funcName.equals("atan") || funcName.equals("arctan")) {
            result = Math.atan(input);
        } else if (funcName.equals("ln")) {
            result = Math.log(input);
        } else if (funcName.equals("exp")) {
            result = Math.exp(input);
        } else if (funcName.equals("log10")) {
            result = Math.log10(input);
        } else if (funcName.equals("sqrt")) {
            result = Math.sqrt(input);
        } else if (funcName.equals("abs")) {
            result = Math.abs(input);
        } else {
            throw new IllegalArgumentException("Unknown function " + funcName);
        }
        return result;
    }

    public static String getExpressionInFunc(String wholeExpression, String funcPrefix) throws IOException {
        int funcPrefixIndex = wholeExpression.indexOf(funcPrefix);
        if (funcPrefixIndex >= 0) {
            String toEatExpression = wholeExpression.substring(funcPrefixIndex);
            return eatAPairOfBrackets(toEatExpression);
        }
        throw new IllegalArgumentException("Cannot find " + funcPrefix + " in " + wholeExpression);
    }

    public static String eatAPairOfBrackets(String expressionWithBrackets) throws IOException {
        StringReader stringReader = new StringReader(expressionWithBrackets);
        char tmp;
        int nBracket = 0, startIndex = 0, endIndex;

        //First, make sure we have read one braket '('.
        while (true) {
            tmp = (char) stringReader.read();
            startIndex++;
            if ((tmp == '(')) {
                nBracket++;
                break;
            }
        }

        endIndex = startIndex;
        while (true) {
            tmp = (char) stringReader.read();
            if ('\uFFFF' == tmp) {
                throw new IllegalArgumentException("Incorrect expression: " + expressionWithBrackets);
            }
            endIndex++;
            if ((tmp == '(')) {
                nBracket++;
            } else if (tmp == ')') {
                nBracket--;
            }
            if (nBracket == 0) {
                break;
            }
        }
        endIndex--; //Avoid ')'
        return expressionWithBrackets.substring(startIndex, endIndex); // eaten expression
    }

    /*public interface ReflectWrapper {
        Object invoke(Object obj, String className, String methodName, Object... objects);
    }*/

    public static class Point2 {
        public BigDecimal x = new BigDecimal(0), y = new BigDecimal(0);

        public Point2(BigDecimal x, BigDecimal y) {
            this.x = x;
            this.y = y;
        }

        public Point2(long x, long y) {
            this.x = new BigDecimal(x);
            this.y = new BigDecimal(y);
        }
    }

    //Default wrapper -- not compatible with GWT.
    /*public static class DesktopReflectionWrapper implements Polyfit.ReflectWrapper {

        @Override
        public Object invoke(Object obj, String className, String methodName, Object... objects) {
            try {
                Class[] classes = new Class[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    classes[i] = objects[i].getClass();
                }
                return Class.forName(className).getDeclaredMethod(methodName, classes).invoke(obj, objects);
            } catch (Exception e) {
                throw new IllegalArgumentException("Error invoking " + className + "." + methodName + " (See log above)", e);
            }
        }
    }*/
}
