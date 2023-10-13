package ru.practicum.location;

import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public static LocationDto fromLocationToLocationDto(Location location) {
        if (location == null) return null;
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }

    public static Location fromLocationDtoToLocation(LocationDto locationDto) {
        if (locationDto == null) return null;
        return new Location(
                locationDto.getLat(),
                locationDto.getLon()
        );
    }
}
