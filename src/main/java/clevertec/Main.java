package clevertec;

import clevertec.dao.impl.ProductDaoImpl;
import clevertec.config.dbConnection.DatabaseConnectionManager;
import clevertec.dto.InfoProductDto;
import clevertec.mapper.ProductMapperImpl;
import clevertec.proxy.DaoProxyImpl;
import clevertec.service.ProductService;
import clevertec.service.impl.ProductServiceImpl;
import clevertec.utils.XmlSerializer;

import java.io.IOException;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        ProductService service = new ProductServiceImpl(new DaoProxyImpl(new ProductDaoImpl(new DatabaseConnectionManager())), new ProductMapperImpl());
        InfoProductDto infoProductDto = service.get(UUID.fromString("1d9411b4-53cc-42fc-8eeb-ab5d4c3820ba"));
        service.get(UUID.fromString("dcce95ba-46ea-4739-887b-1de051755ac7"));
        service.get(UUID.fromString("dcce95ba-46ea-4739-887b-1de051755ac7"));
        service.get(UUID.fromString("1d9411b4-53cc-42fc-8eeb-ab5d4c3820ba"));
        service.getAllProducts().forEach(System.out::println);
        System.out.println(infoProductDto);

        InfoProductDto infoProductDto1 = service.get(UUID.fromString("1d9411b4-53cc-42fc-8eeb-ab5d4c3820ba"));
        InfoProductDto infoProductDto2 = service.get(UUID.fromString("1d9411b4-53cc-42fc-8eeb-ab5d4c3820ba"));
        InfoProductDto infoProductDto3 = service.get(UUID.fromString("1d9411b4-53cc-42fc-8eeb-ab5d4c3820ba"));

        XmlSerializer xmlSerializer = new XmlSerializer();
        String serialize = xmlSerializer.serialize(infoProductDto1);
        System.out.println(serialize);

        service.getAllProducts().forEach(System.out::println);
        service.delete(UUID.fromString("1d9411b4-53cc-42fc-8eeb-ab5d4c3820ba"));
        service.getAllProducts().forEach(System.out::println);
        System.out.println(infoProductDto);
    }
}
