package com.lambdasys.iss_tracker.client.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IssNowResponse extends AbstractIssNowResponse<PositionString> {
}
