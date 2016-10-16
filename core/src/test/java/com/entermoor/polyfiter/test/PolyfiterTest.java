package com.entermoor.polyfiter.test;

import com.badlogic.gdx.math.Vector2;
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
        Set<Vector2> points = new LinkedHashSet<Vector2>();
        points.add(new Vector2(-2, -2));
        points.add(new Vector2(-1, 1));
        points.add(new Vector2(1, 7));
        String func = Polyfit.polyfit1(points);
        System.out.println(func);

        assertEquals(MathExpressionParser.parseLine(new StringReader(func.replace("x", "2"))).eval(), 10, 0.01);
    }
}
