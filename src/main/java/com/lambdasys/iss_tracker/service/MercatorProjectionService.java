package com.lambdasys.iss_tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MercatorProjectionService {

    public static final int MAP_WIDTH = 1280;
    public static final int MAP_HEIGHT = 640;
    private static final double MAX_WEB_MERCATOR_LAT = 85.05112878;


    public int[] toPixel(double latDeg, double lonDeg) {
        double clampedLat = Math.clamp(latDeg, -MAX_WEB_MERCATOR_LAT, MAX_WEB_MERCATOR_LAT);

        double x = (lonDeg + 180.0) / 360.0 * MAP_WIDTH;

        double latRad = Math.toRadians(clampedLat);
        double mercatorY = Math.log(Math.tan(Math.PI / 4.0 + latRad / 2.0));
        double y = (MAP_HEIGHT / 2.0) - (MAP_WIDTH / (2.0 * Math.PI)) * mercatorY;

        int pixelX = (int) Math.round(Math.clamp(x, 0, MAP_WIDTH - 1));
        int pixelY = (int) Math.round(Math.clamp(y, 0, MAP_HEIGHT - 1));

        return new int[] { pixelX, pixelY };
    }

    public int[] toPixel(String lat, String lon) {
        return toPixel(Double.parseDouble(lat), Double.parseDouble(lon));
    }

}
