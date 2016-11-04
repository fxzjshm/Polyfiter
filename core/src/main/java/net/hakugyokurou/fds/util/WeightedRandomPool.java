package net.hakugyokurou.fds.util;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * 
 * 
 * @see <a href="http://stackoverflow.com/a/6409791">http://stackoverflow.com/a/6409791</a>
 * @param <E>
 */
public class WeightedRandomPool<E> {
	private final TreeMap<Float, E> map = new TreeMap<Float, E>();
    private float total = 0;

    public void add(float weight, E result) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
    }

    @SuppressWarnings("unchecked")
    public E next(Random random) {
        // float value = random.nextFloat() * total;
        // return map.ceilingEntry(value).getValue();
        Map.Entry<Float, E>[] entries = (Map.Entry<Float, E>[]) map.entrySet().toArray();
        return entries[random.nextInt(entries.length)].getValue();
    }
    
    public void clear() {
    	map.clear();
    	total = 0f;
    }
}