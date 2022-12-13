package me.goats.registering;

import me.goats.utils.Database;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.sql.SQLException;
import java.util.List;

public class RegisterButtonEvent extends ListenerAdapter {
    Database database = new Database();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("idbuttonregister")) return;
        System.out.println(event.getMember().getEffectiveName() + "register button pressed");

        try {
            String reqSQL = "CREATE TABLE IF NOT EXISTS participationRequest (\n"
                    + "raidId STRING, \n"
                    + "raidName STRING, \n"
                    + "leaderId STRING, \n"
                    + "pseudo STRING, \n"
                    + "classeNiveau STRING, \n"
                    + "sp STRING, \n"
                    + "mule STRING, \n"
                    + "autre STRING, \n"
                    + "selectionChannelId STRING, \n"
                    + "msgAcceptId, \n"
                    + "memberId STRING"
                    + ");";


            new Thread(new Runnable() {
                @Override
                public void run() {
                    database.createTableParticipationRequest(reqSQL);
                }
            }).start();

            List<Database.Raids> raidsQuery = database.selectAllRaids(event.getMessageId());


            Database.Raids raidsInfo = raidsQuery.get(0);

            try {
                List<Database.VerifyParticipationRequest> participationRequests = database.ParticipationRequestCheck(raidsInfo.idSelectionChannel);
                Database.VerifyParticipationRequest participationsReq = participationRequests.get(0);

                for (Database.VerifyParticipationRequest idCheck : participationRequests) {
                    if (idCheck.memberId.equals(event.getMember().getId())){
                        event.reply("You already tried to register to this raid, please contact the leader if necessary").setEphemeral(true).queue();
                        return;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.println("pas encore inscrit");
            }

            String raidName = raidsInfo.raidName;

            TextInput pseudo = TextInput.create("register:pseudo", "In-Game Name", TextInputStyle.SHORT)
                    .setMinLength(4)
                    .setMaxLength(20)
                    .setRequired(true)
                    .build();
            TextInput classeNiveau = TextInput.create("register:classLevel", "Class + Level", TextInputStyle.SHORT)
                    .setMinLength(4)
                    .setMaxLength(32)
                    .setPlaceholder("Archer +65")
                    .setRequired(true)
                    .build();
            TextInput sp = TextInput.create("register:sp", "Which SP can you use ?", TextInputStyle.SHORT)
                    .setMinLength(1)
                    .setMaxLength(32)
                    .setPlaceholder("Destroy, WK and DH for example")
                    .setRequired(true)
                    .build();
            TextInput mule = TextInput.create("register:mule", "How many chars will u bring ?", TextInputStyle.SHORT)
                    .setMinLength(1)
                    .setMaxLength(32)
                    .setPlaceholder("1+2 for example (just type 1 if no alts)")
                    .setRequired(true)
                    .build();
            TextInput autre = TextInput.create("register:autre", "Other relevant informations", TextInputStyle.SHORT)
                    .setMinLength(1)
                    .setMaxLength(70)
                    .setPlaceholder("(Type 0 if empty) Have alt wk buff, other info...")
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create(raidsInfo.idretrivermsg, "Registering for " + raidName)
                    .addActionRows(ActionRow.of(pseudo), ActionRow.of(classeNiveau), ActionRow.of(sp), ActionRow.of(mule), ActionRow.of(autre))
                    .build();

            event.replyModal(modal).queue();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
