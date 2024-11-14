package firstcircle.bank.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_holder")
    private String accountHolder;

    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;


    @Version
    private Long version;

}
