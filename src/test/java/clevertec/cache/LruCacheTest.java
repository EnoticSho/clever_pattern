package clevertec.cache;

import clevertec.cache.impl.LruCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LruCacheTest {

    private LruCache<Integer, String> cache;

    @BeforeEach
    public void setUp() {
        cache = new LruCache<>(2);
    }

    @Test
    public void testPutAndGet() {
        cache.put(1, "One");
        assertEquals("One", cache.get(1), "Cache should return 'One' for key 1");

        cache.put(2, "Two");
        assertEquals("Two", cache.get(2), "Cache should return 'Two' for key 2");

        cache.put(3, "Three");
        assertNull(cache.get(1), "Key 1 should be evicted as it is the least recently used");
    }

    @Test
    public void testUpdateValue() {
        cache.put(1, "One");
        cache.put(1, "Updated One");
        assertEquals("Updated One", cache.get(1), "Cache should return 'Updated One' for key 1");
    }

    @Test
    public void testDelete() {
        cache.put(1, "One");
        cache.delete(1);
        assertNull(cache.get(1), "Key 1 should be deleted from cache");
    }

    @Test
    public void testNonExistentKey() {
        assertNull(cache.get(99), "Accessing a non-existent key should return null");
    }
}
