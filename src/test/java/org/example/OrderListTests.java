package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;


public class OrderListTests extends BaseTest {
    private HttpClient httpClient = new HttpClient();

    @Test
    @DisplayName("checkOrderList")
    public void checkOrderList() {
        httpClient.CallGet("/api/v1/orders")
                .then().assertThat().body("orders", hasSize(greaterThan(0)));
    }
}