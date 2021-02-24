package dcode.domain.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
public class ProductTypeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @Column
    private int id;

    @Column(name = "sub_id")
    private int subId;

    @Column(name = "discount_ratio")
    private double discountRatio;
}
