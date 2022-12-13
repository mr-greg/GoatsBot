package me.goats.raids;

import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DenyButtonEvent extends ListenerAdapter {
    Database database = new Database();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("select:deny")) return;
        Guild guild = event.getGuild();

        // récup raidId & memberId
        try {
            List<Database.ParticipationRequest> ParticipationQuery = database.selectAllParticipationsRequest(event.getChannel().getId(), event.getMessageId());
            Database.ParticipationRequest participations = ParticipationQuery.get(0);


            String idLeader = participations.leaderId;
            String idParticipant = participations.memberId;
            String idSelectionChannel = participations.selectionChannelId;
            String idRaid = participations.raidId;

            String reqSql = "DELETE FROM participationRequest WHERE raidId = ? AND memberId = ?";
            database.deleteParticipationRequest(reqSql, idRaid, participations.memberId);

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("You have been denied to join the raid : " + participations.raidName);
            eb.setDescription("Raid by : <@" + participations.leaderId + "> \n"
                    + "Feel free to send a DM to the leader and ask the reason and advices to get into more raids ! No hard feelings please :)");
            eb.setFooter("If you encouter any issue with this bot, please contact Zaack#1160");
            eb.setColor(Color.RED);


            guild.getJDA().retrieveUserById(idParticipant).queue(replyUser -> {
                replyUser.openPrivateChannel().queue(replyUserDm -> {
                    event.getMessage().delete().queue();
                    replyUserDm.sendMessageEmbeds(eb.build()).submit()
                            .whenComplete((s, error) -> {
                                if (error != null) {
                                    event.reply(replyUser.getAsMention() + " has been denied, but I couldn't warn him in DM, they're not open ! (You don't have anything to do about that unless he contacts you to ask about his registration, please let him know to open his DMs for the next time !").setEphemeral(true).queue();
                                } else {
                                    event.reply(replyUser.getAsMention() + " has been denied to join the raid").setEphemeral(true).queue();
                                }

                            });
                });
            });



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
