package me.goats.raids;

import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AcceptButtonEvent extends ListenerAdapter {
    Database database = new Database();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("select:accept")) return;
        Guild guild = event.getGuild();

        try {

            List<Database.ParticipationRequest> ParticipationQuery = database.selectAllParticipationsRequest(event.getChannel().getId(), event.getMessageId());
            Database.ParticipationRequest participations = ParticipationQuery.get(0);




            String idLeader = participations.leaderId;
            String idParticipant = participations.memberId;
            String idSelectionChannel = participations.selectionChannelId;
            String idRaid = participations.raidId;

            List<Database.Raids> RaidsQueries = database.selectAllRaidsByRaidId(idRaid);
            Database.Raids raidsInfos = RaidsQueries.get(0);

            /*
            try {
                List<Database.Participants> ParticipantsQuery = database.selectAllParticipants(idRaid);
                Database.Participants participants = ParticipantsQuery.get(0);

                if (participants.idParticipant.equals(idParticipant)) {
                    event.reply(participations.pseudo + " is already in the raid. If not, please contact Zaack#1160 (Error code #10").setEphemeral(true).queue();
                    return;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("pas encore en participant");
            }
             */

            String sql = "INSERT INTO participant(idLeader, idParticipant, idSelectionChannel, idRaid) VALUES(?,?,?,?)";

            database.updateTableParticipant(sql, idLeader, idParticipant, idSelectionChannel, idRaid);


            List<Database.Raids> RaidsQuery = database.selectAllRaidsById(participations.selectionChannelId);
            Database.Raids raids = RaidsQuery.get(0);

            TextChannel salonTeam = event.getGuild().getTextChannelById(raids.idTeamChannel);

            String[] welcomeRandom = {"<@%s> has joined the team ! Good luck !",
                                    "<@%s> will join you in the battle !",
                                    "<@%s> is here... Welp we're fucked :)",
                                    "<@%s> might be an impostor... Anyway, hes on the team now :person_shrugging:",
                                    "<@%s> joined, pick ur baguette and get ready for tossing cheese on those bosses !",
                                    "<@%s> se ha unido al equipe ! Welcome cabron ! (sorry, it's all I got in spanish :( )",
                                    "<@%s> присоединился к команде ! Rush B cука !",
                                    "<@%s> joined us, hopefully he doesn't afk again...",
                                    "<@%s> a rejoint l'équipe ! Bonjour mon ami",
                                    "<@%s> joined... That's one way of wasting a spot I guess '-'",
                                    "Wow ! <@%s> almost made me drop my croissant !",
                                    "<@%s> joined. Run.",
                                    "<@%s> took the sword, seize the shields !",
                                    "<@%s> is here, reach out to him",
                                    "Poor <@%s> has stepped on a poop, who's was it ?",
                                    "Eumh, Guys ? <@%s> has joined, please contact support, this might be a mistake",
                                    "<@%s> has joined the team, weird choice but ok ?",
                                    "<@%s> has stepped on a bee",
                                    "<@%s> is here ! Give him a standing ovation for showing up",

            };

            String message;
            if (welcomeRandom == null || welcomeRandom.length == 0) {
                message = "<@%s> has joined the team!";
            } else {
                message = welcomeRandom[ThreadLocalRandom.current().nextInt(welcomeRandom.length)];
            }
            salonTeam.sendMessage(String.format(message, idParticipant)).queue();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("You have been accepted for the raid : " + participations.raidName);
            eb.setDescription("**Raid by : <@" + participations.leaderId + ">** \n"
                    + "**Date and time :** " + raidsInfos.dateTime + "\n"
                    + "*You have now access to the text channel of your team, please check it out !*");
            eb.setFooter("If you encouter any issue with this bot, please contact Zaack#1160");
            eb.setColor(Color.GREEN);

            TextChannel registerChannel = guild.getTextChannelById(raids.idRegisterChannel);
            //récupérer l'id de l'embed avec les infos ? si pas possible, le faire passer en DB
            MessageHistory registerHistory = registerChannel.getHistoryFromBeginning(1).complete();
            String embedRegisterId = registerHistory.getRetrievedHistory().get(0).getId();

            StringBuilder sb = new StringBuilder();

            List<Database.Participants> ParticipantsQuery = database.selectAllParticipants(idRaid);

            for (Database.Participants ids : ParticipantsQuery) {
                System.out.println("tour de boucle accept");
                String idPart = ids.idParticipant;
                sb.append("<@").append(idPart).append("> \n");
            }

            Long idEmoji = null;
            String linkThumbnail = null;
            switch (raidsInfos.raidName.toLowerCase()) {

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



            //récup infos en db (celles qui sont fix) puis boucle sur les participants ?
            EmbedBuilder editedEmbed = new EmbedBuilder();
            editedEmbed.setTitle("Raid : " + raidsInfos.raidName);
            editedEmbed.setColor(Color.WHITE);
            editedEmbed.setThumbnail(linkThumbnail);
            editedEmbed.setDescription(
                    "Hosted by : <@" + raidsInfos.idLeader + ">\n"
                    + "Time and date : " + raidsInfos.dateTime + "\n"
                    + "Number of raids : " + raidsInfos.nbRaid + "\n"
                    + "Need to provide : " + raidsInfos.aFournir + "\n"
                    + "Spots : " + raidsInfos.nbPlace
            );

            editedEmbed.addField("Other informations : ", raidsInfos.autre, false);
            editedEmbed.addField("Members :", "" + sb, false);
            editedEmbed.setFooter("The number of spots available isn't updated on this message, the Leader has to do it ! If you need any help, please ask in help channels or contact Zaack#1160");

            registerChannel.editMessageEmbedsById(embedRegisterId, editedEmbed.build()).queue();

            // registerChannel.editMessageEmbedsById(embedRegisterId, )

            String reqSql = "DELETE FROM participationRequest WHERE raidId = ? AND memberId = ?";
            database.deleteParticipationRequest(reqSql, idRaid, participations.memberId);

            guild.getJDA().retrieveUserById(idParticipant).queue(replyUser -> {
                User user = replyUser;
                event.getMessage().delete().queue();
                replyUser.openPrivateChannel().queue(replyUserDm -> {
                    replyUserDm.sendMessageEmbeds(eb.build()).submit()
                            .whenComplete((s, error) -> {
                                if (error != null) {
                                    event.reply(participations.pseudo + " has been successfully added to the team, but I couldn't send a DM to notify him ! Please tell him to open his DMs on the team's chat").setEphemeral(true).queue();
                                } else {
                                    event.reply(participations.pseudo + " has been successfully added to the team !").setEphemeral(true).queue();
                                }
                            });
                    Role role = guild.getRoleById(raids.idRole);
                    guild.addRoleToMember(replyUser, role).queue();
                });
            });
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // SELECT * FROM participationRequest WHERE selectionChannelId = event.getChannelId()

    }
}
