package com.lambdasys.iss_tracker.service;

import com.lambdasys.iss_tracker.client.IssApiClient;
import com.lambdasys.iss_tracker.client.data.IssNowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssTrackerService {

    private final IssApiClient issApiClient;

    public IssNowResponse getCurrentIssPosition() {
        return issApiClient.getIssNow();
    }
}
