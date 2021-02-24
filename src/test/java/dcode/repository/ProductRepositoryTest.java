package dcode.repository;

import dcode.domain.entity.Product;
import dcode.domain.entity.ProductType;
import dcode.model.request.ProductSaveRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @After
    public void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    public void 전체_상품을_삭제한다() {
        //given
        int mainProductId = 1;
        String mainProductName = "가방";
        int mainProductPrice = 54000;
        ProductType mainProductType = ProductType.ONEMORE;
        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(mainProductId)
                .name(mainProductName)
                .price(mainProductPrice)
                .productType(mainProductType)
                .build());

        int subProductId = 2;
        String subProductName = "신발";
        int subProductPrice = 39000;
        ProductType subProductType = ProductType.MAIN;
        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(subProductId)
                .name(subProductName)
                .price(subProductPrice)
                .productType(subProductType)
                .build());

        //when
        productRepository.deleteAll();

        //then
        assertThat(productRepository.getProduct().size()).isEqualTo(0);
    }

    @Test
    public void 전체_상품을_가져온다() {
        //given
        int id = 1;
        String name = "테스트";
        ProductType productType = ProductType.MAIN;

        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(id)
                .name(name)
                .productType(productType)
                .build());

        //when
        List<Product> productList = productRepository.getProduct();

        //then
        assertThat(productList.size()).isGreaterThan(0);
        assertThat(productList.get(0).getName()).isEqualTo(name);
        assertThat(productList.get(0).getType()).isEqualTo(productType);
    }

    @Test
    public void 상품_ID로_상품을_가져온다() {
        //given
        int id = 1;
        String name = "가방";
        int price = 54000;
        ProductType productType = ProductType.OPTION;
        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(id)
                .name(name)
                .price(price)
                .productType(productType)
                .build());

        //when
        Product product = productRepository.getProduct(id);

        //then
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getType()).isEqualTo(productType);
    }

    @Test
    public void 상품을_저장한다() {
        //given
        int id = 1;
        String name = "신발";
        int price = 39000;
        ProductType productType = ProductType.MAIN;

        //when
        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(id)
                .name(name)
                .price(price)
                .productType(productType)
                .build());

        //then
        Product product = productRepository.getProduct(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getType()).isEqualTo(productType);
    }
}