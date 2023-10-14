package ru.practicum.location;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findFirstByLatAndLon(float lat, float lon);
}