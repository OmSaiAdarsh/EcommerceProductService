package org.example.ecommerceapp.configs;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        // Consider all 4xx and 5xx as errors EXCEPT UNAUTHORIZED
        return response.getStatusCode().is4xxClientError()
               && !response.getStatusCode().equals(HttpStatus.UNAUTHORIZED) ||
               response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // Optionally handle other error codes if needed
        if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            // Log or perform specific actions for unauthorized requests if needed
            System.out.println("Received 401 Unauthorized, handling in RestTemplate call.");
        } else {
            // Optionally throw the default exception for other errors
            // throw new HttpClientErrorException(response.getStatusCode(), response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
        }
    }

    private byte[] getResponseBody(ClientHttpResponse response) throws IOException {
        // Helper method to read the response body (implementation omitted for brevity)
        return new byte[0]; // Replace with actual implementation if needed
    }

    private java.nio.charset.Charset getCharset(ClientHttpResponse response) {
        // Helper method to get the charset (implementation omitted for brevity)
        return null; // Replace with actual implementation if needed
    }
}