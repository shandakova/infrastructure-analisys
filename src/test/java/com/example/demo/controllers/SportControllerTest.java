package com.example.demo.controllers;

import com.example.demo.entities.SportObjects;
import com.example.demo.repositories.SportObjectsRepository;
import com.example.demo.services.SportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.maps.model.LatLng;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class SportControllerTest {
    private MockMvc mvc;
    @InjectMocks
    SportController sportController = new SportController();
    @Mock
    SportObjectsRepository sportObjectsRepository;
    @Mock
    SportService sportService;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(sportController)
                .build();
    }

    @Test
    @Description("getSize return 0 when table have 0 objects")
    public void getSize_return0Size() throws Exception {
        when(sportObjectsRepository.count()).thenReturn(0L);
        MockHttpServletResponse response = getMockHttpServletResponse("/sport/all/size");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEqualSize(response, 0L);
    }

    @Test
    @Description("getSize return normalSize when table have 0 objects")
    public void getSize_returnNon0Size() throws Exception {
        for (long i = 1; i < 5; i++) {
            when(sportObjectsRepository.count()).thenReturn(i);
            MockHttpServletResponse response = getMockHttpServletResponse("/sport/all/size");
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertEqualSize(response, i);
        }
    }

    @Step("Assert that size equals {expectedSize}")
    private void assertEqualSize(MockHttpServletResponse response, long expectedSize) throws UnsupportedEncodingException {
        assertEquals(expectedSize, Long.parseLong(response.getContentAsString()));
    }

    @Step("Send request to {url}")
    private MockHttpServletResponse getMockHttpServletResponse(String url) throws Exception {
        return mvc.perform(
                get(url).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
    }

    @Test
    @Description("getAll return list with objects")
    public void getAll_returnFullList() throws Exception {
        for (long i = 1; i < 5; i++) {
            List<SportObjects> list = new ArrayList<>();
            createListWithObjects(i, list);
            when(sportService.getAll()).thenReturn(list);
            MockHttpServletResponse response = getMockHttpServletResponse("/sport/all");
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertThatListHaveRightSize(i, response);
        }
    }

    @Step("Assert that list have {size} objects.")
    private void assertThatListHaveRightSize(long size, MockHttpServletResponse response) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        SportObjects[] newList = gson.fromJson(response.getContentAsString(), SportObjects[].class);
        assertEquals(size, newList.length);
    }

    @Step("Create list with {i} objects.")
    private void createListWithObjects(long i, List<SportObjects> list) {
        for (long j = 0; j < i; j++) {
            SportObjects object = new SportObjects();
            object.setId(j);
            list.add(object);
        }
    }

    @Test
    @Description("getAll return list with objects")
    public void getAverage_returnAverage() throws Exception {
        for (long i = 1; i < 5; i++) {
            List<SportObjects> list = new ArrayList<>();
            createListWithRatingObjects(i, list);
            Double rating = list.stream().filter(x -> x.getRating() != null && x.getRating() > 0).flatMapToDouble(x -> DoubleStream.of(x.getRating())).average().orElse(0.0);
            when(sportService.getAverageRatingAll()).thenReturn(rating);
            MockHttpServletResponse response = getMockHttpServletResponse("/sport/all/rating");
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            compareRating(rating, response);
        }
    }

    @Step("Assert rating calculation.")
    private void compareRating(double rating, MockHttpServletResponse response) throws UnsupportedEncodingException {
        assertEquals(rating, Double.parseDouble(response.getContentAsString()));
    }

    @Step("Create list with {i} rating objects.")
    private void createListWithRatingObjects(long i, List<SportObjects> list) {
        for (long j = 0; j < i; j++) {
            SportObjects object = new SportObjects();
            object.setId(j);
            object.setRating((double) j);
            list.add(object);
        }
    }

    @Test
    @Description("getByCluster return list with objects")
    public void getByCluster_returnList() throws Exception {
        for (long i = 1; i < 5; i++) {
            List<SportObjects> list = new ArrayList<>();
            createListWithClusterObjects(i, list);
            when(sportObjectsRepository.findByCluster(1)).thenReturn(list);
            MockHttpServletResponse response = getMockHttpServletResponse("/sport/cluster/1");
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertThatListHaveRightSize(i, response);
        }
    }

    @Step("Create list with {i} rating objects.")
    private void createListWithClusterObjects(long i, List<SportObjects> list) {
        for (long j = 0; j < i; j++) {
            SportObjects object = new SportObjects();
            object.setId(j);
            object.setCluster(1);
            list.add(object);
        }
    }

    @Test
    @Description("getByCluster return list with objects")
    public void getBordersByCLuster_returnListLatLng() throws Exception {
        for (long i = 1; i < 5; i++) {
            List<LatLng> list = new ArrayList<>();
            createListWithCoordinates(list);
            when(sportService.getBoardersByCluster(1)).thenReturn(list);
            MockHttpServletResponse response = getMockHttpServletResponse("/sport/cluster/1/borders");
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertThatLatLngListHaveRightSize(3, response);
        }
    }

    @Step("Assert that list LatLng have {size} objects.")
    private void assertThatLatLngListHaveRightSize(long size, MockHttpServletResponse response) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        LatLng[] newList = gson.fromJson(response.getContentAsString(), LatLng[].class);
        assertEquals(size, newList.length);
    }

    @Step("Create LatLng list with 3 objects that have coordinates.")
    private void createListWithCoordinates(List<LatLng> list) {
        list.add(new LatLng(0.0, 0.0));
        list.add(new LatLng(0.0, 1.0));
        list.add(new LatLng(1.0, 1.0));
    }
}