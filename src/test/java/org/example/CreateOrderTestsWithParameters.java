package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTestsWithParameters extends BaseTest {
    private String [] colors;
    private HttpClient httpClient = new HttpClient();
    public CreateOrderTestsWithParameters(String [] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] orderData() {
        return new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}},
        };
    }

    @Test
    @DisplayName("checkColor")
    public void checkColor() {
        Order orderObj = new Order("Naruto", "Uchiha","Konoha, 142 apt.", "4", "+7 800 355 35 35", "5", "2020-06-06", "Saske, come back to Konoha", colors);
            Response response = httpClient.CallPost(orderObj, "/api/v1/orders");
            response.then().assertThat().statusCode(201);
    }
}
