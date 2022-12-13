package me.goats.registering;

import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UnregisterButtonEvent extends ListenerAdapter {
    Database database = new Database();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("idbuttonunregister")) return;
        System.out.println(event.getMember().getEffectiveName() + "unregister button pressed");

        Guild guild = event.getGuild();
        String memberId = event.getMember().getId();

        List<Database.Raids> raidsQuery = null;
        try {
            raidsQuery = database.selectAllRaids(event.getMessageId());
            Database.Raids raidsInfo = raidsQuery.get(0);

            try {
                List<Database.VerifyParticipationRequest> participationRequests = database.ParticipationRequestCheck(raidsInfo.idSelectionChannel);

                for (Database.VerifyParticipationRequest idCheck : participationRequests) {
                    if (idCheck.memberId.equals(event.getMember().getId())){
                        // IL EST DANS LES REQUESTS

                        String msgAcceptId = database.SelectMsgAcceptIdFromPartReq(raidsInfo.id, memberId);
                        TextChannel salon = guild.getTextChannelById(raidsInfo.idSelectionChannel);

                        salon.retrieveMessageById(msgAcceptId).queue(msgDel -> {
                            msgDel.delete().queue();
                        });
                        salon.sendMessage("<@" + memberId + "> has canceled his registration !").queue();

                        String reqSql = "DELETE FROM participationRequest WHERE raidId = ? AND memberId = ?";
                        database.deleteParticipationRequest(reqSql, raidsInfo.id, memberId);
                    }
                }

                try {
                    List<Database.Participants> ParticipantsQuery = database.selectAllParticipants(raidsInfo.id);
                    for (Database.Participants participantCheck : ParticipantsQuery) {
                        if (participantCheck.idParticipant.equals(event.getMember().getId())){
                            // IL EST DANS LES PARTICIPANTS
                            TextChannel select = guild.getTextChannelById(raidsInfo.idSelectionChannel);
                            TextChannel team = guild.getTextChannelById(raidsInfo.idTeamChannel);

                            select.sendMessage("<@" + raidsInfo.idLeader + "> carefull, <@" + memberId + "> has left the team !").queue();
                            team.sendMessage("<@" + memberId + "> left us, what a shame :(").queue();

                            Role role = guild.getRoleById(raidsInfo.idRole);
                            guild.removeRoleFromMember(event.getUser(), role).queue();

                            String reqSQL = "DELETE FROM participant WHERE idRaid = ? AND idParticipant = ?";
                            database.deleteParticipant(reqSQL, raidsInfo.id, memberId);

                        }
                    }

                } catch (SQLException e){
                    return;
                }
                event.reply("You were successfully removed from the team").setEphemeral(true).queue();


                StringBuilder sb = new StringBuilder();

                List<Database.Participants> ParticipantsQuery2 = database.selectAllParticipants(raidsInfo.id);
                boolean emptyTeam = false;
                for (Database.Participants ids : ParticipantsQuery2) {
                    //System.out.println("tour de boucle");
                    String idPart = ids.idParticipant;
                    sb.append("<@").append(idPart).append("> \n");
                    if (idPart.isEmpty()){
                        emptyTeam = true;
                    }
                    //System.out.println(idPart);
                }

                if (ParticipantsQuery2.isEmpty()){
                    emptyTeam = true;
                }


                Long idEmoji = null;
                String linkThumbnail = null;
                switch (raidsInfo.raidName.toLowerCase()) {

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

                EmbedBuilder editedEmbed = new EmbedBuilder();
                editedEmbed.setTitle("Raid : " + raidsInfo.raidName);
                editedEmbed.setColor(Color.WHITE);
                editedEmbed.setThumbnail(linkThumbnail);
                editedEmbed.setDescription(
                        "Hosted by : <@" + raidsInfo.idLeader + ">\n"
                                + "Time and date : " + raidsInfo.dateTime + "\n"
                                + "Number of raids : " + raidsInfo.nbRaid + "\n"
                                + "Need to provide : " + raidsInfo.aFournir + "\n"
                                + "Spots : " + raidsInfo.nbPlace
                );

                editedEmbed.addField("Other informations : ", raidsInfo.autre, false);
                if (!emptyTeam) {
                    editedEmbed.addField("Members :", "" + sb, false);
                    //System.out.println("empty team = false");
                }
                editedEmbed.setFooter("The number of spots available isn't updated on this message, the Leader has to do it ! If you need any help, please ask in help channels or contact Zaack#1160");

                TextChannel registerChannel = guild.getTextChannelById(raidsInfo.idRegisterChannel);
                //récupérer l'id de l'embed avec les infos ? si pas possible, le faire passer en DB
                MessageHistory registerHistory = registerChannel.getHistoryFromBeginning(1).complete();
                String embedRegisterId = registerHistory.getRetrievedHistory().get(0).getId();

                registerChannel.editMessageEmbedsById(embedRegisterId, editedEmbed.build()).queue();

            } catch (IndexOutOfBoundsException e) {
                System.out.println("Soucis au niveau du unregisterButton, IndexOutOfBounds sûrement");
                System.out.println(e.getMessage());
            }




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




    }
}
