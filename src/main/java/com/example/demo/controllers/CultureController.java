package com.example.demo.controllers;

import com.example.demo.entities.CultureObjects;
import com.example.demo.repositories.CultureObjectsRepository;
import com.example.demo.services.CultureService;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("culture")
public class CultureController {
    @Autowired
    private CultureObjectsRepository cultureObjectsRepository;
    @Autowired
    private CultureService cultureService;

    @GetMapping(path = "all/size", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    long getSize() {
        return cultureObjectsRepository.count();
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<CultureObjects> getAll() {
        return cultureService.getAll();
    }

    @GetMapping(path = "/all/rating", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Double getAverageRating() {
        return cultureService.getAverageRatingAll();
    }

    @GetMapping(path = "/cluster/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<CultureObjects> getByCluster(@PathVariable Integer id) {
        return cultureObjectsRepository.findByCluster(id);
    }

    @GetMapping(path = "/cluster/{id}/borders", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<LatLng> getBordersByCluster(@PathVariable Integer id) {
        return cultureService.getBoardersByCluster(id);
    }


}
