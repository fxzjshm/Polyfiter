package com.entermoor.polyfiter.utils;

import net.hakugyokurou.fds.node.InvalidExpressionException;
import net.hakugyokurou.fds.parser.MathExpressionParser;

import java.io.IOException;
import java.io.StringReader;
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
     *
     * @param points Given points
     * @return "k*x+b"
     */
    public static String polyfit1(Set<Point2> points) {
        int n = points.size();
        float sigmaX = 0;
        float sigmaY = 0;
        float sigmaXY = 0;
        float sigmaX2 = 0;

        for (Point2 point : points) {
            sigmaX += point.x;
            sigmaY += point.y;
            sigmaXY += point.x * point.y;
            sigmaX2 += point.x * point.x;
        }

        float avgX = sigmaX / n;
        float avgY = sigmaY / n;

        float k = (sigmaXY - n * avgX * avgY) / (sigmaX2 - n * avgX * avgX);
        float b = avgY - k * avgX;

        return "(" + k + ")*(x)+(" + b + ")";
    }

    /*
     * ==> f(x) = k * ln(x) + b;
     *
     * @param points Given points
     * @return "k*x+b"
     */
    //public static String polyfitln(Set<Point2> points) {

    //}

    public static double parseSpecialFuncs(String expression, ReflectWrapper reflectWrapper) throws IOException {
        for (String funcPrefix : funcPrefixes) {
            try {
                String innerExpression = getExpressionInFunc(expression, funcPrefix);
                double innerResult;
                try {
                    innerResult = MathExpressionParser.parseLine(new StringReader(innerExpression)).eval();
                } catch (InvalidExpressionException iee) {
                    String innerinnerExpression = eatAPairOfBrackets(innerExpression);
                    innerResult = parseSpecialFuncs(innerinnerExpression, reflectWrapper);
                }

                String funcName = funcPrefix;
                if (funcPrefix.startsWith("arc")) {
                    funcName = funcPrefix.replace("arc", "a");
                } else if (funcPrefix.startsWith("ln")) {
                    funcName = "log";
                }
                double result = 0;

                //Plan A: Reflect (@Deprecated)
                /*
                double result = (Double) reflectWrapper.invoke(null, "java.lang.Math", funcName, innerResult);
                return result;*/

                //Plan B: invoke directly
                if (funcName.equals("sin")) {
                    result = Math.sin(innerResult);
                } else if (funcName.equals("cos")) {
                    result = Math.cos(innerResult);
                } else if (funcName.equals("tan")) {
                    result = Math.tan(innerResult);
                } else if (funcName.equals("asin")) {
                    result = Math.asin(innerResult);
                } else if (funcName.equals("acos")) {
                    result = Math.acos(innerResult);
                } else if (funcName.equals("atan")) {
                    result = Math.atan(innerResult);
                } else if (funcName.equals("log")) {
                    result = Math.log(innerResult);
                } else if (funcName.equals("exp")) {
                    result = Math.exp(innerResult);
                } else if (funcName.equals("log10")) {
                    result = Math.log10(innerResult);
                } else if (funcName.equals("sqrt")) {
                    result = Math.sqrt(innerResult);
                } else if (funcName.equals("abs")) {
                    result = Math.abs(innerResult);
                }

                int startIndex = expression.indexOf(funcPrefix);
                int endIndex = expression.indexOf(innerExpression, startIndex) + innerExpression.length() + 1;//Avoid ")" again...
                String cleanExpression = expression.replace(expression.substring(startIndex, endIndex), "" + result);
                return MathExpressionParser.parseLine(new StringReader(cleanExpression)).eval();
            } catch (IllegalArgumentException ignored) {
            }
        }
        throw new IllegalArgumentException("Unable to parse " + expression);
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
        int nBracket = 0, startIndex = 0, endIndex = 0;

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
        String eatenExpression = expressionWithBrackets.substring(startIndex, endIndex);
        return eatenExpression;
    }

    public static String getExpressionInFunc(String wholeExpression) {
        for (String funcPrefix : funcPrefixes) {
            try {
                return getExpressionInFunc(wholeExpression, funcPrefix);
            } catch (IOException ignored) {
            }
        }
        throw new IllegalArgumentException("Cannot find any known function prefix in " + wholeExpression);
    }

    public interface ReflectWrapper {
        Object invoke(Object obj, String className, String methodName, Object... objects);
    }

    public static class Point2 {
        public double x = 0, y = 0;

        public Point2(double x, double y) {
            this.x = x;
            this.y = y;
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
