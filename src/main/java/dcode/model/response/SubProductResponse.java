package dcode.model.response;

import dcode.domain.entity.ProductType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubProductResponse {
    private int id;
    private String name;
    private int price;
    private ProductType type;

    private int amount;
    private String soldOut;

    private double discount;

}
