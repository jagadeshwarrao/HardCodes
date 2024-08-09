import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Solution {
    public static int run(String teamKey) {
        int goals = 0;

        try {
            // URL of the JSON file
            URL url = new URL("https://s3.eu-west-1.amazonaws.com/hackajob-assets1.p.hackajob/challenges/football_session/football.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonText = new StringBuilder();
            String line;

            // Read the entire JSON content
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
            reader.close();

            // Parse JSON content using Gson
            JsonElement jsonElement = new JsonParser().parse(jsonText.toString());
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray rounds = jsonObject.getAsJsonArray("rounds");

            // Iterate through rounds
            for (int i = 0; i < rounds.size(); i++) {
                JsonObject round = rounds.get(i).getAsJsonObject();
                JsonArray matches = round.getAsJsonArray("matches");

                // Iterate through matches
                for (int j = 0; j < matches.size(); j++) {
                    JsonObject match = matches.get(j).getAsJsonObject();

                    JsonObject team1 = match.getAsJsonObject("team1");
                    JsonObject team2 = match.getAsJsonObject("team2");

                    // Check if team1 matches the teamKey
                    if (team1.get("key").getAsString().equals(teamKey)) {
                        goals += match.get("score1").getAsInt();
                    }

                    // Check if team2 matches the teamKey
                    if (team2.get("key").getAsString().equals(teamKey)) {
                        goals += match.get("score2").getAsInt();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return goals;
    }
}
