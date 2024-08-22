package com.tasksprints.auction.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImage {
    @Id
    @GeneratedValue
    @Column(name = "item_image_id")
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Boolean isPrimary;

    @Column()
    private LocalDateTime createdAt;

    @Column()
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void update(String url, Boolean isPrimary) {
        this.url = url;
        this.isPrimary = isPrimary;
    }
}
