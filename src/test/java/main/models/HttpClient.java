package main.models;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class HttpClient {
    public Response callPost(Object json, String url) {
        Response response =
            given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(url);
        return response;
    }

    public Response callGet(String url) {
        Response response =
            given()
                .get(url);
        return response;
    }

    public Response callDelete(String url) {
        Response response =
            given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete(url);

        return response;
    }
}
