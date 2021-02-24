package dcode.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    MAIN("MAIN", "단일 상품", 1),
    ONEMORE("ONEMORE", "1+1 상품", 2),
    BUNDLE("BUNDLE", "묶음 상품", 3),
    OPTION("OPTION", "옵션 상품", 4);

    private final String title;
    private final String description;
    private final int code;

    public static ProductType getProductType(int code) {
        for (ProductType productType : ProductType.values()) {
            if (productType.getCode() == code) {
                return productType;
            }
        }
        return null;
    }

}
