package org.example;

import io.restassured.RestAssured;
import org.junit.Before;

import java.util.UUID;

public class BaseTest {
    @Before
    public void setUp() {

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
