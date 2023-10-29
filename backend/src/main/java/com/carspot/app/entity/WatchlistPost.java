package com.carspot.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="watchlist_post")
@IdClass(WatchlistPostPk.class)
public class WatchlistPost implements Serializable {

    @Id
    private long postId;
    @Id
    private long userId;

}
