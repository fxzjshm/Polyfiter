package com.entermoor.polyfiter.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * These classes are just for GWT.
 * Copied from source code of Collections.
 * Modified some "private" or (package) to "public".and deleted some codes that cannot be compiled.
 */
public abstract class Synchronized {
    public static class SynchronizedCollection<E> implements Collection<E>, Serializable {
        private static final long serialVersionUID = 3053995032091335093L;

        final Collection<E> c;  // Backing Collection
        final Object mutex;     // Object on which to synchronize

        public SynchronizedCollection(Collection<E> c) {
            if (c == null) throw new NullPointerException();
            this.c = c;
            mutex = this;
        }

        public SynchronizedCollection(Collection<E> c, Object mutex) {
            if (c == null || mutex == null) throw new NullPointerException();
            this.c = c;
            this.mutex = mutex;
        }

        public int size() {
            synchronized (mutex) {
                return c.size();
            }
        }

        public boolean isEmpty() {
            synchronized (mutex) {
                return c.isEmpty();
            }
        }

        public boolean contains(Object o) {
            synchronized (mutex) {
                return c.contains(o);
            }
        }

        public Object[] toArray() {
            synchronized (mutex) {
                return c.toArray();
            }
        }

        public <T> T[] toArray(T[] a) {
            synchronized (mutex) {
                return c.toArray(a);
            }
        }

        public Iterator<E> iterator() {
            return c.iterator(); // Must be manually synched by user!
        }

        public boolean add(E e) {
            synchronized (mutex) {
                return c.add(e);
            }
        }

        public boolean remove(Object o) {
            synchronized (mutex) {
                return c.remove(o);
            }
        }

        public boolean containsAll(Collection<?> coll) {
            synchronized (mutex) {
                return c.containsAll(coll);
            }
        }

        public boolean addAll(Collection<? extends E> coll) {
            synchronized (mutex) {
                return c.addAll(coll);
            }
        }

        public boolean removeAll(Collection<?> coll) {
            synchronized (mutex) {
                return c.removeAll(coll);
            }
        }

        public boolean retainAll(Collection<?> coll) {
            synchronized (mutex) {
                return c.retainAll(coll);
            }
        }

        public void clear() {
            synchronized (mutex) {
                c.clear();
            }
        }

        public String toString() {
            synchronized (mutex) {
                return c.toString();
            }
        }
    }

    public static class SynchronizedSet<E>
            extends SynchronizedCollection<E>
            implements Set<E> {
        private static final long serialVersionUID = 487447009682186044L;

        public SynchronizedSet(Set<E> s) {
            super(s);
        }

        public SynchronizedSet(Set<E> s, Object mutex) {
            super(s, mutex);
        }

        public boolean equals(Object o) {
            if (this == o)
                return true;
            synchronized (mutex) {
                return c.equals(o);
            }
        }

        public int hashCode() {
            synchronized (mutex) {
                return c.hashCode();
            }
        }
    }

    public static class SynchronizedMap<K, V>
            implements Map<K, V>, Serializable {
        private static final long serialVersionUID = 1978198479659022715L;
        final Object mutex;        // Object on which to synchronize
        private final Map<K, V> m;     // Backing Map
        private transient Set<K> keySet;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Collection<V> values;

        public SynchronizedMap(Map<K, V> m) {
            if (m == null) throw new NullPointerException();
            this.m = m;
            mutex = this;
        }

        public SynchronizedMap(Map<K, V> m, Object mutex) {
            this.m = m;
            this.mutex = mutex;
        }

        public int size() {
            synchronized (mutex) {
                return m.size();
            }
        }

        public boolean isEmpty() {
            synchronized (mutex) {
                return m.isEmpty();
            }
        }

        public boolean containsKey(Object key) {
            synchronized (mutex) {
                return m.containsKey(key);
            }
        }

        public boolean containsValue(Object value) {
            synchronized (mutex) {
                return m.containsValue(value);
            }
        }

        public V get(Object key) {
            synchronized (mutex) {
                return m.get(key);
            }
        }

        public V put(K key, V value) {
            synchronized (mutex) {
                return m.put(key, value);
            }
        }

        public V remove(Object key) {
            synchronized (mutex) {
                return m.remove(key);
            }
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            synchronized (mutex) {
                m.putAll(map);
            }
        }

        public void clear() {
            synchronized (mutex) {
                m.clear();
            }
        }

        public Set<K> keySet() {
            synchronized (mutex) {
                if (keySet == null)
                    keySet = new SynchronizedSet<K>(m.keySet(), mutex);
                return keySet;
            }
        }

        public Set<Map.Entry<K, V>> entrySet() {
            synchronized (mutex) {
                if (entrySet == null)
                    entrySet = new SynchronizedSet<Map.Entry<K, V>>(m.entrySet(), mutex);
                return entrySet;
            }
        }

        public Collection<V> values() {
            synchronized (mutex) {
                if (values == null)
                    values = new SynchronizedCollection<V>(m.values(), mutex);
                return values;
            }
        }

        public boolean equals(Object o) {
            if (this == o)
                return true;
            synchronized (mutex) {
                return m.equals(o);
            }
        }

        public int hashCode() {
            synchronized (mutex) {
                return m.hashCode();
            }
        }

        public String toString() {
            synchronized (mutex) {
                return m.toString();
            }
        }
    }
}
