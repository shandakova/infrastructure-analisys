package com.example.demo.services;

import com.example.demo.entities.HealthObjects;
import com.example.demo.repositories.HealthObjectsRepository;
import com.google.maps.model.LatLng;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class HealthServiceTest {
    @Autowired
    HealthService healthService;
    @Autowired
    HealthObjectsRepository healthObjectsRepository;

    @Before
    public void setUp() {
        healthObjectsRepository.deleteAll();
    }

    @Test
    @Description("getAverageRating in Health Objects with No objects returns 0.0")
    public void getAverageRatingAll_emptyTable() {
        getAverageByService(0.0);
    }

    @Test
    @Description("getAverageRating in Health Objects with objects returns normal value")
    public void getAverageRatingAll_tableWithObjects() {
        createTwoObjectsWithRating();
        getAverageByService(3.0);
    }

    @Step("Get average by service and compare with expected {expectedValue} ")
    private void getAverageByService(double expectedValue) {
        double averageRatingAll = healthService.getAverageRatingAll();
        assertEquals(expectedValue, averageRatingAll);
    }

    @Step("Create two objects with rating")
    private void createTwoObjectsWithRating() {
        HealthObjects ho1 = new HealthObjects();
        ho1.setId(1l);
        ho1.setRating(5.0);
        HealthObjects ho2 = new HealthObjects();
        ho2.setRating(1.0);
        ho2.setId(2l);
        healthObjectsRepository.save(ho1);
        healthObjectsRepository.save(ho2);
    }

    @Test
    @Description("getALl in Health Objects return empty list")
    public void getAll_emptyTable() {
        getListAndCompareSize(0);
    }

    @Test
    @Description("Create objects and compare")
    public void createObjectsAndCompare() {
        for (int i = 1; i < 5; i++) {
            createManyObjects(i);
            getListAndCompareSize(i);
            removeAllObjects();
        }
    }

    @Test
    @Description("getBordersByCluster with 0 Objects return empty list")
    public void getBordersByCluster_emptyTable() {
        getListByClusterAndCompareSize(0, 0);
    }

    @Test
    @Description("getBordersByCluster with Objects but without clusters return empty list")
    public void getBordersByCluster_nonEmptyTableWithNoCluster() {
        for (int i = 1; i < 5; i++) {
            createManyObjects(i);
            getListByClusterAndCompareSize(0, 0);
            removeAllObjects();
        }
    }

    @Test
    @Description("getBordersByCluster with Objects with 3 objects")
    public void getBordersByCluster_NotEmptyCluster() {
        for (int i = 1; i < 5; i++) {
            create3ObjectsWithCluster(i);
            getListByClusterAndCompareSize(3, i);
            removeAllObjects();
        }
    }

    @Step("Get list of objects and compare size with {expectedSize}")
    private void getListByClusterAndCompareSize(int expectedSize, int cluster) {
        List<LatLng> list = healthService.getBoardersByCluster(cluster);
        assertEquals(expectedSize, list.size());
    }

    @Step("Get list of objects and compare size with {expectedSize}")
    private void getListAndCompareSize(int expectedSize) {
        List<HealthObjects> list = healthService.getAll();
        assertEquals(expectedSize, list.size());
    }

    @Step("Remove all objects")
    private void removeAllObjects() {
        healthObjectsRepository.deleteAll();
    }

    @Step("Create and save {numberCreatedObject} objects")
    private void createManyObjects(int numberCreatedObject) {
        for (int i = 0; i < numberCreatedObject; i++) {
            HealthObjects ho = new HealthObjects();
            ho.setId((long) i);
            healthObjectsRepository.save(ho);
        }
    }

    @Step("Create and save 3 objects with CLuster")
    private void create3ObjectsWithCluster(int cluster) {
        HealthObjects ho = new HealthObjects();
        ho.setId((long) 0);
        ho.setCluster(cluster);
        ho.setCoordinateLng(0.0);
        ho.setCoordinateLat(0.0);
        healthObjectsRepository.save(ho);
        HealthObjects ho1 = new HealthObjects();
        ho1.setId((long) 1);
        ho1.setCluster(cluster);
        ho1.setCoordinateLng(0.0);
        ho1.setCoordinateLat(1.0);
        healthObjectsRepository.save(ho1);
        HealthObjects ho2 = new HealthObjects();
        ho2.setId((long) 2);
        ho2.setCluster(cluster);
        ho2.setCoordinateLng(1.0);
        ho2.setCoordinateLat(1.0);
        healthObjectsRepository.save(ho2);
    }

}