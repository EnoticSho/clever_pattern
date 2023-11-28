package clevertec.service.impl;

import clevertec.dto.InfoProductDto;
import clevertec.dto.ProductDto;
import clevertec.entity.Product;
import clevertec.mapper.ProductMapper;
import clevertec.proxy.DaoProxyImpl;
import clevertec.service.ProductService;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final DaoProxyImpl daoProxy;
    private final ProductMapper productMapper;

    @Override
    public InfoProductDto get(UUID uuid) {
        Product byId = daoProxy.getProductById(uuid);
        return productMapper.toInfoProductDto(byId);
    }

    @Override
    public List<InfoProductDto> getAllProducts() {
        return daoProxy.getAllProducts().stream()
                .map(productMapper::toInfoProductDto)
                .toList();
    }

    @Override
    public UUID update(UUID uuid, @Valid ProductDto productDto) {
        Product byId = daoProxy.getProductById(uuid);
        Product merge = productMapper.merge(byId, productDto);
        return daoProxy.update(merge).getId();
    }

    @Override
    public UUID create(@Valid ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        product.setId(UUID.randomUUID());
        product.setCreated(LocalDateTime.now());
        return daoProxy.saveProduct(product).getId();
    }

    @Override
    public void delete(UUID uuid) {
        daoProxy.deleteProductById(uuid);
    }
}
