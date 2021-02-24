package dcode.repository;

import dcode.domain.entity.ProductTypeInfo;
import dcode.model.request.ProductSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;

@RequiredArgsConstructor
@Repository
public class ProductTypeInfoRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductTypeInfo getProductTypeInfo(int id) {
        String query = "SELECT ID, " +
                " SUB_ID, " +
                " DISCOUNT_RATIO " +
                " FROM `productTypeInfo` " +
                " WHERE ID = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> ProductTypeInfo.builder()
                        .id(rs.getInt("id"))
                        .subId(rs.getInt("sub_id"))
                        .discountRatio(rs.getDouble("discount_ratio"))
                        .build()
        );
    }

    public Integer saveProductTypeInfo(ProductSaveRequest saveRequest) {
        String query = "INSERT INTO `productTypeInfo`( " +
                " ID, " +
                " SUB_ID, " +
                " DISCOUNT_RATIO " +
                " ) " +
                " VALUES( " +
                " :id, " +
                " :subId, " +
                " :discountRatio " +
                " )";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", saveRequest.getId());
        params.addValue("subId", saveRequest.getSubProductId());
        params.addValue("discountRatio", saveRequest.getDiscount());

        return namedParameterJdbcTemplate.update(
                query,
                params
        );
    }

    @Transactional
    public void deleteAll() {
        String query = "DELETE FROM `productTypeInfo`";

        namedParameterJdbcTemplate.update(query, new HashMap<>());
    }
}
