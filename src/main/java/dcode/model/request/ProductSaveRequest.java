package dcode.model.request;

import dcode.domain.entity.ProductType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSaveRequest {
    private int id;
    private String name;
    private int price;
    private ProductType productType;

    private int amount;
    private String soldOut;

    private int subProductId;
    private double discount;
}
