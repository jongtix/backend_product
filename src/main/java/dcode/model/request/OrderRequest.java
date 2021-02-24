package dcode.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequest {
    private ProductInfoRequest productInfo;
    private int point;
    private int couponId;
    private int userId;
    private String tId; //PG사로 부터 전달 받은 트렌젝션 아이디

}
