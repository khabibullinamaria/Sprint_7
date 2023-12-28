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
        if(loginResponse.body().toString().contains("id")) {
            int id = loginResponse.body().jsonPath().get("id");
            httpClient.callDelete("/api/v1/courier/" + id);
        }
    }

    @Test
    @DisplayName("successLoginCourier")
    public void successLoginCourier() {
        Login loginObj = new Login(login, password);
        Response response = httpClient.callPost(loginObj, "/api/v1/courier/login");
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("loginWithoutPass")
    public void loginWithoutPass() {
        Login loginWithoutPass = new Login("uncleF11", "");
        Response responseWithoutPass = httpClient.callPost(loginWithoutPass, "/api/v1/courier/login");
        responseWithoutPass.then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("loginWithoutLogin")
    public void loginWithoutLogin() {
        Login loginWithoutLogin = new Login("", "1234");

        Response responseWithoutLogin = httpClient.callPost(loginWithoutLogin, "/api/v1/courier/login");
        responseWithoutLogin.then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("checkCorrectData")
    public void notCreatedAccount() {
        Login login = new Login("ninja" , "1234");
        Response response = httpClient.callPost(login, "/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
            .and()
            .statusCode(404);
    }

    @Test
    @DisplayName("errorOnEmptyPassField")
    public void errorOnEmptyPassField() {
        Login loginWithoutPass = new Login("uncleF11" , "");

        Response responseWithoutPass = httpClient.callPost(loginWithoutPass, "/api/v1/courier/login");
        responseWithoutPass.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("errorOnEmptyLoginField")
    public void errorOnEmptyLoginField() {
        Login loginWithoutLogin = new Login("" , "1234");

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
}
