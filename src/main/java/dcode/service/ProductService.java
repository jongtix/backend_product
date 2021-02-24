package dcode.service;

import dcode.domain.entity.Product;
import dcode.domain.entity.ProductType;
import dcode.domain.entity.ProductTypeInfo;
import dcode.domain.entity.Stock;
import dcode.model.response.ProductDetailResponse;
import dcode.model.response.ProductListResponse;
import dcode.model.response.SubProductResponse;
import dcode.repository.ProductRepository;
import dcode.repository.ProductTypeInfoRepository;
import dcode.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ProductTypeInfoRepository productTypeInfoRepository;

    public ProductListResponse getProducts() {
        List<Product> productResponse = productRepository.getProduct();

        return ProductListResponse.builder()
                .productList(
                        productResponse.stream().map(product -> {

                            Stock stock = stockRepository.getStock(product.getId());

                            return SubProductResponse.builder()
                                    .id(product.getId())
                                    .name(product.getName())
                                    .price(product.getPrice())
                                    .type(product.getType())
                                    .amount(stock.getAmount())
                                    .soldOut(stock.getSoldOut())
                                    .build();
                        }).collect(Collectors.toList())
                )
                .build();
    }

    public ProductDetailResponse getProductDetail(int productId){
        return getProductDetail(productId, true);
    }

    public ProductDetailResponse getProductDetail(int productId, boolean option) {  //option: 옵션 상품 선택 여부 확인
        Product mainProduct = productRepository.getProduct(productId);
        Stock mainStock = stockRepository.getStock(productId);
        SubProductResponse subProductResponse = null;

        //서브 상품의 정보 노출 여부 처리
        if (mainProduct.getType() == ProductType.ONEMORE //상품 판매 형태가 1 + 1 상품이거나
                || mainProduct.getType() == ProductType.BUNDLE  //상품 판매 형태가 묶음 상품이거나
                || (mainProduct.getType() == ProductType.OPTION && option)) {   //상품 판매 형태가 옵션 상품이고 선택 했을 경우
            ProductTypeInfo subProductTypeInfo = productTypeInfoRepository.getProductTypeInfo(mainProduct.getId());
            Product subProduct = productRepository.getProduct(subProductTypeInfo.getSubId());
            Stock subStock = stockRepository.getStock(subProductTypeInfo.getSubId());
            subProductResponse = SubProductResponse.builder()
                            .id(subProduct.getId())
                            .name(subProduct.getName())
                            .price(subProduct.getPrice())
                            .type(subProduct.getType())
                            .amount(subStock.getAmount())
                            .soldOut(subStock.getSoldOut())
                            .discount(subProductTypeInfo.getDiscountRatio())
                            .build();
        }

        return ProductDetailResponse.builder()
                .id(mainProduct.getId())
                .name(mainProduct.getName())
                .price(mainProduct.getPrice())
                .type(mainProduct.getType())
                .amount(mainStock.getAmount())
                .soldOut(mainStock.getSoldOut())
                .subProduct(subProductResponse)
                .build();
    }

}
