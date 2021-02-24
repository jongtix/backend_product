package dcode.repository;

import dcode.domain.entity.Product;
import dcode.domain.entity.ProductType;
import dcode.model.request.ProductSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Product> getProduct() {
        String query = "SELECT ID, " +
                " NAME, " +
                " PRICE, " +
                " TYPE, " +
                " FROM `product`";

        return namedParameterJdbcTemplate.query(
                query,
                (rs, rowNum) -> Product.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .price(rs.getInt("price"))
                        .type(ProductType.getProductType(rs.getInt("type")))
                        .build()
        );
    }

    public Product getProduct(int id) {
        String query = "SELECT ID, " +
                " NAME, " +
                " PRICE, " +
                " TYPE, " +
                " FROM `product` WHERE id = :id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> Product.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .price(rs.getInt("price"))
                        .type(ProductType.getProductType(rs.getInt("type")))
                        .build()
        );
    }

    public Integer saveProduct(ProductSaveRequest saveRequest) {
        String query = "INSERT INTO `product`(" +
                " ID," +
                " NAME," +
                " PRICE," +
                " TYPE " +
                " ) " +
                " VALUES( " +
                " :id, " +
                " :name, " +
                " :price, " +
                " :type " +
                " )";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", saveRequest.getId());
        params.addValue("name", saveRequest.getName());
        params.addValue("price", saveRequest.getPrice());
        params.addValue("type", saveRequest.getProductType().getCode());

        return namedParameterJdbcTemplate.update(query, params);
    }

    @Transactional
    public void deleteAll() {
        String query = "DELETE FROM `product`";

        namedParameterJdbcTemplate.update(query, new HashMap<>());
    }
}
