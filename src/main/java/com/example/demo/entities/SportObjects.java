package com.example.demo.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SPORT_OBJECTS")
@Data
public class SportObjects {
    @Id
    private Long id;
    private String type;
    private Double area;
    private String name;
    private String address;
    @Column(name = "coordinate_lat")
    private Double coordinateLat;
    @Column(name = "coordinate_lng")
    private Double coordinateLng;
    private String district;
    private Double rating;
    @Column(name = "place_id")
    private String placeId;
    private String reference;
    private Integer cluster;
}
