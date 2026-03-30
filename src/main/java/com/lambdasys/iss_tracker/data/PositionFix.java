package com.lambdasys.iss_tracker.data;


import com.lambdasys.iss_tracker.client.data.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@SuperBuilder
public class PositionFix extends Position<Double> {

    private Integer pixelX;
    private Integer pixelY;
    private Long timestamp;
    private Instant updateAt;

}
