package com.carspot.app.entity;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WatchlistPostPk implements Serializable {

    private long postId;
    private long userId;

}