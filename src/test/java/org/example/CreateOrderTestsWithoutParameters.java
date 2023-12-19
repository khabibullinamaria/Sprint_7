package org.example;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTestsWithoutParameters extends BaseTest {
    private HttpClient httpClient = new HttpClient();

    @Test
    @DisplayName("checkTrack")
    public void checkTrack() {
        Order orderObj = new Order("Naruto", "Uchiha","Konoha, 142 apt.", "4", "+7 800 355 35 35", "5", "2020-06-06", "Saske, come back to Konoha", new String[]{});
        Response response = httpClient.CallPost(orderObj, "/api/v1/orders");
        response.then().assertThat().body("track", notNullValue());
    }
}
