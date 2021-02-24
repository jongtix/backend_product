package dcode.domain.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;    //매인 상품 시퀀스

    @Column
    private int id; //매인 상품 번호

    @Column
    private String name;    //상품명

    @Column
    private int price;  //상품 가격

    @Column
    private ProductType type;   //상품 판매 형태

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Stock stock;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private ProductTypeInfo productTypeInfo;
}
