package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CreateCourierTests {
    private String login;

    @Before
    public void setUp() {
        login = UUID.randomUUID().toString();

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void tearDown() {
        var loginJson = "{\"login\": \"" + login + "\", \"password\": \"1234\"}";
        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginJson)
                        .when()
                        .post("/api/v1/courier/login");
        if(loginResponse.body().toString().contains("id")){
            int id = loginResponse.body().jsonPath().get("id");
            Response deleteResponse =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .when()
                            .delete("/api/v1/courier/"+id);
            deleteResponse.then().assertThat().statusCode(200);
        }
    }

    @Test
    public void createCourier() {
            var json = "{\"login\": \"" + login + "\", \"password\": \"1234\",\"firstName\": \"Дядя Fedor\"}";
            Response response =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body(json)
                            .when()
                            .post("/api/v1/courier");
            response.then().assertThat().statusCode(201);
    }

    @Test
    public void errorOnSameDataCourier() {
        String json = "{\"login\": \"" + login + "\", \"password\": \"1234\",\"firstName\": \"Дядя Федор2\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        Response secondResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        secondResponse.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    public void checkRequiredFields() {
        String json1 = "{\"login\": \"" + login + "\",\"firstName\": \"Дядя 666\"}";
        String json2 = "{\"password\": \"1234\",\"firstName\": \"Дядя 666\"}";

        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json1)
                        .when()
                        .post("/api/v1/courier");
        response1.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        Response response2 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json2)
                        .when()
                        .post("/api/v1/courier");
        response2.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    public void checkCorrectStatusCode() {
        String json = "{\"login\": \"" + login + "\", \"password\": \"1234\",\"firstName\": \"Дядя Федор132\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(201);
    }

    @Test
    public void checkSuccessResponseText() {
        String json = "{\"login\": \"" + login + "\", \"password\": \"1234\",\"firstName\": \"Дядя Федор1335\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Test
    public void errorOnIfNotAllFields() {
        String json1 = "{\"login\": \"" + login + "\",\"firstName\": \"Дядя 666\"}";
        String json2 = "{\"password\": \"1234\",\"firstName\": \"Дядя 666\"}";

        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json1)
                        .when()
                        .post("/api/v1/courier");
        response1.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        Response response2 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json2)
                        .when()
                        .post("/api/v1/courier");
        response2.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    public void errorOnSameLoginCourier() {
        String json = "{\"login\": \"" + login + "\", \"password\": \"1234\",\"firstName\": \"Дядя Федор131213\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        Response secondResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        secondResponse.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }
}