package dcode.repository;

import dcode.domain.entity.ProductType;
import dcode.domain.entity.Stock;
import dcode.model.request.ProductSaveRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @After
    public void tearDown() throws Exception {
        stockRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void 상품_ID로_재고를_가져온다() {
        //given
        int mainProductId = 1;
        String mainProductName = "신발";
        int mainProductPrice = 37000;
        ProductType mainProductType = ProductType.OPTION;
        int mainProductAmount = 6;
        String mainProductSoldOut = "F";

        int subProductId = 2;
        String subProductName = "가방";
        int subProductPrice = 59900;
        ProductType subProductType = ProductType.MAIN;
        int subProductAmount = 0;
        String subProductSoldOut = "T";

        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(mainProductId)
                .name(mainProductName)
                .price(mainProductPrice)
                .productType(mainProductType)
                .build());

        stockRepository.saveStock(ProductSaveRequest.builder()
                .id(mainProductId)
                .amount(mainProductAmount)
                .soldOut(mainProductSoldOut)
                .build());

        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(subProductId)
                .name(subProductName)
                .price(subProductPrice)
                .productType(subProductType)
                .build());

        stockRepository.saveStock(ProductSaveRequest.builder()
                .id(subProductId)
                .amount(subProductAmount)
                .soldOut(subProductSoldOut)
                .build());

        //when
        Stock stock = stockRepository.getStock(subProductId);

        //then
        assertThat(stock.getId()).isEqualTo(subProductId);
        assertThat(stock.getAmount()).isEqualTo(subProductAmount);
        assertThat(stock.getSoldOut()).isEqualTo(subProductSoldOut);
    }

    @Test
    public void 상품의_재고를_저장한다() {
        //given
        int mainProductId = 1;
        String mainProductName = "신발";
        int mainProductPrice = 37000;
        ProductType mainProductType = ProductType.OPTION;
        int mainProductAmount = 6;
        String mainProductSoldOut = "F";

        productRepository.saveProduct(ProductSaveRequest.builder()
                .id(mainProductId)
                .name(mainProductName)
                .price(mainProductPrice)
                .productType(mainProductType)
                .build());

        //when
        stockRepository.saveStock(
                ProductSaveRequest.builder()
                .id(mainProductId)
                .amount(mainProductAmount)
                .soldOut(mainProductSoldOut)
                .build()
        );

        //then
        Stock stock = stockRepository.getStock(mainProductId);
        assertThat(stock.getId()).isEqualTo(mainProductId);
        assertThat(stock.getAmount()).isEqualTo(mainProductAmount);
        assertThat(stock.getSoldOut()).isEqualTo(mainProductSoldOut);
    }
}
