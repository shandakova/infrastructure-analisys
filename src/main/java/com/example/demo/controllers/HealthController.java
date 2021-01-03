package com.example.demo.controllers;

import com.example.demo.entities.HealthObjects;
import com.example.demo.repositories.HealthObjectsRepository;
import com.example.demo.services.HealthService;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("health")
public class HealthController {
    @Autowired
    private HealthObjectsRepository healthObjectsRepository;
    @Autowired
    private HealthService healthService;

    @GetMapping(path = "all/size", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    long getSize() {
        return healthObjectsRepository.count();
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<HealthObjects> getAll() {
        List<HealthObjects> list = new ArrayList<>();
        list = healthService.getAll();
        return list;
    }

    @GetMapping(path = "/all/rating", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Double getAverageRating() {
        return healthService.getAverageRatingAll();
    }

    @GetMapping(path = "/cluster/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<HealthObjects> getByCluster(@PathVariable Integer id) {
        return healthObjectsRepository.findByCluster(id);
    }

    @GetMapping(path = "/cluster/{id}/borders", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<LatLng> getBordersByCluster(@PathVariable Integer id) {
        return healthService.getBoardersByCluster(id);
    }


}
