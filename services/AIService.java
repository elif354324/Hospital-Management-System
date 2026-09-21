package services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AIService {

    private static final String OLLAMA_URL =
            "http://localhost:11434/api/chat";

    private static final String MODEL =
            "llama3.2:3b";

    private final HttpClient httpClient;

    public AIService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public String ask(String prompt) throws Exception {

        String jsonBody = """
                {
                  "model": "%s",
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ],
                  "stream": false
                }
                """.formatted(
                        MODEL,
                        escapeJson(prompt)
                );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Ollama API hatası: "
                    + response.statusCode()
                    + "\n"
                    + response.body()
            );
        }

        return extractContent(response.body());
    }

    private String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String extractContent(String json) {

        String marker = "\"content\":\"";

        int start = json.indexOf(marker);

        if (start == -1) {
            return "AI cevabı alınamadı.\n\n" + json;
        }

        start += marker.length();

        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = start; i < json.length(); i++) {

            char c = json.charAt(i);

            if (escaped) {

                switch (c) {
                    case 'n' -> result.append('\n');
                    case 'r' -> result.append('\r');
                    case 't' -> result.append('\t');
                    case '"' -> result.append('"');
                    case '\\' -> result.append('\\');
                    default -> result.append(c);
                }

                escaped = false;

            } else if (c == '\\') {

                escaped = true;

            } else if (c == '"') {

                break;

            } else {

                result.append(c);
            }
        }

        return result.toString();
    }
}