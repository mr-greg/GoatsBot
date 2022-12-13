package me.goats.act4;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.awt.*;

public class PercentageCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("act4")) return;
        if (!event.isFromGuild()) {
            event.reply("You must send this command from a server !").queue();
        }

        Guild guild = event.getGuild();
        if (guild == null) return;

        TextChannel percentages = event.getGuild().getTextChannelById(1044443156600594512L);

        if (!event.getChannel().equals(percentages)){
            event.reply("Please use this command in the " + percentages.getAsMention() + " channel").setEphemeral(true).queue();
            return;
        }

        Dotenv config = Dotenv.load();
        try {
            Jedis jedis = new Jedis(config.get("JEDIS_HOST"), 38401);
            jedis.auth(config.get("JEDIS_AUTH"));

            String json = jedis.get("dump");
            //System.out.println(json);

            JSONObject obj = new JSONObject(json);

            String ange = obj.getString("ange");
            String demon = obj.getString("demon");
            String angeRaid = obj.getString("angeRaid");
            //System.out.println("angeRaid = " + angeRaid);
            String demonRaid = obj.getString("demonRaid");
            //System.out.println("demonRaid = " + demonRaid);
            long hour = obj.getLong("heure");


            // Convert heure
            String lastUpdate = "<t:" + Long.toString(hour) + ">";

            long hourPlus30 = hour += 30*60;
            //System.out.println("test " + hourPlus30);

            String heureBoss = "<t:" + Long.toString(hourPlus30) + ">";


            if (ange == null || demon == null || angeRaid == null || demonRaid == null) {
                event.reply("There is a DB issue, please contact Zaack#1160 with code error #DB01 in order to fix this").queue();
                return;
            }

            // SI RAID ANGE EN COURS
            if (angeRaid.contains("1")) {
                //System.out.println("angeraid egal 1 L89");

                EmbedBuilder pourcents = new EmbedBuilder()
                        .setTitle("Act4 Percentage - INT Asgobas", null)
                        .setColor(Color.WHITE)
                        .setDescription("Percentages update every minute")
                        .addField("Angel","RAID OPEN", true)
                        .addBlankField(true)
                        .addField("Demon", demon + "%", true)
                        .addField("Boss at", heureBoss, true)
                        .setFooter("If bot doesn't update after raid, please DM Zaack#1160")
                        .setThumbnail("https://i.imgur.com/gu9SV0s.png");

                event.replyEmbeds(pourcents.build()).queue( reply -> {
                    reply.retrieveOriginal().queue(message -> {
                        message.addReaction(Emoji.fromCustom("hatus", 1045076456109244480L, false)).queue();
                        message.addReaction(Emoji.fromCustom("morcos", 1045076457287852234L, false)).queue();
                        message.addReaction(Emoji.fromCustom("berios", 1045076342200352818L, false)).queue();
                        message.addReaction(Emoji.fromCustom("calvina", 1045076343336996926L, false)).queue();
                    });
                }, failure -> {
                    System.out.println("An error occured while trying to send the act4% (L107)");
                });

            }

            // SI RAID DEMON EN COURS
            if (demonRaid.contains("1")) {
                //System.out.println("demonraid egal 1 L106");

                EmbedBuilder pourcents = new EmbedBuilder()
                        .setTitle("Act4 Percentage - INT Asgobas", null)
                        .setColor(Color.RED)
                        .setDescription("Percentages update every minute")
                        .addField("Angel", ange + "%", true)
                        .addBlankField(true)
                        .addField("Demon", "RAID OPEN", true)
                        .addBlankField(true)
                        .addBlankField(true)
                        .addField("Boss at", heureBoss, true)
                        .setFooter("If bot doesn't update anymore, please DM Zaack#1160")
                        .setThumbnail("https://i.imgur.com/bTpygoq.png");


                event.replyEmbeds(pourcents.build()).queue( reply -> {
                    reply.retrieveOriginal().queue(message -> {
                        message.addReaction(Emoji.fromCustom("hatus", 1045076456109244480L, false)).queue();
                        message.addReaction(Emoji.fromCustom("morcos", 1045076457287852234L, false)).queue();
                        message.addReaction(Emoji.fromCustom("berios", 1045076342200352818L, false)).queue();
                        message.addReaction(Emoji.fromCustom("calvina", 1045076343336996926L, false)).queue();
                    });
                }, failure -> {
                    System.out.println("An error occured while trying to send the act4% (L107)");
                });
            }

            // SI PAS DE RAID EN COURS
            if (!demonRaid.contains("1") && !angeRaid.contains("1")) {
                EmbedBuilder pourcents = new EmbedBuilder()
                        .setTitle("Act4 Percentage - INT Asgobas", null)
                        .setColor(new Color(148, 56, 38))
                        .setDescription("Percentages update every minute")
                        .addField("Angel", ange + "%", true)
                        .addField("Demon", demon + "%", true)
                        .addField("Last Update", lastUpdate, false)
                        .setFooter("If bot doesn't update anymore, please DM Zaack#1160")
                        .setThumbnail("https://i.imgur.com/n7qPHDF.png");

                event.replyEmbeds(pourcents.build()).queue();
            }


        } catch (JedisDataException e) {
            event.reply("There is a DB issue, please contact Zaack#1160 with code error #DB02 in order to fix this").queue();
        }
    }
}
