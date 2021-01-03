package com.example.demo.services;

import com.example.demo.entities.CultureObjects;
import com.example.demo.repositories.CultureObjectsRepository;
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
public class CultureServiceTest {
    @Autowired
    CultureService cultureService;
    @Autowired
    CultureObjectsRepository cultureObjectsRepository;

    @Before
    public void setUp() {
        cultureObjectsRepository.deleteAll();
    }

    @Test
    @Description("getAverageRating in Culture Objects with No objects returns 0.0")
    public void getAverageRatingAll_emptyTable() {
        getAverageByService(0.0);
    }

    @Test
    @Description("getAverageRating in Culture Objects with objects returns normal value")
    public void getAverageRatingAll_tableWithObjects() {
        createTwoObjectsWithRating();
        getAverageByService(3.0);
    }

    @Step("Get average by service and compare with expected {expectedValue} ")
    private void getAverageByService(double expectedValue) {
        double averageRatingAll = cultureService.getAverageRatingAll();
        assertEquals(expectedValue, averageRatingAll);
    }

    @Step("Create two objects with rating")
    private void createTwoObjectsWithRating() {
        CultureObjects co1 = new CultureObjects();
        co1.setId(1l);
        co1.setRating(5.0);
        CultureObjects co2 = new CultureObjects();
        co2.setRating(1.0);
        co2.setId(2l);
        cultureObjectsRepository.save(co1);
        cultureObjectsRepository.save(co2);
    }

    @Test
    @Description("getALl in Culture Objects return empty list")
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
        List<LatLng> list = cultureService.getBoardersByCluster(cluster);
        assertEquals(expectedSize, list.size());
    }

    @Step("Get list of objects and compare size with {expectedSize}")
    private void getListAndCompareSize(int expectedSize) {
        List<CultureObjects> list = cultureService.getAll();
        assertEquals(expectedSize, list.size());
    }

    @Step("Remove all objects")
    private void removeAllObjects() {
        cultureObjectsRepository.deleteAll();
    }

    @Step("Create and save {numberCreatedObject} objects")
    private void createManyObjects(int numberCreatedObject) {
        for (int i = 0; i < numberCreatedObject; i++) {
            CultureObjects co = new CultureObjects();
            co.setId((long) i);
            cultureObjectsRepository.save(co);
        }
    }

    @Step("Create and save 3 objects with CLuster")
    private void create3ObjectsWithCluster(int cluster) {
        CultureObjects co = new CultureObjects();
        co.setId((long) 0);
        co.setCluster(cluster);
        co.setCoordinateLng(0.0);
        co.setCoordinateLat(0.0);
        cultureObjectsRepository.save(co);
        CultureObjects co1 = new CultureObjects();
        co1.setId((long) 1);
        co1.setCluster(cluster);
        co1.setCoordinateLng(0.0);
        co1.setCoordinateLat(1.0);
        cultureObjectsRepository.save(co1);
        CultureObjects co2 = new CultureObjects();
        co2.setId((long) 2);
        co2.setCluster(cluster);
        co2.setCoordinateLng(1.0);
        co2.setCoordinateLat(1.0);
        cultureObjectsRepository.save(co2);
    }

}