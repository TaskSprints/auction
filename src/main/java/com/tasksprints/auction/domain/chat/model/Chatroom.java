package com.tasksprints.auction.domain.chat.model;

import com.tasksprints.auction.domain.auction.model.Auction;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Chatroom {
    @Id
    Long id;

    @OneToOne
    Auction auction;
}
