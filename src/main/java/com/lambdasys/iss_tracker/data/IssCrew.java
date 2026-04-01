package com.lambdasys.iss_tracker.data;

import com.lambdasys.iss_tracker.client.data.IssPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@SuperBuilder
public class IssCrew {

    private Integer number;
    private List<IssPerson> members;

}
