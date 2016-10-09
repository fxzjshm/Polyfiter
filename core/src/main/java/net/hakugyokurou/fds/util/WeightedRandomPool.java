package net.hakugyokurou.fds.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * 
 * 
 * @see <a href="http://stackoverflow.com/a/6409791">http://stackoverflow.com/a/6409791</a>
 * @param <E>
 */
public class WeightedRandomPool<E> {
	private final NavigableMap<Float, E> map = new TreeMap<Float, E>();
    private float total = 0;

    public void add(float weight, E result) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
    }

    public E next(Random random) {
        float value = random.nextFloat() * total;
        return map.ceilingEntry(value).getValue();
    }
    
    public void clear() {
    	map.clear();
    	total = 0f;
    }
}