import java.net.http.*;
import java.net.URI;
import java.util.regex.*;

public class SentimentoIA {
    public static void main(String[] args) throws Exception {
        String endpoint = "https://sentimentalia.cognitiveservices.azure.com/";
        String key = "7jwdOvrJ80G0OTsDBf3cplyzKCtzvKYfqtIB3yPLsKvjK0MydzJJJQQJ99BJACBsN54XJ3w3AAAaACOGfDha";

        String[] textos = {
            "O atendimento foi excelente, a comida chegou rápido e estava deliciosa. Com certeza voltarei mais vezes!",
            "O restaurante fica no centro da cidade e abre todos os dias das 11h às 22h.",
            "O pedido demorou muito para chegar e a comida estava fria e sem sabor."
        };

        HttpClient client = HttpClient.newHttpClient();
        boolean showJson = Boolean.parseBoolean(System.getenv().getOrDefault("SHOW_JSON", "true"));

        for (int i = 0; i < textos.length; i++) {
            String body = """
            {
              "kind": "SentimentAnalysis",
              "parameters": { "opinionMining": false },
              "analysisInput": {
                "documents": [ { "id": "1", "language": "pt-BR", "text": "%s" } ]
              }
            }
            """.formatted(textos[i]);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint + "language/:analyze-text?api-version=2024-11-01"))
                .header("Content-Type", "application/json")
                .header("Ocp-Apim-Subscription-Key", key)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            System.out.println("🧩 Texto " + (i + 1) + ": " + textos[i]);
            if (showJson) {
                System.out.println("Resposta JSON do Azure:\n");
                System.out.println("Response: "+json);
            }


            String sentimento = extract(json, "\"sentiment\"\\s*:\\s*\"(positive|neutral|negative|mixed)\"", "desconhecido");
            double pos = extractDouble(json, "\"confidenceScores\"[^}]*\"positive\"\\s*:\\s*([0-9.]+)");
            double neu = extractDouble(json, "\"confidenceScores\"[^}]*\"neutral\"\\s*:\\s*([0-9.]+)");
            double neg = extractDouble(json, "\"confidenceScores\"[^}]*\"negative\"\\s*:\\s*([0-9.]+)");

            System.out.printf("Resultado:  sentimento: %s | positivo: %.2f neutro: %.2f negativo: %.2f%n", sentimento, pos, neu, neg);
            System.out.println("\n------------------------------------------------------------\n");
        }
    }

    //Achar padrões com REGEX
    private static String extract(String src, String regex, String def) {
        Matcher m = Pattern.compile(regex).matcher(src);
        return m.find() ? m.group(1) : def;
    }

    private static double extractDouble(String src, String regex) {
        Matcher m = Pattern.compile(regex).matcher(src);
        return m.find() ? Double.parseDouble(m.group(1)) : Double.NaN;
    }
}
