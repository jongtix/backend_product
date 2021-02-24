package dcode.domain.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @Column
    private int id;

    @Column
    private int amount;

    @Column(name = "sold_out")
    private String soldOut;

}
