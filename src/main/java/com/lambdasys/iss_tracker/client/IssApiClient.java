package com.lambdasys.iss_tracker.client;

import com.lambdasys.iss_tracker.client.data.IssNowResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "iss-api", url = "http://api.open-notify.org")
public interface IssApiClient {

    @GetMapping("/iss-now.json")
    IssNowResponse getIssNow();
}
