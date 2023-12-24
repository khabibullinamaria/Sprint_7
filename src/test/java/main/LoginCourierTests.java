package main;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import main.models.Courier;
import main.models.HttpClient;
import main.models.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginCourierTests extends BaseTest {
    private String login;
    private String password;
    private HttpClient httpClient = new HttpClient();
    @Before
    public void setUp() {
        login = UUID.randomUUID().toString();
        password = UUID.randomUUID().toString();
        String firstName = UUID.randomUUID().toString();
        Courier courierObj = new Courier(login , password, firstName);
        httpClient.callPost(courierObj, "/api/v1/courier");
        super.setUp();
    }
    @After
    public void tearDown() {
        Login loginObj = new Login(login, password);
        Response loginResponse = httpClient.callPost(loginObj, "/api/v1/courier/login");
        if(loginResponse.body().toString().contains("id")){
            int id = loginResponse.body().jsonPath().get("id");
            Response deleteResponse =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .when()
                            .delete("/api/v1/courier/"+id);
        }
    }

    @Test
    @DisplayName("loginCourier")
    public void loginCourier() {
        Login loginObj = new Login(login, password);
            Response response = httpClient.callPost(loginObj, "/api/v1/courier/login");
            response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("authorizedCourier")
    public void authorizedCourier() {
        Login loginWithoutPass = new Login("uncleF11", "");
        Login loginWithoutLogin = new Login("", "1234");
        Response responseWithoutPass = httpClient.callPost(loginWithoutPass, "/api/v1/courier/login");
        responseWithoutPass.then().assertThat().statusCode(400);

        Response responseWithoutLogin = httpClient.callPost(loginWithoutLogin, "/api/v1/courier/login");
        responseWithoutLogin.then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("checkCorrectData")
    public void checkCorrectData() {
        Login firstLogin = new Login("ninja" , "1234");
        Login secondLogin = new Login("uncleF11" , "123455");
        Response firstResponse = httpClient.callPost(firstLogin, "/api/v1/courier/login");
        firstResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        Response secondResponse = httpClient.callPost(secondLogin, "/api/v1/courier/login");
        secondResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("errorOnEmptyField")
    public void errorOnEmptyField() {
        Login loginWithoutPass = new Login("uncleF11" , "");
        Login loginWithoutLogin = new Login("" , "1234");
        Response responseWithoutPass = httpClient.callPost(loginWithoutPass, "/api/v1/courier/login");
        responseWithoutPass.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));

        Response responseWithoutLogin = httpClient.callPost(loginWithoutLogin, "/api/v1/courier/login");
        responseWithoutLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("errorOnWrongLogin")
    public void errorOnWrongLogin() {
        Login loginObj = new Login("0000" , "1234");
        Response response = httpClient.callPost(loginObj, "/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("successResultWithIdTest")
    public void successResultWithIdTest() {
        Login loginObj = new Login(login, password);
        Response response = httpClient.callPost(loginObj, "/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue());
    }
}
