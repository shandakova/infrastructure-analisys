package com.example.demo.repositories;

import com.example.demo.entities.CultureObjects;
import com.example.demo.entities.SportObjects;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportObjectsRepository extends CrudRepository<SportObjects, Long> {
    List<SportObjects> findByCluster(Integer cluster);
    List<SportObjects> findByRatingGreaterThan(Double rating);
    List<SportObjects> findByRatingGreaterThanAndClusterEquals(Double rating, Integer cluster);

}
