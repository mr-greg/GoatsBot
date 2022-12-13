package me.goats.act4;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.awt.*;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReadyEvent extends ListenerAdapter {

    public static boolean raidAlreadyNotified = false;

    @Override
    public void onReady(net.dv8tion.jda.api.events.session.ReadyEvent e) {

        Dotenv config = Dotenv.load();

        try {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            Runnable task = () -> {
                //System.out.println(LocalTime.now());
                Jedis jedis = new Jedis(config.get("JEDIS_HOST"), 38401);
                jedis.auth(config.get("JEDIS_AUTH"));
                // 2 - get les valeurs de s'il y a un raid dans la database
                String json = jedis.get("dump");

                JSONObject obj = new JSONObject(json);

                String ange = obj.getString("angeRaid");

                String demon = obj.getString("demonRaid");
                long hour = obj.getLong("heure");

                // Convert heure

                long hourPlus30 = hour += 30*60;

                String heureBoss = "<t:" + Long.toString(hourPlus30) + ">";


                //System.out.println("etat demon : " + demon);
                //System.out.println("etat ange : " + ange);

                if (!demon.equals("1")) {
                    if (!ange.equals("1")) {
                        raidAlreadyNotified = false;
                    }
                }

                if (!ange.equals("1")) {
                    if (!demon.equals("1")) {
                        raidAlreadyNotified = false;
                    }
                }
                //System.out.println("état notif : " + raidAlreadyNotified);
                if (raidAlreadyNotified) {
                    //System.out.println("déjà notif, return");
                    return;
                }
                // 3 - créer un message embed :
                EmbedBuilder raids = new EmbedBuilder();

                if (ange.equals("1")) {
                    raids.setTitle("INT - Asgobas | ANGEL RAID !", null);
                    raids.setThumbnail("https://i.imgur.com/gu9SV0s.png");
                    raids.setDescription("Angel raid open ! Please react with the element of the raid");
                    raids.setColor(Color.WHITE);
                    raids.addField("Boss at", heureBoss, true);

                    if (!raidAlreadyNotified) {

                            TextChannel channelToSend = e.getJDA().getTextChannelById(1044440542320267284L);

                            if (channelToSend == null) return;
                            if (channelToSend.getIdLong() == 1044440542320267284L) {
                                channelToSend.sendMessage("<@&1044739681536966756>").setEmbeds(raids.build()).queue(reply -> {
                                    reply.addReaction(Emoji.fromCustom("hatus", 1045076456109244480L, false)).queue();
                                    reply.addReaction(Emoji.fromCustom("morcos", 1045076457287852234L, false)).queue();
                                    reply.addReaction(Emoji.fromCustom("berios", 1045076342200352818L, false)).queue();
                                    reply.addReaction(Emoji.fromCustom("calvina", 1045076343336996926L, false)).queue();

                                });
                            }
                        raidAlreadyNotified = true;
                    }

                }
                if (demon.equals("1")) {
                    System.out.println("cond demon 1");
                    raids.setTitle("INT - Asgobas | DEMON RAID !", null);
                    raids.setThumbnail("https://i.imgur.com/bTpygoq.png");
                    raids.setColor(Color.RED);
                    raids.setDescription("Demon raid open ! Please react with the element of the raid");
                    raids.addField("Boss at", heureBoss, true);

                    if (!raidAlreadyNotified) {

                        TextChannel channelToSend = e.getJDA().getTextChannelById(1044440542320267284L);

                            if (channelToSend.getIdLong() == 1044440542320267284L) {
                                channelToSend.sendMessage("<@&1044739765771194539>").setEmbeds(raids.build()).queue(reply -> {
                                    reply.addReaction(Emoji.fromCustom("hatus", 1045076456109244480L, false)).queue();
                                    reply.addReaction(Emoji.fromCustom("morcos", 1045076457287852234L, false)).queue();
                                    reply.addReaction(Emoji.fromCustom("berios", 1045076342200352818L, false)).queue();
                                    reply.addReaction(Emoji.fromCustom("calvina", 1045076343336996926L, false)).queue();
                                });
                            }

                        raidAlreadyNotified = true;
                        //System.out.println("etat notif 2 : " + raidAlreadyNotified);
                    }
                }
            };

            int initialDelay = 3;
            int periodicDelay = 60;
            scheduler.scheduleAtFixedRate(task, initialDelay, periodicDelay, TimeUnit.SECONDS);

        } catch (JedisDataException error) {

            TextChannel channelToSendError = e.getJDA().getTextChannelById(1044440542320267284L);

            if (channelToSendError == null) return;

            channelToSendError.sendMessage("There is a DB issue, please contact Zaack#5631 in order to fix this").queue();

        }
    }

}
