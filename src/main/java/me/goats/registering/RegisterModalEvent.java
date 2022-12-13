package me.goats.registering;

import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RegisterModalEvent extends ListenerAdapter {
    Database database = new Database();
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getChannel().asTextChannel().getParentCategoryId().equals("1038005703408877599")) {
            //System.out.println("Pas le modal register");
            return;
        }
        System.out.println("register modal complété");

        Guild guild = event.getGuild();
        String memberId = event.getMember().getId();

        try {
            List<Database.Raids> raidsQuery = database.selectAllRaids(event.getModalId());

            Database.Raids raidsInfo = raidsQuery.get(0);
            //System.out.println(raidsInfo.raidName);
            //System.out.println(raidsInfo.id);

            String pseudo = event.getValue("register:pseudo").getAsString();
            String classeNiveau = event.getValue("register:classLevel").getAsString();
            String sp = event.getValue("register:sp").getAsString();
            String mule = event.getValue("register:mule").getAsString();
            String autre = event.getValue("register:autre").getAsString();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("New register for raid : " + String.valueOf(raidsInfo.raidName));
            eb.setColor(Color.GREEN);
            eb.setDescription("Name : " + pseudo + "\n"
                            + "Class & Level : " + classeNiveau + "\n"
                            + "SP Available : " + sp + "\n"
                            + "Alts (number of chars) : " + mule + "\n"
                            + "Other infos : " + autre + "\n"
                            + "Discord profile : <@" + memberId + ">"
            );
            eb.setFooter("If you need help, go to the leader help channel or contact Zaack#1160 !");

            //String channelSelectId = guild.getTextChannelById(raidsInfo.idSelectionChannel.toString());
            TextChannel channelSelect = guild.getJDA().getTextChannelById(raidsInfo.idSelectionChannel);

            Button accept = Button.success("select:accept", "Accept");
            Button deny = Button.danger("select:deny", "Deny");

            channelSelect.sendMessage("<@" + raidsInfo.idLeader + ">").setEmbeds(eb.build())
                    .addActionRow(accept, deny)
                    .queue(reply -> {

                        String sql = "INSERT INTO participationRequest(raidId,raidName,leaderId,pseudo,classeNiveau,sp,mule,autre,selectionChannelId, msgAcceptId, memberId) VALUES(?,?,?,?,?,?,?,?,?,?, ?)";

                        String raidId = raidsInfo.id;
                        String raidName = raidsInfo.raidName;
                        String idLeader = raidsInfo.idLeader;
                        String selectionChannelId = raidsInfo.idSelectionChannel;
                        String msgAcceptId = reply.getId();


                        database.updateTableParticipationRequest(sql, raidId, raidName, idLeader, pseudo, classeNiveau, sp, mule, autre, selectionChannelId,msgAcceptId, memberId);

                        event.reply("Your request has been successfully registered. If you're accepted, you will get a notification and access to the team's private channel confirming your participation to the raid !\n" +
                                "if you're not accepted, you should receive a private message from this bot telling you so, please make sure to open your DMs on the server (right click on the server's icon on the left -> privacy settings -> allow direct messages and message requests !").setEphemeral(true).queue();
                    });

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            event.reply("An error has occured while trying to register you, please contact Zaack#1160 (Error code #09)").setEphemeral(true).queue();
        }


    }


}
