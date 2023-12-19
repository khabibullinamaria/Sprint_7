package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CreateCourierTests extends BaseTest {
    private String login;
    private HttpClient httpClient = new HttpClient();

    @Before
    public void setUp() {
        login = UUID.randomUUID().toString();
        super.setUp();
    }

    @After
    public void tearDown() {
        Login loginObj = new Login(login , "1234");
        Response loginResponse = httpClient.CallPost(loginObj, "/api/v1/courier/login");
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
    @DisplayName("createCourier")
    public void createCourier() {
        Courier courierObj = new Courier(login , "1234", "Дядя Fedor");
            Response response = httpClient.CallPost(courierObj, "/api/v1/courier");
            response.then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("errorOnSameDataCourier")
    public void errorOnSameDataCourier() {
        Courier courierObj = new Courier(login , "1234", "Дядя Федор2");
        Response response = httpClient.CallPost(courierObj, "/api/v1/courier");
        Response secondResponse = httpClient.CallPost(courierObj, "/api/v1/courier");
        secondResponse.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    @DisplayName("checkRequiredFields")
    public void checkRequiredFields() {
        Courier courierObj1 = new Courier(login , null, "Дядя 666");
        Courier courierObj2 = new Courier(null, "1234", "Дядя 666");

        Response response1 = httpClient.CallPost(courierObj1, "/api/v1/courier");
        response1.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        Response response2 = httpClient.CallPost(courierObj2, "/api/v1/courier");
        response2.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("checkCorrectStatusCode")
    public void checkCorrectStatusCode() {
        Courier courierObj = new Courier(login , "1234", "Дядя Федор132");
        Response response = httpClient.CallPost(courierObj, "/api/v1/courier");
        response.then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("checkSuccessResponseText")
    public void checkSuccessResponseText() {
        Courier courierObj = new Courier(login , "1234", "Дядя Федор1335");
        Response response = httpClient.CallPost(courierObj, "/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("errorOnIfNotAllFields")
    public void errorOnIfNotAllFields() {
        Courier courierObj1 = new Courier(login , null, "Дядя 666");
        Courier courierObj2 = new Courier(null, "1234", "Дядя 666");

        Response response1 = httpClient.CallPost(courierObj1, "/api/v1/courier");
        response1.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        Response response2 = httpClient.CallPost(courierObj2, "/api/v1/courier");
        response2.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("errorOnSameLoginCourier")
    public void errorOnSameLoginCourier() {
        Courier courierObj = new Courier(login , "1234", "Дядя Федор131213");
        Response response = httpClient.CallPost(courierObj, "/api/v1/courier");
        Response secondResponse = httpClient.CallPost(courierObj, "/api/v1/courier");
        secondResponse.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }
}