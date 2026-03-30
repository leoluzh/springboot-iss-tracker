package com.lambdasys.iss_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IssTrackerWebController {

    @GetMapping("/tracker")
    public String showIssTrackerPage() {
        return "iss-tracker";
    }

    @GetMapping("/tracker-leaflet")
    public String showIssTrackerLeafletPage() {
        return "iss-tracker-leaflet";
    }


}
