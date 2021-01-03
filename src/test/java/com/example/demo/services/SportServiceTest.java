package com.example.demo.services;

import com.example.demo.entities.SportObjects;
import com.example.demo.repositories.SportObjectsRepository;
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
public class SportServiceTest {
    @Autowired
    SportService sportService;
    @Autowired
    SportObjectsRepository sportObjectsRepository;

    @Before
    public void setUp() {
        sportObjectsRepository.deleteAll();
    }

    @Test
    @Description("getAverageRating in Sport Objects with No objects returns 0.0")
    public void getAverageRatingAll_emptyTable() {
        getAverageByService(0.0);
    }

    @Test
    @Description("getAverageRating in Sport Objects with objects returns normal value")
    public void getAverageRatingAll_tableWithObjects() {
        createTwoObjectsWithRating();
        getAverageByService(3.0);
    }

    @Step("Get average by service and compare with expected {expectedValue} ")
    private void getAverageByService(double expectedValue) {
        double averageRatingAll = sportService.getAverageRatingAll();
        assertEquals(expectedValue, averageRatingAll);
    }

    @Step("Create two objects with rating")
    private void createTwoObjectsWithRating() {
        SportObjects so1 = new SportObjects();
        so1.setId(1l);
        so1.setRating(5.0);
        SportObjects so2 = new SportObjects();
        so2.setRating(1.0);
        so2.setId(2l);
        sportObjectsRepository.save(so1);
        sportObjectsRepository.save(so2);
    }

    @Test
    @Description("getALl in Sport Objects return empty list")
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
        List<LatLng> list = sportService.getBoardersByCluster(cluster);
        assertEquals(expectedSize, list.size());
    }

    @Step("Get list of objects and compare size with {expectedSize}")
    private void getListAndCompareSize(int expectedSize) {
        List<SportObjects> list = sportService.getAll();
        assertEquals(expectedSize, list.size());
    }

    @Step("Remove all objects")
    private void removeAllObjects() {
        sportObjectsRepository.deleteAll();
    }

    @Step("Create and save {numberCreatedObject} objects")
    private void createManyObjects(int numberCreatedObject) {
        for (int i = 0; i < numberCreatedObject; i++) {
            SportObjects so = new SportObjects();
            so.setId((long) i);
            sportObjectsRepository.save(so);
        }
    }

    @Step("Create and save 3 objects with CLuster")
    private void create3ObjectsWithCluster(int cluster) {
        SportObjects so = new SportObjects();
        so.setId((long) 0);
        so.setCluster(cluster);
        so.setCoordinateLng(0.0);
        so.setCoordinateLat(0.0);
        sportObjectsRepository.save(so);
        SportObjects so1 = new SportObjects();
        so1.setId((long) 1);
        so1.setCluster(cluster);
        so1.setCoordinateLng(0.0);
        so1.setCoordinateLat(1.0);
        sportObjectsRepository.save(so1);
        SportObjects so2 = new SportObjects();
        so2.setId((long) 2);
        so2.setCluster(cluster);
        so2.setCoordinateLng(1.0);
        so2.setCoordinateLat(1.0);
        sportObjectsRepository.save(so2);
    }

}