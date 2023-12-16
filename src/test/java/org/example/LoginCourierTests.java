package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginCourierTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void loginCourier() {
            String json = "{\"login\": \"uncleF11\", \"password\": \"1234\"}";
            Response response =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body(json)
                            .when()
                            .post("/api/v1/courier/login");
            response.then().assertThat().statusCode(200);
    }

    @Test
    public void authorizedCourier() {
        String json1 = "{\"login\": \"uncleF11\", \"password\": \"\"}";
        String json2 = "{\"login\": \"\", \"password\": \"1234\"}";
        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json1)
                        .when()
                        .post("/api/v1/courier/login");
        response1.then().assertThat().statusCode(400);

        Response response2 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json2)
                        .when()
                        .post("/api/v1/courier/login");
        response2.then().assertThat().statusCode(400);
    }

    @Test
    public void checkCorrectData() {
        String json1 = "{\"login\": \"ninja\", \"password\": \"1234\"}";
        String json2 = "{\"login\": \"uncleF11\", \"password\": \"123455\"}";
        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json1)
                        .when()
                        .post("/api/v1/courier/login");
        response1.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        Response response2 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json2)
                        .when()
                        .post("/api/v1/courier/login");
        response2.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    public void errorOnEmptyField() {
        String json1 = "{\"login\": \"uncleF11\", \"password\": \"\"}";
        String json2 = "{\"login\": \"\", \"password\": \"1234\"}";
        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json1)
                        .when()
                        .post("/api/v1/courier/login");
        response1.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));

        Response response2 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json2)
                        .when()
                        .post("/api/v1/courier/login");
        response2.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    public void errorOnWrongLogin() {
        String json = "{\"login\": \"0000\", \"password\": \"1234\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    public void successResultWithIdTest() {
        String json = "{\"login\": \"uncleF11\", \"password\": \"1234\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue());
    }
}