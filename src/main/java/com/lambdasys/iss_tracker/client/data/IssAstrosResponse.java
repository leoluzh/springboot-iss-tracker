package com.lambdasys.iss_tracker.client.data;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IssAstrosResponse extends AbstractResponse {

    private Integer number;
    private List<IssPerson> people;

}
