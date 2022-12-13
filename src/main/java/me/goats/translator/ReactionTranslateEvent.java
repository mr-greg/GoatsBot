package me.goats.translator;

import com.google.gson.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReactionTranslateEvent extends ListenerAdapter {

    //FR - GB - DE - ES - PL - RU
    String[] flags = {"\uD83C\uDDEB\uD83C\uDDF7", "\uD83C\uDDEC\uD83C\uDDE7", "\uD83C\uDDE9\uD83C\uDDEA", "\uD83C\uDDEA\uD83C\uDDF8", "\uD83C\uDDF5\uD83C\uDDF1", "\uD83C\uDDF7\uD83C\uDDFA"};

    private static String key = "9d5fe937dde84915b1cc10ff4aab9155";
    private static String location = "westeurope";
    OkHttpClient client = new OkHttpClient();


    public String Post(String url, String toTranslate) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \" " + toTranslate + "\"}]");


        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Ocp-Apim-Subscription-Key", key)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    public static List<String> usingSubstringMethod(String text, int n) {
        List<String> results = new ArrayList<>();
        int length = text.length();

        for (int i = 0; i < length; i += n) {
            results.add(text.substring(i, Math.min(length, i + n)));
        }

        return results;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        String emoji = event.getEmoji().getAsReactionCode();
        if (!Arrays.asList(flags).contains(emoji)) return;
        if (!event.isFromGuild()) return;

        String messageId = event.getMessageId();
        MessageChannelUnion channel = event.getChannel();
        String userId = event.getUserId();
        Guild guild = event.getGuild();

        String targetLang = "fr";


        switch (emoji) {
            case "\uD83C\uDDEB\uD83C\uDDF7" -> {
                targetLang = "fr";
            }
            case "\uD83C\uDDEC\uD83C\uDDE7" -> {
                targetLang = "en";
            }
            case "\uD83C\uDDE9\uD83C\uDDEA" -> {
                targetLang = "de";
            }
            case "\uD83C\uDDEA\uD83C\uDDF8" -> {
                targetLang = "es";
            }
            case "\uD83C\uDDF5\uD83C\uDDF1" -> {
                targetLang = "pl";
            }
            case "\uD83C\uDDF7\uD83C\uDDFA" -> {
                targetLang = "ru";
            }
        }
        System.out.println(targetLang);

        OkHttpClient client = new OkHttpClient();


        String endpoint = "https://api.cognitive.microsofttranslator.com";
        String route = "/translate?api-version=3.0&to=" + targetLang;
        String url = endpoint.concat(route);

        String langEmbed = targetLang;


        channel.retrieveMessageById(messageId).queue(message -> {
            if (!message.getEmbeds().isEmpty()) {
                MessageEmbed embed = message.getEmbeds().get(0);

                String embedTitle = embed.getTitle() + "\n";
                String embedDescription = "";
                if (!(embed.getDescription() == null)) {
                    embedDescription = embed.getDescription() + "\n";
                }
                List<MessageEmbed.Field> fields = embed.getFields();

                String fieldsString = "";
                for (MessageEmbed.Field field : fields) {
                    fieldsString += "**" + field.getName() + "**";
                    fieldsString += "\n";
                    fieldsString += field.getValue();
                    fieldsString += "\n";
                }
                String embedFooter = "";
                if (!(embed.getFooter() == null)){
                    embedFooter = embed.getFooter().getText();
                }

                String embedString = embedTitle + embedDescription + fieldsString + embedFooter;

                ReactionTranslateEvent translationRequest = new ReactionTranslateEvent();

                try {
                    String response = translationRequest.Post(url,embedString);
                    JsonArray rootArr = new JsonParser().parse(response).getAsJsonArray();
                    JsonObject firstObject = rootArr.get(0).getAsJsonObject();
                    JsonArray translations = firstObject.get("translations").getAsJsonArray();
                    JsonObject firstTranslation = translations.get(0).getAsJsonObject();
                    String translated = firstTranslation.get("text").getAsString();

                    List<String> results = usingSubstringMethod(translated, 1000);
                    List<String> originals = usingSubstringMethod(embedString, 1000);
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Translation in \"" + langEmbed + "\"");
                    eb.addField("Original message :", "",false);
                    for (String original : originals) {
                        eb.addField("", original, false);
                    }
                    eb.addField("", "-------------------------------", false);
                    eb.addField("Translation :", "", false);
                    for (String result : results) {
                        eb.addField("",result,false);
                    }
                    eb.setColor(Color.white);
                    eb.setThumbnail("https://i.imgur.com/NLWNskp.png");

                    // Envoie le message en MP à l'user
                    guild.getJDA().retrieveUserById(userId).queue(replyUser -> {
                        replyUser.openPrivateChannel().queue(replyUserDm -> {
                            replyUserDm.sendMessageEmbeds(eb.build()).queue();
                        });
                    });

                    // Envoie le message dans le salon du serveur (msg est publique)
                    //channel.sendMessage(event.getMember().getAsMention()).setEmbeds(eb.build()).queue();

                } catch (IOException | RuntimeException e) {
                    System.out.println("oopsi");
                    throw new RuntimeException(e);
                }
                return;
            }

            String contenu = message.getContentDisplay().replaceAll("\"", "'");

            ReactionTranslateEvent translationRequest = new ReactionTranslateEvent();
            try {
                String response = translationRequest.Post(url,contenu);
                JsonArray rootArr = new JsonParser().parse(response).getAsJsonArray();
                JsonObject firstObject = rootArr.get(0).getAsJsonObject();
                JsonArray translations = firstObject.get("translations").getAsJsonArray();
                JsonObject firstTranslation = translations.get(0).getAsJsonObject();
                String translated = firstTranslation.get("text").getAsString();

                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Translation in \"" + langEmbed + "\"");
                eb.addField("Original message :", contenu,false);
                eb.addField("Translation :", translated, false);
                eb.setColor(Color.white);
                eb.setThumbnail("https://i.imgur.com/NLWNskp.png");

                // Envoie le message en MP à l'user
                guild.getJDA().retrieveUserById(userId).queue(replyUser -> {
                    replyUser.openPrivateChannel().queue(replyUserDm -> {
                        replyUserDm.sendMessageEmbeds(eb.build()).queue();
                    });
                });

                // Envoie le message dans le salon du serveur (msg est publique)
                //channel.sendMessage(event.getMember().getAsMention()).setEmbeds(eb.build()).queue();


            } catch (IOException e) {
                System.out.println("oopsi");
                throw new RuntimeException(e);
            }

        });




    }
}
