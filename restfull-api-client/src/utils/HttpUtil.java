package utils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtil {

    private static final int MAX_RETRY = 3;
    private static final long waitTime = 1000;
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String get(String url, Map<String, String> params, int requestTimeout) throws Exception {

        String query = params.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" +
                          URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String fullUrl = url;
        if(!query.isEmpty()) {
            fullUrl += "?" + query;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(requestTimeout))
                .GET()
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (HttpConnectTimeoutException e) {
            throw new Exception("Lỗi: Không thể kết nối tới server!", e);
        } catch (HttpTimeoutException e) {
            throw new Exception("Lỗi: Server xử lý quá lâu, đã timeout!", e);
        }
    }

    public static String post(String url, Map<String, String> params, int requestTimeout) throws Exception {

        String form = params.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" +
                          URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(requestTimeout))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("X-API-KEY", "your_secret_key") // Hoặc API Key
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        int retryCount = 0;
        while(retryCount > MAX_RETRY) {
            try {
                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());
                return response.body();
            } catch (Exception e) {
                retryCount++;
                if(retryCount >= MAX_RETRY) {
                    throw new Exception("Lỗi: Không thể kết nối tới server!", e);
                }

                Thread.sleep(waitTime);
            } 
        }
        return null;
    }
}
