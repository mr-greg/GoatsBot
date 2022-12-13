package me.goats.raids;

import io.github.cdimascio.dotenv.Dotenv;
import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CompleteRaidButtonEvent extends ListenerAdapter {
    Database database = new Database();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("idcompleteraid")) return;
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
                        eb.setTitle("RAID " + raids.raidName + " COMPLETE !");
                        eb.setDescription("Raid by : <@" + raids.idLeader + "> \n"
                                + "Date and Time : " + raids.dateTime + "\n"
                                + "Thank you for your participation ! Feel free to register to another raid or give us feedback of your experience on the server ! :)");
                        eb.setFooter("If you encouter any issue with this bot, please contact Zaack#1160");
                        eb.setColor(Color.GREEN);

                        replyUserDm.sendMessageEmbeds(eb.build()).submit()
                                .whenComplete((s, error) -> {
                                    if (error != null) System.out.println("someone didn't have their dm open to congratulate them");
                                });
                    });
                });
            }

            guild.getJDA().retrieveUserById(event.getUser().getId()).queue(reply2 -> {
                reply2.openPrivateChannel().queue(replyLeaderDm -> {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("RAID " + raids.raidName + " COMPLETE !");
                    eb.setDescription("Raid by : <@" + raids.idLeader + "> \n"
                            + "Date and Time : " + raids.dateTime + "\n"
                            + "Congratulations on completing your raid ! Feel free to plan more or give us feedback on your experience with the bot on the server ! :)");
                    eb.setFooter("If you encouter any issue with this bot, please contact Zaack#1160");
                    eb.setColor(Color.GREEN);

                    replyLeaderDm.sendMessageEmbeds(eb.build()).queue();
                });
            });

            // partitipationReq = info participationRequests
            // boucler à travers les IDmember des requesters et leur envoyer un DM pour indiquer que c'est annulé
            List<Database.VerifyParticipationRequest> participationRequests = database.ParticipationRequestCheck(raids.idSelectionChannel);

            for (Database.VerifyParticipationRequest requester : participationRequests) {
                guild.getJDA().retrieveUserById(requester.memberId).queue(reply -> {
                    reply.openPrivateChannel().queue(replyUserDm -> {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("RAID " + raids.raidName + " OVER !");
                        eb.setDescription("Raid by : <@" + raids.idLeader + "> \n"
                                + "Date and Time : " + raids.dateTime + "\n"
                                + "This raid is now over, you'll get in next time !");
                        eb.setFooter("If you encouter any issue with this bot, please contact Zaack#1160");
                        eb.setColor(Color.RED);

                        replyUserDm.sendMessageEmbeds(eb.build()).queue();
                    });
                });
            }

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

            TextChannel allChat = guild.getTextChannelById(Dotenv.load().get("CHATALLCHANNELID"));
            allChat.sendMessage("Team <@" + raids.idLeader + "> has completed the raid(s) " + raids.raidName + " congratulations !").queue();

            // Delete le role + les salons

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
