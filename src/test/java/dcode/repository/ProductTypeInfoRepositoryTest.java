package dcode.repository;

import dcode.domain.entity.ProductType;
import dcode.domain.entity.ProductTypeInfo;
import dcode.model.request.ProductSaveRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductTypeInfoRepositoryTest {

    @Autowired
    private ProductTypeInfoRepository productTypeInfoRepository;

    @Autowired
    private ProductRepository productRepository;

    @After
    public void tearDown() throws Exception {
        productTypeInfoRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void 상품_ID로_서브_상품_정보를_가져온다() {
        //given
        int mainProductId = 1;
        String mainProductName = "신발";
        int mainProductPrice = 37000;
        ProductType mainProductType = ProductType.OPTION;
        int subProductId = 2;
        String subProductName = "가방";
        int subProductPrice = 59900;
        ProductType subProductType = ProductType.MAIN;
        double discountRatio = 0.7;

        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(mainProductId)
                .name(mainProductName)
                .price(mainProductPrice)
                .productType(mainProductType)
                .build());

        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(subProductId)
                .name(subProductName)
                .price(subProductPrice)
                .productType(subProductType)
                .build());

        productTypeInfoRepository.saveProductTypeInfo(ProductSaveRequest.builder()
                .id(mainProductId)
                .subProductId(subProductId)
                .discount(discountRatio)
                .build());

        //when
        ProductTypeInfo productTypeInfo =  productTypeInfoRepository.getProductTypeInfo(mainProductId);

        //then
        assertThat(productTypeInfo.getId()).isEqualTo(mainProductId);
        assertThat(productTypeInfo.getSubId()).isEqualTo(subProductId);
        assertThat(productTypeInfo.getDiscountRatio()).isEqualTo(discountRatio);
    }

    @Test
    public void 서브_상품_정보를_저장한다() {
        //given
        int mainProductId = 1;
        String mainProductName = "신발";
        int mainProductPrice = 37000;
        ProductType mainProductType = ProductType.OPTION;
        int subProductId = 2;
        String subProductName = "가방";
        int subProductPrice = 59900;
        ProductType subProductType = ProductType.MAIN;
        double discountRatio = 0.7;

        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(mainProductId)
                .name(mainProductName)
                .price(mainProductPrice)
                .productType(mainProductType)
                .build());

        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(subProductId)
                .name(subProductName)
                .price(subProductPrice)
                .productType(subProductType)
                .build());

        //when
        productTypeInfoRepository.saveProductTypeInfo(ProductSaveRequest.builder()
                .id(mainProductId)
                .subProductId(subProductId)
                .discount(discountRatio)
                .build());

        //then
        ProductTypeInfo productTypeInfo = productTypeInfoRepository.getProductTypeInfo(mainProductId);
        assertThat(productTypeInfo.getId()).isEqualTo(mainProductId);
        assertThat(productTypeInfo.getSubId()).isEqualTo(subProductId);
        assertThat(productTypeInfo.getDiscountRatio()).isEqualTo(discountRatio);
    }
}