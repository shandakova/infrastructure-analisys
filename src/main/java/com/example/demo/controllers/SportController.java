package com.example.demo.controllers;

import com.example.demo.entities.SportObjects;
import com.example.demo.repositories.SportObjectsRepository;
import com.example.demo.services.SportService;
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
@RequestMapping("sport")
public class SportController {
    @Autowired
    private SportObjectsRepository sportObjectsRepository;
    @Autowired
    private SportService sportService;

    @GetMapping(path = "all/size", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    long getSize() {
        return sportObjectsRepository.count();
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<SportObjects> getAll() {
        return sportService.getAll();
    }

    @GetMapping(path = "/all/rating", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Double getAverageRating() {
        return sportService.getAverageRatingAll();
    }

    @GetMapping(path = "/cluster/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<SportObjects> getByCluster(@PathVariable Integer id) {
        return sportObjectsRepository.findByCluster(id);
    }

    @GetMapping(path = "/cluster/{id}/borders", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<LatLng> getBordersByCluster(@PathVariable Integer id) {
        return sportService.getBoardersByCluster(id);
    }


}
