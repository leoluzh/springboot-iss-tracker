package com.lambdasys.iss_tracker.client.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractIssNowResponse<T> {

    private String message;
    private Long timestamp;
    @JsonProperty("iss_position")
    private T position;

}
