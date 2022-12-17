package http;

import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        apiToken = register(url);
    }

    private String register(String url) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
            if (statusCode != 200) {
                throw new ManagerSaveException("Не удалось зарегистрировать клиент, код статуса: " + statusCode);
            }
            return httpResponse.body();
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
            throw new ManagerSaveException("Не удалось зарегистрировать клиент");
        }
    }

    public String load(String key) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
            if (statusCode != 200) {
                throw new ManagerSaveException("Не удалось получить менеджер, код статуса: " + statusCode);
            }
            return httpResponse.body();
        } catch (IOException | InterruptedException exception) {
            throw new ManagerSaveException("Не удалось получить менеджер");
        }
    }

    public void put(String key, String json) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<Void> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            int statusCode = httpResponse.statusCode();
            if (statusCode != 200) {
                throw new ManagerSaveException("Не удалось загрузить менеджер, код статуса: " + statusCode);
            }
        } catch (IOException | InterruptedException exception) {
            throw new ManagerSaveException("Не удалось загрузить менеджер");
        }
    }
}
