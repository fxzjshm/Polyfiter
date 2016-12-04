package com.entermoor.polyfiter.test;

import com.entermoor.polyfiter.utils.Polyfit;

import net.hakugyokurou.fds.parser.MathExpressionParser;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PolyfiterTest {

    @Test
    public void testPolyfit1() {
        Set<Polyfit.Point2> points = new LinkedHashSet<Polyfit.Point2>();
        points.add(new Polyfit.Point2(-2, -2));
        points.add(new Polyfit.Point2(-1, 1));
        points.add(new Polyfit.Point2(1, 7));
        String func = Polyfit.polyfit1(points);

        assertEquals(MathExpressionParser.parseLine(new StringReader(func.replace("x", "2"))).eval(), 10, 0.01);
    }

    @Test
    public void testPolyfitln() throws IOException {
        Set<Polyfit.Point2> points = new LinkedHashSet<Polyfit.Point2>();
        points.add(new Polyfit.Point2(1, 0));
        points.add(new Polyfit.Point2(Math.E, 1));
        points.add(new Polyfit.Point2(2, 0.693147646807594));
        String func = Polyfit.polyfitSpecialFunc("ln", points);

        assertEquals(Polyfit.parseSpecialFuncs(func.replace("x", "10")), 2.30258664183521, 0.01);
    }

    @Test
    public void testPolyfitlog10() throws IOException {
        Set<Polyfit.Point2> points = new LinkedHashSet<Polyfit.Point2>();
        points.add(new Polyfit.Point2(1, 0));
        points.add(new Polyfit.Point2(Math.E, 0.434294189773888));
        points.add(new Polyfit.Point2(10, 1));
        String func = Polyfit.polyfitSpecialFunc("log10", points);

        assertEquals(Polyfit.parseSpecialFuncs(func.replace("x", "2")), 0.301029995663981, 0.01);
    }
}
