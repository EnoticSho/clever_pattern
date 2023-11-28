package clevertec.proxy;

import clevertec.cache.Cache;
import clevertec.cache.impl.LfuCache;
import clevertec.cache.impl.LruCache;
import clevertec.config.ConfigurationLoader;
import clevertec.dao.ProductDao;
import clevertec.entity.Product;
import clevertec.exception.ProductNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Прокси-класс для доступа к данным продуктов, инкапсулирующий логику кэширования.
 */
public class DaoProxyImpl {
    private final ProductDao productDao;
    private final Cache<UUID, Product> cache;

    /**
     * Конструктор DaoProxy.
     *
     * @param productDao DAO для работы с продуктами
     * @throws IOException если возникает ошибка при инициализации кэша
     */
    public DaoProxyImpl(ProductDao productDao) throws IOException {
        this.productDao = productDao;
        this.cache = cacheInit();
    }

    /**
     * Конструктор DaoProxy.
     *
     * @param productDao DAO для работы с продуктами
     * @param cache кэш для работы с продуктами
     */
    public DaoProxyImpl(ProductDao productDao, Cache<UUID, Product> cache) {
        this.productDao = productDao;
        this.cache = cache;
    }

    /**
     * Инициализирует кэш на основе конфигурации.
     *
     * @return Инстанс кэша
     * @throws IOException если возникает ошибка при загрузке конфигурации
     */
    private Cache<UUID, Product> cacheInit() throws IOException {
        ConfigurationLoader configLoader = new ConfigurationLoader();
        Map<String, Object> objectMap = configLoader.loadConfig();
        Map<String, Object> cache = (Map<String, Object>) objectMap.get("cache");
        int capacity = (Integer) cache.get("capacity");
        String cacheType = (String) cache.get("type");
        if (cacheType.equals("lru")) {
            return new LruCache<>(capacity);
        }
        else if (cacheType.equals("lfu")) {
            return new LfuCache<>(capacity);
        }
        return null;
    }

    /**
     * Получает продукт по его идентификатору. Сначала проверяет наличие продукта в кэше,
     * если его нет, то загружает из DAO и помещает в кэш.
     *
     * @param id Идентификатор продукта
     * @return Продукт, соответствующий идентификатору
     */
    public Product getProductById(UUID id) {
        Product product = cache.get(id);
        if (product == null) {
            product = productDao.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
            cache.put(id, product);
        }
        return product;
    }

    /**
     * Получает список всех продуктов.
     *
     * @return Список продуктов
     */
    public List<Product> getAllProducts() {
        return productDao.findALL();
    }

    /**
     * Сохраняет продукт, используя DAO, и добавляет его в кэш.
     *
     * @param product Продукт для сохранения
     * @return Сохраненный продукт
     */
    public Product saveProduct(Product product) {
        Product save = productDao.save(product);
        cache.put(product.getId(), save);
        return save;
    }

    /**
     * Обновляет продукт с помощью DAO и обновляет его в кэше.
     *
     * @param product Продукт для обновления
     * @return Обновленный продукт
     */
    public Product update(Product product) {
        Product update = productDao.update(product);
        cache.put(product.getId(), update);
        return update;
    }

    /**
     * Удаляет продукт по его идентификатору с помощью DAO и удаляет его из кэша.
     *
     * @param id Идентификатор продукта для удаления
     */
    public void deleteProductById(UUID id) {
        productDao.delete(id);
        cache.delete(id);
    }
}
