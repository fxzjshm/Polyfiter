package com.entermoor.polyfiter.test;

import com.entermoor.polyfiter.utils.Polyfit;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class SpecialFunctionsTest {

    @Test
    public void testSpecialFuncs() throws IOException {
        String e = "Polyfit.parseSpecialFuncs() failed!";
        Assert.assertEquals(e, 266.6664266881124, Polyfit.parseSpecialFuncs("log10(100) * ln (sqrt(4) * ln(abs(-1096.6331584283305)) + sin(((arcsin(1)+arccos(0)+arctan(0))/2)) + 2) + 233 + 6 + 6 + 6 + tan(1.405657649) + ln(exp(4))"), 0.001);

        // Wrong things
        try {
            Polyfit.parseSpecialFuncs("idontexist(233)+666");
            Polyfit.runSpecialFunc("idontexist",233);
            Assert.assertFalse(e, true);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testGetExpressionInFunc() throws IOException {
        Assert.assertTrue("Polyfit.getExpressionInFunc() failed!", Polyfit.getExpressionInFunc("0.666 * (37 * 3) * ln (2x + 3)+ 233", "ln").equals("2x + 3"));
        Assert.assertTrue("Polyfit.getExpressionInFunc() failed!", Polyfit.getExpressionInFunc("0.666 * (37 * 3) * ln(2*exp(sqrt(7x+8))+3)+ 233", "ln").equals("2*exp(sqrt(7x+8))+3"));

    }

    @Test
    public void testeatAPairOfBrackets() throws IOException {
        String e = "Polyfit.getExpressionInFunc() failed!";
        Assert.assertTrue(e, Polyfit.eatAPairOfBrackets("wtf (Oh what is the (f***) )is that ... ?)").equals("Oh what is the (f***) "));

        // Wrong things
        try {
            Polyfit.eatAPairOfBrackets("7+8*(34*(57-63)*(29/(465+74))");
            Assert.assertFalse(e, true);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
