package me.goats.translator;

import java.io.IOException;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class TranslatorText extends ListenerAdapter {

    private static String key = "9d5fe937dde84915b1cc10ff4aab9155";

    // location, also known as region.
    // required if you're using a multi-service or regional (not global) resource. It can be found in the Azure portal on the Keys and Endpoint page.
    private static String location = "westeurope";


    // Instantiates the OkHttpClient.
    OkHttpClient client = new OkHttpClient();

    public String endpoint = "https://api.cognitive.microsofttranslator.com";
    public String route = "/translate?api-version=3.0&to=de";
    public String url = endpoint.concat(route);



    // This function performs a POST request.
    public String Post() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \" Bonjour \"}]");


        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Ocp-Apim-Subscription-Key", key)
                // location required if you're using a multi-service or regional (not global) resource.
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    // This function prettifies the json response.
    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }



    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("translator")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }


        try {
            TranslatorText translateRequest = new TranslatorText();
            String response = translateRequest.Post();

            JsonArray rootArr = new JsonParser().parse(response).getAsJsonArray();
            System.out.println("ok first");
            System.out.println(rootArr);

            JsonArray translation = rootArr.get(0).getAsJsonArray();
            System.out.println("ok second");
            JsonObject firstObject = translation.get(0).getAsJsonObject();
            System.out.println("ok third");
            System.out.println(firstObject.get("text").getAsString());


        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public static void main(String[] args) {
        try {
            TranslatorText translateRequest = new TranslatorText();
            String response = translateRequest.Post();
            System.out.println(response);
            //System.out.println(prettify(response));

            JsonArray rootArr = new JsonParser().parse(response).getAsJsonArray();
            JsonObject firstObject = rootArr.get(0).getAsJsonObject();
            JsonArray translations = firstObject.get("translations").getAsJsonArray();
            JsonObject firstTranslation = translations.get(0).getAsJsonObject();
            String text = firstTranslation.get("text").getAsString();
            System.out.println(text);

            String targetLang = "de";
            String endpoint = "https://api.cognitive.microsofttranslator.com";
            String route = "/translate?api-version=3.0&to=" + targetLang + "\"";
            String url = endpoint.concat(route);
            System.out.println("route = " + route);
            System.out.println("url = " + url);


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}