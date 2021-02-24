package dcode.repository;

import dcode.domain.entity.Stock;
import dcode.model.request.ProductSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;

@RequiredArgsConstructor
@Repository
public class StockRepository {
    private  final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Stock getStock(int id) {
        String query = "SELECT ID, " +
                " AMOUNT, " +
                " SOLD_OUT " +
                " FROM `stock` " +
                " WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> Stock.builder()
                        .id(rs.getInt("id"))
                        .amount(rs.getInt("amount"))
                        .soldOut(rs.getString("sold_out"))
                        .build()
        );
    }

    public Integer saveStock(ProductSaveRequest saveRequest) {
        String query = "INSERT INTO `stock`(" +
                " ID, " +
                " AMOUNT, " +
                " SOLD_OUT " +
                " ) " +
                " VALUES(" +
                " :id, " +
                " :amount, " +
                " :soldOut " +
                " )";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", saveRequest.getId());
        params.addValue("amount", saveRequest.getAmount());
        params.addValue("soldOut", saveRequest.getSoldOut());

        return namedParameterJdbcTemplate.update(query, params);
    }

    @Transactional
    public void deleteAll() {
        String query = "DELETE FROM `stock`";

        namedParameterJdbcTemplate.update(query, new HashMap<>());
    }
}
