package com.tasksprints.auction.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Lob
    private String description;

    @Column
    private String category;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<Auction> auctions = new ArrayList<>();

    public void update(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
}
