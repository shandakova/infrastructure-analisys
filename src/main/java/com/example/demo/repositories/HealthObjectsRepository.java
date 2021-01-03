package com.example.demo.repositories;

import com.example.demo.entities.HealthObjects;
import com.example.demo.entities.SportObjects;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthObjectsRepository extends CrudRepository<HealthObjects, Long> {
    long count();
    List<HealthObjects> findByCluster(Integer cluster);
    List<HealthObjects> findByRatingGreaterThan(Double rating);
    List<HealthObjects> findByRatingGreaterThanAndClusterEquals(Double rating, Integer cluster);
}
