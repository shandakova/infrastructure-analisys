package com.example.demo.services;

import com.example.demo.entities.SportObjects;
import com.example.demo.repositories.SportObjectsRepository;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

import static java.lang.Math.*;

@Service
public class SportService {
    @Autowired
    private SportObjectsRepository sportObjectsRepository;

    public double getAverageRatingAll() {
        List<SportObjects> objects = sportObjectsRepository.findByRatingGreaterThan(0.0);
        OptionalDouble rating = objects.stream().filter(x -> x.getRating() != null && x.getRating() > 0).flatMapToDouble(x -> DoubleStream.of(x.getRating())).average();
        return rating.orElse(0.0);
    }

    //нахождение выпуклой оболочки методом Джарвиса
    public List<LatLng> getBoardersByCluster(Integer cluster) {
        List<LatLng> latLngs = new ArrayList<>();
        List<SportObjects> objects = new ArrayList<>(sportObjectsRepository.findByCluster(cluster));
        List<SportObjects> delete = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (i + 1 < objects.size()) {
                if (objects.get(i).getCoordinateLat().equals(objects.get(i + 1).getCoordinateLat()) && objects.get(i).getCoordinateLng().equals(objects.get(i + 1).getCoordinateLng())) {
                    delete.add(objects.get(i + 1));
                }
            }
        }
        objects.removeAll(delete);
        delete.clear();
        if (objects.size() <= 3) {
            objects.forEach(
                    x -> {
                        latLngs.add(new LatLng(x.getCoordinateLat(), x.getCoordinateLng()));
                    }
            );
        } else {
            SportObjects first = objects.get(0);
            //самая левая из нижних точек
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i).getCoordinateLat() < first.getCoordinateLat() ||
                        objects.get(i).getCoordinateLat().equals(first.getCoordinateLat()) &&
                                objects.get(i).getCoordinateLng() < first.getCoordinateLng()) {
                    first = objects.get(i);
                }
            }
            latLngs.add(new LatLng(first.getCoordinateLat(), first.getCoordinateLng()));
            SportObjects curr = first;
            SportObjects prev = new SportObjects();
            prev.setCoordinateLat(first.getCoordinateLat());
            prev.setCoordinateLng(first.getCoordinateLng() - 1);
            do {
                double minCosAngle = Double.MAX_VALUE;
                double maxLen = Double.MAX_VALUE;
                int next = -1;
                for (int i = 0; i < objects.size(); i++) {
                    double curCosAngle = сosAngle(prev, curr, objects.get(i));
                    if (curCosAngle < minCosAngle) {
                        next = i;
                        minCosAngle = curCosAngle;
                        maxLen = dist(curr, objects.get(i));
                    } else if (curCosAngle == minCosAngle) {
                        double curLen = dist(curr, objects.get(i));
                        if (curLen > maxLen) {
                            next = i;
                            maxLen = curLen;
                        }
                    }
                }
                prev = curr;
                curr = objects.get(next);
                latLngs.add(new LatLng(objects.get(next).getCoordinateLat(), objects.get(next).getCoordinateLng()));
                objects.remove(next);
            } while (curr != first);
        }
        return latLngs;
    }

    public List<SportObjects> getAll() {
        List<SportObjects> list = new ArrayList<>();
        for (SportObjects x : sportObjectsRepository.findAll()) {
            list.add(x);
        }
        return list;
    }

    private double dist(SportObjects curr, SportObjects healthObjects) {
        double latDiff = curr.getCoordinateLat() - healthObjects.getCoordinateLat();
        double lngDiff = curr.getCoordinateLng() - healthObjects.getCoordinateLng();
        return sqrt(latDiff * latDiff + lngDiff * lngDiff);
    }

    private double сosAngle(SportObjects prev, SportObjects curr, SportObjects object) {
        double result = atan2(curr.getCoordinateLat() - prev.getCoordinateLat(), curr.getCoordinateLng() - prev.getCoordinateLng()) -
                atan2(curr.getCoordinateLat() - object.getCoordinateLat(), curr.getCoordinateLng() - object.getCoordinateLng());
        return cos(result);
    }
}
