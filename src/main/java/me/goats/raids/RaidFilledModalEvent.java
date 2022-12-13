package me.goats.raids;

import io.github.cdimascio.dotenv.Dotenv;
import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;

public class RaidFilledModalEvent extends ListenerAdapter {
    Database database = new Database();
    Dotenv dotenv = Dotenv.load();

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals("creationModal")) {
            //System.out.println("pas le bon id");
            return;
        }

        System.out.println(event.getMember().getEffectiveName() + "créé d'un raid : " + LocalDateTime.now());
        try {
            String idretrievermsg = database.idretrievermsgFromDataSelect(event.getUser().getId());


            if (idretrievermsg == null) {
                event.reply("You only have 5 minutes max to fill the modal, please try again. If this error persists, please contact Zaack#1160 (error code #12)").setEphemeral(true).queue();
                return;
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
            event.reply("You only have 5 minutes max to fill the modal, please try again. If this error persists, please contact Zaack#1160 (error code #12)").setEphemeral(true).queue();
            return;
        }

        Member member = event.getMember();
        Guild guild = event.getGuild();
        if (member == null || guild == null) return;

        System.out.println("Cond1 OK");

        String dateHeure = event.getValue("heure:input").getAsString();
        String nbRaid = event.getValue("nombreRaid:input").getAsString();

        System.out.println("dateHeure =" + dateHeure + " / nbRaid : " + nbRaid);

        String nbPlaceStr = event.getValue("nombrePlace:input").getAsString();
        System.out.println("nb place avant parse : " + nbPlaceStr);


        String fournir = event.getValue("fournir:input").getAsString();
        System.out.println("fournir = " + fournir);

        String autre = event.getValue("autre:input").getAsString();
        System.out.println("autre = " + autre);

        String idLeader = member.getId();

        System.out.println("idLeader = " + idLeader);

        String idRetrieverMsg = database.idretrievermsgFromDataSelect(idLeader);
        System.out.println("idRetriverMsg = " + idRetrieverMsg);

        String raidName = database.raidNameFromDataSelect(idLeader);
        System.out.println("raidName = " + raidName);

        if (idRetrieverMsg == null || raidName == null) {
            event.reply("I haven't be able to retrieve some infos, please contact Zaack#1160 with error code #08").setEphemeral(true).queue();
            return;
        }

        String leaderName = member.getEffectiveName();
        System.out.println("leaderName = " + leaderName);
        Category categoryTeams = guild.getCategoryById(1037397987187511416L);
        Category categoryRegister = guild.getCategoryById(1038005703408877599L);
        System.out.println("categoryTeams = " + categoryTeams);


        guild.createRole().setName(leaderName + "_" + raidName).queue(reply -> {
            System.out.println("rentre dans la création du role LEADERNAME_RAIDNAME");
            System.out.println("----------------");
            String idRole = reply.getId();
            System.out.println("idRole = " + idRole);
            guild.addRoleToMember(member, reply.getManager().getRole()).queue();

            guild.createTextChannel(leaderName + "_" + raidName + "_TEAM").setParent(categoryTeams)
                    .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(reply.getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)



                    .queue(reply2 -> {

                System.out.println("rentre dans la création du salon LEADERNAME_RAIDNAME_TEAM");
                String idTeamChannel = reply2.getId();
                System.out.println("idRegisterChannel = " + idTeamChannel);

                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Welcome to the team selection !");
                eb.setColor(Color.WHITE);
                eb.setDescription("Please react with yes or no to accept or deny the registering request, requests will appear when users do register to your raid !\n"
                + "**Do not forget to type a message in the register channel to inform how many spots are left !**");
                eb.setFooter("If you need any help, please contact Zaack#1160 or ask other Leaders");

                guild.createTextChannel(leaderName + "_" + raidName + "_SELECTION")
                        .setParent(categoryTeams)
                        .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                        .addMemberPermissionOverride(member.getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .queue(reply3 -> {

                            Button raidTermine = Button.success("idcompleteraid", "Raid completed");
                            Button closeRegister = Button.primary("idcloseregister", "Close registrations");
                            Button annulerRaidButton = Button.danger("idcancelraid", "Cancel Raid");


                            System.out.println("rentre dans la création du salon LEADERNAME_RAIDNAME_SELECTION");
                            String idSelectionChannel = reply3.getId();
                            reply3.sendMessageEmbeds(eb.build()).addActionRow(raidTermine, closeRegister, annulerRaidButton).queue();

                    guild.createTextChannel(leaderName + "_" + raidName + "_REGISTER").setParent(categoryRegister)
                            .syncPermissionOverrides()
                            .addMemberPermissionOverride(Long.parseLong(idLeader), Collections.singleton(Permission.MESSAGE_SEND), null)
                            .queue(reply5 -> {
                        String idRegisterChannel = reply5.getId();

                        String idSelectChannel = reply3.getId();
                        System.out.println("selectionChannel = " + idSelectChannel);



                        System.out.println("fin dernier reply, updateRaids fire");

                        TextChannel idRetriver = guild.getTextChannelById(1037268617118625832L);

                        idRetriver.sendMessage("<@" + idLeader + "> a confirmé la création du raid " + raidName).queue();

                        // AVEC LE idRegisterChannel, envoyer le message embed pour l'inscritpion (prévoir bouton "Register" and "Unregister")
                        EmbedBuilder register = new EmbedBuilder();
                        register.setTitle("Raid : " + raidName);
                        register.setColor(Color.WHITE);
                        register.setDescription(
                                "Hosted by : " + event.getMember().getAsMention() + "\n"
                                + "Time and date : " + dateHeure + "\n"
                                + "Number of raids : " + nbRaid + "\n"
                                + "Need to provide : " + fournir + "\n"
                                + "Spots : " + nbPlaceStr);
                        register.addField("Other informations : \n", autre, false);

                                System.out.println("raidName to lower case : " + raidName.toLowerCase());
                        Long idEmoji = null;
                        String linkThumbnail = null;
                        switch (raidName.toLowerCase()) {

                            case "alzanor" -> {
                                idEmoji = 1036531966830460928L;
                                linkThumbnail = "https://i.imgur.com/syWLXmp.png";
                            } case "valehir" -> {
                                idEmoji = 1036531993967599618L;
                                linkThumbnail = "https://i.imgur.com/ucBVylq.png";
                            } case "paimon revenant" -> {
                                idEmoji = 1036531990591184918L;
                                linkThumbnail = "https://i.imgur.com/NZlFAdc.png";
                            } case "paimon" -> {
                                idEmoji = 1036531989521645638L;
                                linkThumbnail = "https://i.imgur.com/CZiAgTp.png";
                            } case "belial" -> {
                                idEmoji = 1036531971892985906L;
                                linkThumbnail = "https://i.imgur.com/dheFyX6.png";
                            } case "kirollas" -> {
                                idEmoji = 1036531987051192390L;
                                linkThumbnail = "https://i.imgur.com/QQ2vfcX.png";
                            } case "carno" -> {
                                idEmoji = 1036531973411307540L;
                                linkThumbnail = "https://i.imgur.com/SanEcKn.png";
                            } case "fernon" -> {
                                idEmoji = 1036531979803439134L;
                                linkThumbnail = "https://i.imgur.com/EKhMfpH.png";
                            } case "zenas" -> {
                                idEmoji = 1036531996677111818L;
                                linkThumbnail = "https://i.imgur.com/hqDE50A.png";
                            } case "erenia" -> {
                                idEmoji = 1036531976003395665L;
                                linkThumbnail = "https://i.imgur.com/RPv52nC.png";
                            } case "grenigas" -> {
                                idEmoji = 1036531982508752916L;
                                linkThumbnail = "https://i.imgur.com/FNnEOXW.png";
                            } case "valakus" -> {
                                idEmoji = 1036531992826753127L;
                                linkThumbnail = "https://i.imgur.com/dxy0nMy.png";
                            } case "kertos" -> {
                                idEmoji = 1036531985344114741L;
                                linkThumbnail = "https://i.imgur.com/NQtmFzU.png";
                            } case "ibrahim" -> {
                                idEmoji = 1036531983544750150L;
                                linkThumbnail = "https://i.imgur.com/a1BZD0H.png";
                            } case "glacerus/draco" -> {
                                idEmoji = 1036531981455998996L;
                                linkThumbnail = "https://i.imgur.com/kdnNX7X.png";
                            } case "laurena" -> {
                                idEmoji = 1036531988196237322L;
                                linkThumbnail = "https://i.imgur.com/B606oRl.png";
                            } case "yertirand" -> {
                                idEmoji = 1036531995255246870L;
                                linkThumbnail = "https://i.imgur.com/RNc73Su.png";
                            } case "fafnir" -> {
                                idEmoji = 1036531978641608735L;
                                linkThumbnail = "https://i.imgur.com/wy5pmJ8.png";
                            } case "event" -> {
                                idEmoji = 1036531977144242276L;
                                linkThumbnail = "https://i.imgur.com/57L7nls.png";
                            } case "other raid" -> {
                                idEmoji = 1035894318197657601L;
                                linkThumbnail = "https://i.imgur.com/C5VbYAb.png";
                            }
                        }

                        register.setThumbnail(linkThumbnail);
                        System.out.println(linkThumbnail);



                        register.setFooter("The number of spots available isn't updated on this message, the Leader has to do it ! If you need any help, please ask in help channels or contact Zaack#1160");

                        Button inscriptionRaidButton = Button.success("idbuttonregister", "Register");
                        Button desinscriptionRaidButton = Button.danger("idbuttonunregister", "Un-register");

                        TextChannel textChannelRegister = guild.getTextChannelById(idRegisterChannel);
                                System.out.println("RaidName is filled event : " + raidName);
                        String idRolePing = guild.getJDA().getRolesByName(raidName, true).get(0).getId();
                        textChannelRegister.sendMessage("<@&" + idRolePing + ">").setEmbeds(register.build())
                                .addActionRow(inscriptionRaidButton, desinscriptionRaidButton)
                                .queue(reply6 -> {

                                    String newIdRetrieverMsg = reply6.getId();

                                    String sql = "INSERT INTO raids(idLeader, idRole, idChannel, idretrievermsg, raidName, nbPlace, nbRaid, dateTime, aFournir, autre, idTeamChannel, idRegisterChannel, idSelectionChannel ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

                                    database.updateRaids(sql, idLeader, idRole, event.getChannel().getId(), newIdRetrieverMsg, raidName, nbPlaceStr, nbRaid, dateHeure, fournir, autre, idTeamChannel, idRegisterChannel, idSelectionChannel);
                                });

                        event.reply("Your raid has been successfully created, please checkout the messages in the register category and teams category, some channels with your name have been created !").setEphemeral(true).queue();

                        TextChannel salonSelect = guild.getTextChannelById(Long.parseLong(dotenv.get("SALONSELECTID")));


                        String sqlDelete = "DELETE FROM dataSelect WHERE idLeader = ?";
                        database.deleteDataSelect(sqlDelete, idLeader);


                    });
                });
            });


        });
    }
}
