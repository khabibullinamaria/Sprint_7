package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTests {
    private String color;
        public CreateOrderTests(String color) {

            this.color = color;
        }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters
    public static Object[][] orderData() {
        //Сгенерируй тестовые данные (свою учётку и несколько случайных)
        return new Object[][] {
                {"\"BLACK\""},
                {"\"GREY\""},
                {"\"BLACK\", \"GREY\""},
                {""},
        };
    }

    @Test
    public void checkColor() {
            String json = "{\"firstName\": \"Naruto\", \"lastName\": \"Uchiha\", \"address\": \"Konoha, 142 apt.\", \"metroStation\": 4, \"phone\": \"+7 800 355 35 35\", \"rentTime\": 5, \"deliveryDate\": \"2020-06-06\", \"comment\": \"Saske, come back to Konoha\", \"color\": [" +
                    color +
                    "]}";
            Response response =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body(json)
                            .when()
                            .post("/api/v1/orders");
            response.then().assertThat().statusCode(201);
    }

    @Test
    public void checkTrack() {
        String json = "{\"firstName\": \"Naruto\", \"lastName\": \"Uchiha\", \"address\": \"Konoha, 142 apt.\", \"metroStation\": 4, \"phone\": \"+7 800 355 35 35\", \"rentTime\": 5, \"deliveryDate\": \"2020-06-06\", \"comment\": \"Saske, come back to Konoha\", \"color\": []}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue());
    }

}