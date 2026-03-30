package com.lambdasys.iss_tracker.client.mapper;

import com.lambdasys.iss_tracker.client.data.PositionDouble;
import com.lambdasys.iss_tracker.client.data.PositionString;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    PositionString toPositionString(Double latitude, Double longitude);
    PositionString toPositionString(String latitude, String longitude);
    PositionDouble toPositionDouble(Double latitude, Double longitude);
    PositionDouble toPositionDouble(String latitude, String longitude);
    PositionString toPositionString(PositionDouble position);
    PositionDouble toPositionDouble(PositionString position);
}
