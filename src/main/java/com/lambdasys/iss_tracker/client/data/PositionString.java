package com.lambdasys.iss_tracker.client.data;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@SuperBuilder
public class PositionString extends Position<String> {
}
