package clevertec.service;

import clevertec.data.ProductTestData;
import clevertec.dto.InfoProductDto;
import clevertec.dto.ProductDto;
import clevertec.entity.Product;
import clevertec.exception.ProductNotFoundException;
import clevertec.mapper.ProductMapper;
import clevertec.proxy.DaoProxyImpl;
import clevertec.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductMapper productMapper;
    @Mock
    private DaoProxyImpl daoProxy;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void shouldReturnInfoProductDtoWhenProductExists() {
        //Given
        Product product = ProductTestData.builder()
                .build()
                .buildProduct();
        InfoProductDto expectedDto = ProductTestData.builder()
                .build()
                .buildInfoProductDto();
        UUID id = product.getId();
        when(daoProxy.getProductById(id)).thenReturn(product);
        when(productMapper.toInfoProductDto(product)).thenReturn(expectedDto);

        //When
        InfoProductDto actualDto = productService.get(id);

        //Then
        verify(daoProxy).getProductById(id);
        verify(productMapper).toInfoProductDto(product);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void shouldReturnListOfInfoProductDtoWhenProductsExist() {
        //Given
        Product product = ProductTestData.builder()
                .build()
                .buildProduct();
        InfoProductDto infoProductDto = ProductTestData.builder()
                .build()
                .buildInfoProductDto();
        List<Product> products = Collections.singletonList(product);
        List<InfoProductDto> expected = Collections.singletonList(infoProductDto);

        when(daoProxy.getAllProducts())
                .thenReturn(products);

        when(productMapper.toInfoProductDto(product))
                .thenReturn(infoProductDto);

        //When
        List<InfoProductDto> result = productService.getAllProducts();

        //Then
        assertEquals(expected, result);
        verify(daoProxy)
                .getAllProducts();
        verify(productMapper)
                .toInfoProductDto(product);
    }

    @Test
    public void shouldReturnEmptyListWhenNoProductsExist() {
        // Given
        when(daoProxy.getAllProducts()).thenReturn(Collections.emptyList());

        // When
        List<InfoProductDto> result = productService.getAllProducts();

        // Then
        verify(daoProxy)
                .getAllProducts();
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldCreateProductAndReturnUuid() {
        //Given
        Product product = ProductTestData.builder()
                .build()
                .buildProduct();
        ProductDto productDto = ProductTestData.builder()
                .build()
                .buildProductDto();
        when(productMapper.toProduct(productDto))
                .thenReturn(product);

        when(daoProxy.saveProduct(product))
                .thenReturn(product);

        //When
        UUID result = productService.create(productDto);

        //Then
        assertEquals(product.getId(), result);
        verify(productMapper)
                .toProduct(productDto);
        verify(daoProxy)
                .saveProduct(product);
    }

    @Test
    public void shouldUpdateExistingProduct() {
        //Given
        Product product = ProductTestData.builder()
                .build()
                .buildProduct();
        ProductDto productDto = ProductTestData.builder()
                .withName("New name")
                .withPrice(1000.00)
                .build()
                .buildProductDto();
        when(daoProxy.getProductById(product.getId()))
                .thenReturn(product);
        when(productMapper.merge(product, productDto))
                .thenReturn(product);
        when(daoProxy.update(product))
                .thenReturn(product);

        //When
        UUID update = productService.update(product.getId(), productDto);

        //Then
        verify(daoProxy)
                .getProductById(product.getId());
        verify(productMapper)
                .merge(product, productDto);
        verify(daoProxy)
                .update(product);
        assertEquals(product.getId(), update);
    }

    @Test
    public void shouldDeleteExistingProduct() {
        // Given
        UUID uuid = UUID.fromString("3ecb77f7-0114-47a7-ada7-3ec685d202a7");

        // When
        productService.delete(uuid);

        // Then
        verify(daoProxy)
                .deleteProductById(uuid);
    }

    @Test
    public void ShouldThrowProductNotFoundExceptionWhenUUIDDoesNotExist() {
        // Given
        UUID uuid = UUID.fromString("3ecb77f7-0114-47a7-ada7-3ec685d202a7");
        ProductDto productDto = ProductTestData.builder()
                .build()
                .buildProductDto();
        when(daoProxy.getProductById(uuid))
                .thenThrow(new ProductNotFoundException(uuid));

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> productService.update(uuid, productDto));
        verify(daoProxy).getProductById(uuid);
        verify(daoProxy, never()).update(any(Product.class));
    }
}
