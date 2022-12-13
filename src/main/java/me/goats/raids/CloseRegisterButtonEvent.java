package me.goats.raids;

import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class CloseRegisterButtonEvent extends ListenerAdapter {
    Database database = new Database();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("idcloseregister")) return;
        Guild guild = event.getGuild();

        System.out.println("entering closeRegisterButtonEvent");
        List<Database.Raids> RaidsQuery = null;
        try {
            RaidsQuery = database.selectAllRaidsById(event.getChannel().getId());
            Database.Raids raids = RaidsQuery.get(0);

            List<Database.VerifyParticipationRequest> participationRequests = database.ParticipationRequestCheck(raids.idSelectionChannel);

            for (Database.VerifyParticipationRequest requester : participationRequests) {
                guild.getJDA().retrieveUserById(requester.memberId).queue(reply -> {
                    reply.openPrivateChannel().queue(replyUserDm -> {
                                EmbedBuilder eb = new EmbedBuilder();
                                eb.setTitle("Registrations for raid " + raids.raidName + " are now closed !");
                                eb.setDescription("Raid by : <@" + raids.idLeader + "> \n"
                                        + "Date and Time : " + raids.dateTime + "\n"
                                        + "This raid has closed its registrations, feel free to register to another raid !");
                                eb.setFooter("If you encouter any issue with this bot, please contact Zaack#1160");
                                eb.setColor(Color.RED);

                                replyUserDm.sendMessageEmbeds(eb.build()).queue();
                            });
                });
            }

            String deletePartReqSql = "DELETE FROM participationRequest WHERE raidId = ?";
            database.deleteRaidFromPartReq(deletePartReqSql, raids.id);

            guild.getJDA().getTextChannelById(raids.idTeamChannel).sendMessage("Registrations for this raid are now closed ! Good luck !").queue();

            Button raidTermine = Button.success("idcompleteraid", "Raid completed");
            Button annulerRaidButton = Button.danger("idcancelraid", "Cancel Raid");


            guild.getTextChannelById(raids.idSelectionChannel).createCopy().queue(reply -> {
                String newChannelId = reply.getId();
                guild.getTextChannelById(raids.idSelectionChannel).delete().queue();

                String sql = "UPDATE raids SET idSelectionChannel = ? WHERE id = ?";
                database.updateRaidsSelectionChannel(sql,newChannelId, raids.id);

                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Registrations closed !");
                eb.setDescription("Please don't forget to cancel or complete the raid in order to delete it once you're done !");
                eb.setColor(Color.WHITE);

                reply.sendMessage("<@" + raids.idLeader + ">").setEmbeds(eb.build()).addActionRow(raidTermine, annulerRaidButton).queue();
            });


            guild.getJDA().getTextChannelById(raids.idRegisterChannel).delete().queue();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
