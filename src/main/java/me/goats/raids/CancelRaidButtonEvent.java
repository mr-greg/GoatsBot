package me.goats.raids;

import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CancelRaidButtonEvent extends ListenerAdapter {
    Database database = new Database();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("idcancelraid")) return;
        Guild guild = event.getGuild();
        try {
            // raids = infos du raid
            // delete le raid de la database (à la fin)
            List<Database.Raids> RaidsQuery = database.selectAllRaidsById(event.getChannel().getId());
            Database.Raids raids = RaidsQuery.get(0);

            // participants = info participants
            // boucler à travers les IDmember des participants et leur envoyer un DM pour indiquer que c'est annulé
            List<Database.Participants> ParticipantsQuery = database.selectAllParticipants(raids.id);

            for (Database.Participants participant : ParticipantsQuery) {
                guild.getJDA().retrieveUserById(participant.idParticipant).queue(reply -> {
                    reply.openPrivateChannel().queue(replyUserDm -> {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("RAID " + raids.raidName + " CANCELED !");
                        eb.setDescription("Raid by : <@" + raids.idLeader + "> \n"
                                + "Date and Time : " + raids.dateTime + "\n"
                                + "This raid has been canceled by its leader, feel free to register to another raid !");
                        eb.setFooter("If you encouter any issue with this bot, please contact Zaack#1160");
                        eb.setColor(Color.RED);

                        replyUserDm.sendMessageEmbeds(eb.build()).queue();
                    });
                });
            }

            // partitipationReq = info participationRequests
            // boucler à travers les IDmember des requesters et leur envoyer un DM pour indiquer que c'est annulé
            List<Database.VerifyParticipationRequest> participationRequests = database.ParticipationRequestCheck(raids.idSelectionChannel);

            for (Database.VerifyParticipationRequest requester : participationRequests) {
                guild.getJDA().retrieveUserById(requester.memberId).queue(reply -> {
                    reply.openPrivateChannel().queue(replyUserDm -> {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("RAID " + raids.raidName + " CANCELED !");
                        eb.setDescription("Raid by : <@" + raids.idLeader + "> \n"
                                + "Date and Time : " + raids.dateTime + "\n"
                                + "This raid has been canceled by its leader, feel free to register to another raid !");
                        eb.setFooter("If you encouter any issue with this bot, please contact Zaack#1160");
                        eb.setColor(Color.RED);

                        replyUserDm.sendMessageEmbeds(eb.build()).queue();
                    });
                });
            }

            guild.getJDA().retrieveUserById(event.getUser().getId()).queue(reply2 -> {
                reply2.openPrivateChannel().queue(replyLeaderDm -> {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("RAID " + raids.raidName + " CANCELED !");
                    eb.setDescription("Raid by : <@" + raids.idLeader + "> \n"
                            + "Date and Time : " + raids.dateTime + "\n"
                            + "I'm sorry everything didn't go as planned, hopefully next time's the charm ! Feel free to contact Zaack to discuss any issues you encountered !");
                    eb.setFooter("If you encountered any issue with this bot, please contact Zaack#1160");
                    eb.setColor(Color.RED);

                    replyLeaderDm.sendMessageEmbeds(eb.build()).queue();
                });
            });

            try {
                guild.getJDA().getRoleById(raids.idRole).delete().queue();
                guild.getJDA().getTextChannelById(raids.idTeamChannel).delete().queue();
                guild.getJDA().getTextChannelById(raids.idSelectionChannel).delete().queue();
                guild.getJDA().getTextChannelById(raids.idRegisterChannel).delete().queue();
            } catch (NullPointerException e){
                System.out.println("salon deja supprime");
            }


            // delete en DB les participants + reqParticipant + raids
            String deletePartReqSql = "DELETE FROM participationRequest WHERE raidId = ?";
            database.deleteRaidFromPartReq(deletePartReqSql, raids.id);

            String deletePartSql = "DELETE FROM participant WHERE idRaid = ?";
            database.deleteRaidFromPart(deletePartSql, raids.id);

            String deleteRaidSql = "DELETE FROM raids WHERE id = ?";
            database.deleteRaid(deleteRaidSql, raids.id);

            // Delete le role + les salons

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
