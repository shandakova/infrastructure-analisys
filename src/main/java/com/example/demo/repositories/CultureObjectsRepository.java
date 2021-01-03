package com.example.demo.repositories;

import com.example.demo.entities.CultureObjects;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CultureObjectsRepository extends CrudRepository<CultureObjects, Long> {
    @Query(value = "Select place_id from culture_objects GROUP BY place_id HAVING count(place_id)>1", nativeQuery = true)
    List<Object> getClonePlaceId();

    List<CultureObjects> findByPlaceIdIsNull();

    List<CultureObjects> findByRatingGreaterThan(Double rating);

    List<CultureObjects> findByRatingGreaterThanAndClusterEquals(Double rating, Integer cluster);

    List<CultureObjects> findByCluster(Integer cluster);
}
