package main;

import io.qameta.allure.junit4.DisplayName;
import main.models.HttpClient;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class OrderListTests extends BaseTest {
    private HttpClient httpClient = new HttpClient();

    @Test
    @DisplayName("checkOrderList")
    public void checkOrderList() {
        httpClient.callGet("/api/v1/orders")
                .then().assertThat().body("orders", hasSize(greaterThan(0)));
    }
}
