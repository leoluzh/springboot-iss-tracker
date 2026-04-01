package com.lambdasys.iss_tracker.client.mapper;

import com.lambdasys.iss_tracker.client.data.IssAstrosResponse;
import com.lambdasys.iss_tracker.data.IssCrew;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CrewMapper {

    @Mapping(target = "members", source = "people")
    IssCrew toIssCrew(IssAstrosResponse response);

}
