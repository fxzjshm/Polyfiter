package com.entermoor.polyfiter.test;

import com.entermoor.polyfiter.Polyfiter;
import com.entermoor.polyfiter.utils.Polyfit;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SpecialFunctionsTest {

    @Test
    public void testSpecialFuncs() throws IOException {
        Assert.assertEquals("Polyfit.parseSpecialFuncs() failed!", Polyfit.parseSpecialFuncs("2 * ln (2 * 7 + 3)+233+28"/*, new Polyfiter.GdxReflectionWrapper()*/), 266.6664266881124, 1);
    }

    @Test
    public void testGetExpressionInFunc() throws IOException {
        Assert.assertTrue("Polyfit.getExpressionInFunc() failed!", Polyfit.getExpressionInFunc("0.666 * (37 * 3) * ln (2x + 3)+ 233", "ln").equals("2x + 3"));
        Assert.assertTrue("Polyfit.getExpressionInFunc() failed!", Polyfit.getExpressionInFunc("0.666 * (37 * 3) * ln(2*exp(sqrt(7x+8))+3)+ 233", "ln").equals("2*exp(sqrt(7x+8))+3"));

    }

    @Test
    public void testeatAPairOfBrackets() throws IOException {
        Assert.assertTrue("Polyfit.getExpressionInFunc() failed!", Polyfit.eatAPairOfBrackets("wtf (Oh what is the (f***) )is that ... ?)").equals("Oh what is the (f***) "));
    }
}
