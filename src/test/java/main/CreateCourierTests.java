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
        Response loginResponse = httpClient.callPost(loginObj, "/api/v1/courier/login");
        if(loginResponse.body().toString().contains("id")){
            int id = loginResponse.body().jsonPath().get("id");
            Response deleteResponse = httpClient.callDelete("/api/v1/courier/"+id);
        }
    }

    @Test
    @DisplayName("successCreateCourier")
    public void successCreateCourier() {
        Courier courierObj = new Courier(login , "1234", "Дядя Fedor");
        Response response = httpClient.callPost(courierObj, "/api/v1/courier");
        response.then().assertThat().statusCode(201);
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("errorOnSameDataCourier")
    public void errorOnSameDataCourier() {
        Courier courierObj = new Courier(login , "1234", "Дядя Федор2");
        Response response = httpClient.callPost(courierObj, "/api/v1/courier");
        Response secondResponse = httpClient.callPost(courierObj, "/api/v1/courier");
        secondResponse.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    @DisplayName("errorOnIfNotAllFields")
    public void errorOnIfNotAllFields() {
        Courier nullLoginCourier = new Courier(null, "1234", "Дядя 666");

        Response nullLoginResponse = httpClient.callPost(nullLoginCourier, "/api/v1/courier");
        nullLoginResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("errorOnIfNotAllFields")
    public void errorOnIfNotAllFields() {
        Courier nullPassCourier = new Courier(login , null, "Дядя 666");

        Response nullPassResponse = httpClient.callPost(nullPassCourier, "/api/v1/courier");
        nullPassResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
            .and()
            .statusCode(400);
    }

    @Test
    @DisplayName("errorOnSameLoginCourier")
    public void errorOnSameLoginCourier() {
        Courier courierObj = new Courier(login , "1234", "Дядя Федор131213");
        Response response = httpClient.callPost(courierObj, "/api/v1/courier");
        Response secondResponse = httpClient.callPost(courierObj, "/api/v1/courier");
        secondResponse.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }
}
