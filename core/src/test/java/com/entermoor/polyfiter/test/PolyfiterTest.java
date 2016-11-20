package com.entermoor.polyfiter.test;

import com.entermoor.polyfiter.utils.Polyfit;

import net.hakugyokurou.fds.parser.MathExpressionParser;

import org.junit.Test;

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
}
