package org.example;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TelegramNotificationSender {

    private static String CHAT_ID = null;
    private static String TOKEN = null;

    public TelegramNotificationSender() {
        CHAT_ID = System.getProperty("chatId");
        TOKEN = System.getProperty("token");

        if (CHAT_ID == null || TOKEN == null) {
            throw new RuntimeException("chatId and/or token not set in system properties");
        }
    }

    public void sendMessage (String message) {

        String baseUrl = "https://api.telegram.org";
        String endpoint = "/{token}/sendMessage";
        RestAssured.baseURI = baseUrl;

        RequestSpecification httpRequest = RestAssured.given()
                .pathParam("token", "bot" + TOKEN)
                .queryParam("chat_id", CHAT_ID)
                .queryParam("text", message);

        httpRequest.request(Method.GET, endpoint);
    }

}
