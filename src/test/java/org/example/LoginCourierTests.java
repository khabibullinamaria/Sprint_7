package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginCourierTests extends BaseTest {
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
    @DisplayName("loginCourier")
    public void loginCourier() {
        Login loginObj = new Login("uncleF11" , "1234");
            Response response = httpClient.CallPost(loginObj, "/api/v1/courier/login");
            response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("authorizedCourier")
    public void authorizedCourier() {
        Login loginObj1 = new Login("uncleF11" , "");
        Login loginObj2 = new Login("" , "1234");
        Response response1 = httpClient.CallPost(loginObj1, "/api/v1/courier/login");
        response1.then().assertThat().statusCode(400);

        Response response2 = httpClient.CallPost(loginObj2, "/api/v1/courier/login");
        response2.then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("checkCorrectData")
    public void checkCorrectData() {
        Login loginObj1 = new Login("ninja" , "1234");
        Login loginObj2 = new Login("uncleF11" , "123455");
        Response response1 = httpClient.CallPost(loginObj1, "/api/v1/courier/login");
        response1.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        Response response2 = httpClient.CallPost(loginObj2, "/api/v1/courier/login");
        response2.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("errorOnEmptyField")
    public void errorOnEmptyField() {
        Login loginObj1 = new Login("uncleF11" , "");
        Login loginObj2 = new Login("" , "1234");
        Response response1 = httpClient.CallPost(loginObj1, "/api/v1/courier/login");
        response1.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));

        Response response2 = httpClient.CallPost(loginObj2, "/api/v1/courier/login");
        response2.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("errorOnWrongLogin")
    public void errorOnWrongLogin() {
        Login loginObj = new Login("0000" , "1234");
        Response response = httpClient.CallPost(loginObj, "/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("successResultWithIdTest")
    public void successResultWithIdTest() {
        Login loginObj = new Login("uncleF11" , "1234");
        Response response = httpClient.CallPost(loginObj, "/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue());
    }
}