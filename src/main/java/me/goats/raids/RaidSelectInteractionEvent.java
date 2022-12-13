package me.goats.raids;

import io.github.cdimascio.dotenv.Dotenv;
import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.awt.*;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RaidSelectInteractionEvent extends ListenerAdapter {
    Database database = new Database();
    Dotenv dotenv = Dotenv.load();

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponent().getId() == null) return;
        if (!event.getComponent().getId().equalsIgnoreCase("menu:raid")) return;
        Member member = event.getMember();
        Guild guild = event.getGuild();
        TextChannel salon = event.getChannel().asTextChannel();
        System.out.println(member.getEffectiveName() + "selected a raid to create");

        if (member == null) return;
        if (guild == null) return;

        Role leader = guild.getRolesByName("Leader", true).get(0);

        if (!member.getRoles().contains(leader)) {
            event.reply("You are not supposed to be here o_o (Error code #05)").queue();
            return;
        }

        if (event.getComponent().getId() == null) {
            System.out.println("SelectMenuInteraction - l'id du component est null");
            return;
        }

        try {
           event.getSelectedOptions().get(0).getValue();
        } catch (IndexOutOfBoundsException e){
            event.reply("You unselected the raid. If you tried to start the same raid as last time, please try again ! (Error code #07)").setEphemeral(true).queue();
            return;
        }

        String raidSelectionne = event.getSelectedOptions().get(0).getLabel();



        String idLeader = member.getId();

        TextInput dateHeure = TextInput.create("heure:input", "Time and date of the raid ?", TextInputStyle.SHORT)
                .setMinLength(5)
                .setMaxLength(30)
                .setRequired(true)
                .setPlaceholder("Please precise your local time (ex: GMT+2)")
                .build();
        TextInput nbRaid = TextInput.create("nombreRaid:input", "How many raids ?", TextInputStyle.SHORT)
                .setMinLength(1)
                .setMaxLength(10)
                .setRequired(true)
                .setPlaceholder("Type the number of raids, or the time (like 1H30)")
                .build();

        TextInput nbPlace = TextInput.create("nombrePlace:input", "How many spots you have available ?", TextInputStyle.SHORT)
                .setMinLength(1)
                .setMaxLength(15)
                .setRequired(true)
                .setPlaceholder("Just type the number of spots available (count yourself !!)")
                .build();

        TextInput fournir = TextInput.create("fournir:input", "Items to provide (like 7/7 for example)", TextInputStyle.SHORT)
                .setMaxLength(60)
                .setMinLength(1)
                .setRequired(false)
                .setPlaceholder("If empty, just type 0")
                .build();

        TextInput autre = TextInput.create("autre:input", "If you accept alts, minimum level, stuff...", TextInputStyle.SHORT)
                .setMaxLength(100)
                .setRequired(true)
                .setPlaceholder("Alts not welcome ! (for exemple), if empty, type 0")
                .build();


        Modal modal = Modal.create("creationModal", "You selected the raid : " + raidSelectionne)
                .addActionRows(ActionRow.of(dateHeure), ActionRow.of(nbRaid), ActionRow.of(nbPlace), ActionRow.of(fournir), ActionRow.of(autre))
                .build();


        String createtabledataselect = "CREATE TABLE IF NOT EXISTS dataSelect (\n"
                + "idLeader STRING, \n"
                + "idretrievermsg STRING, \n"
                + "raidName STRING"
                + ");";

        database.createTableDataSelect(dotenv.get("URLDB"), createtabledataselect);


        //
        TextChannel dev = guild.getTextChannelById("1037268617118625832");
        if (dev == null) {
            event.reply("dev channel is missing, please contact Zaack#1160 with error code #07").queue();
            return;
        }

        event.replyModal(modal).queue();


        dev.sendMessage(member.getAsMention() + " a sélectionné le raid " + raidSelectionne).queue(reply -> {


            // Active ceci si soucis au niveau de la table dataSelect !
            /*
            String raidNonNull = database.raidNameFromDataSelect(idLeader);
            if (raidNonNull != null) {
                event.reply("You need to wait 5 minutes between each selection ! If this error occurs, please contact Zaack#1160 with Error code #DB03").queue();
                return;
            }
            */


            String sql = "INSERT INTO dataSelect(idLeader, idretrievermsg, raidName) VALUES(?,?,?)";

            String idRetrieverMsg = reply.getId();

            database.updateSelectData(sql, idLeader, idRetrieverMsg, raidSelectionne);

            // Active aussi ça !
            /*
            StringSelectMenu disabled = event.getComponent().asDisabled();
            StringSelectMenu enabled = event.getComponent().asEnabled();

            String sqlDelete = "DELETE from dataSelect where IdLeader = ?";

            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            Runnable task = new Runnable() {
                public void run() {
                    database.deleteDataSelect(sqlDelete, idLeader);
                }
            };
            int delay = 5;
            scheduler.schedule(task,delay, TimeUnit.MINUTES);
            scheduler.shutdown();

             */

            String leaderName = event.getMember().getEffectiveName();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(leaderName + " is planning a raid, please wait...");
            eb.setDescription("The selection will be available 5 minutes after he selected a raid, or once he completed the creation ! :)");
            eb.addField("Selection available : ", String.valueOf(TimeFormat.RELATIVE.now().plus(Duration.ofMinutes(5))), false);
            eb.setFooter("If this message doesn't dissapear at the end of the countdown and/or the selection is still not available, please contact Zaack#1160 (Error code #11)");
            eb.setColor(Color.RED);
        });
    }
}
