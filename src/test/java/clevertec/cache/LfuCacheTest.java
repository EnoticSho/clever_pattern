package clevertec.cache;

import clevertec.cache.impl.LfuCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LfuCacheTest {

    private LfuCache<Integer, String> cache;

    @BeforeEach
    void setUp() {
        cache = new LfuCache<>(2);
    }

    @Test
    void testPutAndGet() {
        cache.put(1, "One");
        cache.put(2, "Two");

        assertEquals("One", cache.get(1));
        assertEquals("Two", cache.get(2));
    }

    @Test
    void testEvictionPolicy() {
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.get(1);
        cache.put(3, "Three");

        assertNull(cache.get(2), "Key 2 should be evicted");
        assertEquals("One", cache.get(1));
        assertEquals("Three", cache.get(3));
    }

    @Test
    void testDelete() {
        cache.put(1, "One");
        cache.delete(1);

        assertNull(cache.get(1), "Key 1 should be deleted");
    }

    @Test
    void testCapacity() {
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");

        assertNull(cache.get(1), "Key 1 should be evicted due to capacity limits");
    }

    @Test
    void testGetNonExistentKey() {
        assertNull(cache.get(99), "Getting a non-existent key should return null");
    }

    @Test
    void testLeastFrequentUsedEviction() {
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.get(1);
        cache.get(1);
        cache.get(2);
        cache.get(2);
        cache.put(3, "Three");

        assertNull(cache.get(1), "Key 1 should be evicted as least frequently used");
        assertEquals("Two", cache.get(2));
        assertEquals("Three", cache.get(3));
    }
}
