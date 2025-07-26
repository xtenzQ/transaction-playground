package com.xtenzq.transactionplayground.base.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Version
    @Column(name = "version")
    private Integer version; // Nullable for better JPA compatibility

    @OneToMany(mappedBy = "fromAccount", fetch = FetchType.LAZY)
    private List<TransactionLog> outgoingTransactions;

    @OneToMany(mappedBy = "toAccount", fetch = FetchType.LAZY)
    private List<TransactionLog> incomingTransactions;

    public Account(String ownerName, BigDecimal balance) {
        this.ownerName = ownerName;
        this.balance = balance;
    }
}

