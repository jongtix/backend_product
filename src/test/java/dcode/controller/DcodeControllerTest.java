package dcode.controller;

import dcode.domain.entity.ProductType;
import dcode.model.request.ProductSaveRequest;
import dcode.repository.ProductRepository;
import dcode.repository.ProductTypeInfoRepository;
import dcode.repository.StockRepository;
import dcode.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DcodeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductTypeInfoRepository productTypeInfoRepository;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @After
    public void tearDown() {
        stockRepository.deleteAll();
        productTypeInfoRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void 상품_리스트_API_테스트() throws Exception {
        //given
        int firstProductId = 1;
        String firstProductName = "가방";
        int firstProductPrice = 54000;
        ProductType firstProductType = ProductType.MAIN;
        int firstProductAmount = 10;
        String firstProductSoldOut = "F";
        int firstProductSubProductId = 0;
        double firstProductDiscount = 0.7;
        ProductSaveRequest firstProductSaveRequest = ProductSaveRequest.builder()
                .id(firstProductId)
                .name(firstProductName)
                .price(firstProductPrice)
                .productType(firstProductType)
                .amount(firstProductAmount)
                .soldOut(firstProductSoldOut)
                .subProductId(firstProductSubProductId)
                .discount(firstProductDiscount)
                .build();

        productRepository.saveProduct(firstProductSaveRequest);
        stockRepository.saveStock(firstProductSaveRequest);
        productTypeInfoRepository.saveProductTypeInfo(firstProductSaveRequest);

        int secondProductId = 2;
        String secondProductName = "신발";
        int secondProductPrice = 39000;
        ProductType secondProductType = ProductType.OPTION;
        int secondProductAmount = 0;
        String secondProductSoldOut = "T";
        int secondProductSubProductId = firstProductId;
        double secondProductDiscount = 0.8;
        ProductSaveRequest secondProductSaveRequest = ProductSaveRequest.builder()
                .id(secondProductId)
                .name(secondProductName)
                .price(secondProductPrice)
                .productType(secondProductType)
                .amount(secondProductAmount)
                .soldOut(secondProductSoldOut)
                .subProductId(secondProductSubProductId)
                .discount(secondProductDiscount)
                .build();

        productRepository.saveProduct(secondProductSaveRequest);
        stockRepository.saveStock(secondProductSaveRequest);
        productTypeInfoRepository.saveProductTypeInfo(secondProductSaveRequest);

        String url = "http://localhost:" + port + "/dcode/products";

        //when
        //then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productList[0].id").value(firstProductId))
                .andExpect(jsonPath("$.productList[0].name").value(firstProductName))
                .andExpect(jsonPath("$.productList[0].price").value(firstProductPrice))
                .andExpect(jsonPath("$.productList[0].type").value(firstProductType.getTitle()))
                .andExpect(jsonPath("$.productList[0].amount").value(firstProductAmount))
                .andExpect(jsonPath("$.productList[0].soldOut").value(firstProductSoldOut))
                .andExpect(jsonPath("$.productList[1].id").value(secondProductId))
                .andExpect(jsonPath("$.productList[1].name").value(secondProductName))
                .andExpect(jsonPath("$.productList[1].price").value(secondProductPrice))
                .andExpect(jsonPath("$.productList[1].type").value(secondProductType.getTitle()))
                .andExpect(jsonPath("$.productList[1].amount").value(secondProductAmount))
                .andExpect(jsonPath("$.productList[1].soldOut").value(secondProductSoldOut));
    }

    @Test
    public void 상품_상세_API_테스트() throws Exception {
        //given
        int subProductId = 1;
        String subProductName = "신발";
        int subProductPrice = 39000;
        ProductType subProductType = ProductType.MAIN;
        int subProductAmount = 3;
        String subProductSoldOut = "F";
        int subProductSubProductId = 0;
        double subProductDiscount = 0.8;
        ProductSaveRequest subProductSaveRequest = ProductSaveRequest.builder()
                .id(subProductId)
                .name(subProductName)
                .price(subProductPrice)
                .productType(subProductType)
                .amount(subProductAmount)
                .soldOut(subProductSoldOut)
                .subProductId(subProductSubProductId)
                .build();

        productRepository.saveProduct(subProductSaveRequest);
        stockRepository.saveStock(subProductSaveRequest);

        int mainProductId = 2;
        String mainProductName = "가방";
        int mainProductPrice = 54000;
        ProductType mainProductType = ProductType.OPTION;
        int mainProductAmount = 10;
        String mainProductSoldOut = "F";
        int mainProductSubProductId = subProductId;
        ProductSaveRequest mainProductSaveRequest = ProductSaveRequest.builder()
                .id(mainProductId)
                .name(mainProductName)
                .price(mainProductPrice)
                .productType(mainProductType)
                .amount(mainProductAmount)
                .soldOut(mainProductSoldOut)
                .subProductId(mainProductSubProductId)
                .discount(subProductDiscount)
                .build();

        productRepository.saveProduct(mainProductSaveRequest);
        stockRepository.saveStock(mainProductSaveRequest);
        productTypeInfoRepository.saveProductTypeInfo(mainProductSaveRequest);

        String url = "http://localhost:" + port + "/dcode/products/" + mainProductId;

        //when
        //then
        mvc.perform(get(url))
            .andExpect(status().isOk())
                .andDo(print())
            .andExpect(jsonPath("$.id").value(mainProductId))
            .andExpect(jsonPath("$.name").value(mainProductName))
            .andExpect(jsonPath("$.price").value(mainProductPrice))
            .andExpect(jsonPath("$.type").value(mainProductType.getTitle()))
            .andExpect(jsonPath("$.amount").value(mainProductAmount))
            .andExpect(jsonPath("$.soldOut").value(mainProductSoldOut))
            .andExpect(jsonPath("$.subProduct.id").value(subProductId))
            .andExpect(jsonPath("$.subProduct.name").value(subProductName))
            .andExpect(jsonPath("$.subProduct.price").value(subProductPrice))
            .andExpect(jsonPath("$.subProduct.type").value(subProductType.getTitle()))
            .andExpect(jsonPath("$.subProduct.amount").value(subProductAmount))
            .andExpect(jsonPath("$.subProduct.soldOut").value(subProductSoldOut))
            .andExpect(jsonPath("$.subProduct.discount").value(subProductDiscount));
    }

    @Test
    public void 상품_상세_API_옵션_TRUE_테스트() throws Exception {
        //given
        boolean option = true;

        int subProductId = 1;
        String subProductName = "신발";
        int subProductPrice = 39000;
        ProductType subProductType = ProductType.MAIN;
        int subProductAmount = 3;
        String subProductSoldOut = "F";
        int subProductSubProductId = 0;
        double subProductDiscount = 0.8;
        ProductSaveRequest subProductSaveRequest = ProductSaveRequest.builder()
                .id(subProductId)
                .name(subProductName)
                .price(subProductPrice)
                .productType(subProductType)
                .amount(subProductAmount)
                .soldOut(subProductSoldOut)
                .subProductId(subProductSubProductId)
                .build();

        productRepository.saveProduct(subProductSaveRequest);
        stockRepository.saveStock(subProductSaveRequest);

        int mainProductId = 2;
        String mainProductName = "가방";
        int mainProductPrice = 54000;
        ProductType mainProductType = ProductType.OPTION;
        int mainProductAmount = 10;
        String mainProductSoldOut = "F";
        int mainProductSubProductId = subProductId;
        ProductSaveRequest mainProductSaveRequest = ProductSaveRequest.builder()
                .id(mainProductId)
                .name(mainProductName)
                .price(mainProductPrice)
                .productType(mainProductType)
                .amount(mainProductAmount)
                .soldOut(mainProductSoldOut)
                .subProductId(mainProductSubProductId)
                .discount(subProductDiscount)
                .build();

        productRepository.saveProduct(mainProductSaveRequest);
        stockRepository.saveStock(mainProductSaveRequest);
        productTypeInfoRepository.saveProductTypeInfo(mainProductSaveRequest);

        String url = "http://localhost:" + port + "/dcode/products/" + mainProductId + "/" + option;

        //when
        //then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(mainProductId))
                .andExpect(jsonPath("$.name").value(mainProductName))
                .andExpect(jsonPath("$.price").value(mainProductPrice))
                .andExpect(jsonPath("$.type").value(mainProductType.getTitle()))
                .andExpect(jsonPath("$.amount").value(mainProductAmount))
                .andExpect(jsonPath("$.soldOut").value(mainProductSoldOut))
                .andExpect(jsonPath("$.subProduct.id").value(subProductId))
                .andExpect(jsonPath("$.subProduct.name").value(subProductName))
                .andExpect(jsonPath("$.subProduct.price").value(subProductPrice))
                .andExpect(jsonPath("$.subProduct.type").value(subProductType.getTitle()))
                .andExpect(jsonPath("$.subProduct.amount").value(subProductAmount))
                .andExpect(jsonPath("$.subProduct.soldOut").value(subProductSoldOut))
                .andExpect(jsonPath("$.subProduct.discount").value(subProductDiscount));
    }

    @Test
    public void 상품_상세_API_옵션_FALSE_테스트() throws Exception {
        //given
        boolean option = false;

        int subProductId = 1;
        String subProductName = "신발";
        int subProductPrice = 39000;
        ProductType subProductType = ProductType.MAIN;
        int subProductAmount = 3;
        String subProductSoldOut = "F";
        int subProductSubProductId = 0;
        double subProductDiscount = 0.8;
        ProductSaveRequest subProductSaveRequest = ProductSaveRequest.builder()
                .id(subProductId)
                .name(subProductName)
                .price(subProductPrice)
                .productType(subProductType)
                .amount(subProductAmount)
                .soldOut(subProductSoldOut)
                .subProductId(subProductSubProductId)
                .build();

        productRepository.saveProduct(subProductSaveRequest);
        stockRepository.saveStock(subProductSaveRequest);

        int mainProductId = 2;
        String mainProductName = "가방";
        int mainProductPrice = 54000;
        ProductType mainProductType = ProductType.OPTION;
        int mainProductAmount = 10;
        String mainProductSoldOut = "F";
        int mainProductSubProductId = subProductId;
        ProductSaveRequest mainProductSaveRequest = ProductSaveRequest.builder()
                .id(mainProductId)
                .name(mainProductName)
                .price(mainProductPrice)
                .productType(mainProductType)
                .amount(mainProductAmount)
                .soldOut(mainProductSoldOut)
                .subProductId(mainProductSubProductId)
                .discount(subProductDiscount)
                .build();

        productRepository.saveProduct(mainProductSaveRequest);
        stockRepository.saveStock(mainProductSaveRequest);
        productTypeInfoRepository.saveProductTypeInfo(mainProductSaveRequest);

        String url = "http://localhost:" + port + "/dcode/products/" + mainProductId + "/" + option;

        //when
        //then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(mainProductId))
                .andExpect(jsonPath("$.name").value(mainProductName))
                .andExpect(jsonPath("$.price").value(mainProductPrice))
                .andExpect(jsonPath("$.type").value(mainProductType.getTitle()))
                .andExpect(jsonPath("$.amount").value(mainProductAmount))
                .andExpect(jsonPath("$.soldOut").value(mainProductSoldOut))
                .andExpect(jsonPath("$.subProduct").doesNotExist());
    }

}