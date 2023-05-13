package org.example;


import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

public class AppTest {

    boolean isFrontEndUp = false;
    boolean isRESTUp = false;
    boolean isSOAPUp = false;

    private static final String OK = "\u2705";
    private static final String NOT_OK = "\u274C";
    private static final String BASE_URL =  System.getProperty("baseUrl");

    private final TelegramNotificationSender notificationSender = new TelegramNotificationSender();

    @Test
    public void shouldAnswerWithTrue() {

        Response responseRESTAPI = given()
                .log()
                .all()
                .get(BASE_URL + "8080/test-orders/5")
                .then()
                .log()
                .all()
                .extract()
                .response();

        if ( responseRESTAPI.getStatusCode() == HttpStatus.SC_OK  ) {
            isRESTUp = true;
        }

        String messageREST = isRESTUp ? OK + " REST" : NOT_OK +  " REST";
        sendNotification( messageREST);
    }

    @Test
    public void soap() {

        String responseSOAPAPI = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "                 xmlns:gs=\"http://spring.io/guides/gs-producing-web-service\">" +
                "" +
                "<soapenv:Header/>\n" +
                "  <soapenv:Body>\n" +
                "     <gs:getCountryRequest>\n" +
                "        <gs:name>Spain</gs:name>\n" +
                "     </gs:getCountryRequest>\n" +
                "  </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        try {

            Response responseSOAP = given()
                    .contentType("text/xml")
                    .body(responseSOAPAPI)
                    .log()
                    .all()
                    .when()
                    .post( BASE_URL+ "8090/ws")
                    .then()
                    .log()
                    .all()
                    .extract()
                    .response();

            if ( responseSOAP.getStatusCode() == HttpStatus.SC_OK ) {
                isSOAPUp = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String messageSOAP = isSOAPUp ? OK + " SOAP" : NOT_OK +  " SOAP";
        sendNotification( messageSOAP);
    }

    @Test
    public void frontEnd() {
        try {

            Response responseFrontEnd = given()
                    .log()
                    .all()
                    .get(BASE_URL + "3000")
                    .then()
                    .log()
                    .all()
                    .extract()
                    .response();

            if (( responseFrontEnd.getBody() != null ) & ( responseFrontEnd.getStatusCode() == HttpStatus.SC_OK ) ) {
                isFrontEndUp = true;
            };

        } catch ( Exception e){
            e.printStackTrace();
        }

        String messageFront = isFrontEndUp ? OK + " Front" : NOT_OK +  " Front";
        sendNotification( messageFront);
    }

    private void sendNotification(String status) {
        notificationSender.sendMessage( status );
    }
}
