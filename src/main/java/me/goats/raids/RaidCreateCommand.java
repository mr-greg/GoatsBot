package me.goats.raids;

import io.github.cdimascio.dotenv.Dotenv;
import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;

public class RaidCreateCommand extends ListenerAdapter {
    Database database = new Database();
    Dotenv dotenv = Dotenv.load();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("raidcreateselect")) return;

        if (!event.isFromGuild()) {
            System.out.println("isFromGuild false");
            event.reply("This command only works on guilds").queue();
            return;
        }
        if (event.getGuild() == null) return;
        if (event.getMember() == null) return;

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            System.out.println("not admin perm");
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }

        String createtableraids = "CREATE TABLE IF NOT EXISTS raids (\n"
                + "id INTEGER PRIMARY KEY, \n"
                + "idLeader STRING, \n"
                + "idRole STRING, \n"
                + "idChannel STRING,\n"
                + "idretrievermsg STRING, \n"
                + "raidName STRING, \n"
                + "nbPlace STRING, \n"
                + "nbRaid STRING, \n"
                + "dateTime STRING, \n"
                + "aFournir STRING, \n"
                + "autre STRING, \n"
                + "idTeamChannel STRING, \n"
                + "idRegisterChannel STRING, \n"
                + "idSelectionChannel STRING"
                + ");";

        String createtablepartcipant = "CREATE TABLE IF NOT EXISTS participant (\n"
                + "id INTEGER PRIMARY KEY, \n"
                + "idRaid STRING, \n"
                + "idLeader STRING, \n"
                + "idParticipant STRING, \n"
                + "idSelectionChannel STRING"
                + ");";

        database.createTableRaids(dotenv.get("URLDB"), createtableraids );
        database.createTableParticipant(dotenv.get("URLDB"), createtablepartcipant);

        StringSelectMenu menu = StringSelectMenu.create("menu:raid")
                .setPlaceholder("Select which raid to plan")
                .setRequiredRange(0, 1)
                .addOption("Alzanor", "value:alzanor", "Alzanor !", Emoji.fromCustom("alzanor", 1036531966830460928L, false))
                .addOption("Valehir", "value:valehir", "Valehir !", Emoji.fromCustom("valehir", 1036531993967599618L, false))
                .addOption("Paimon Revenant", "value:paimonrev", "Paimon Revenant !", Emoji.fromCustom("paimonresu", 1036531990591184918L, false))
                .addOption("Paimon", "value:paimon", "Paimon !", Emoji.fromCustom("paimon", 1036531989521645638L, false))
                .addOption("Belial", "value:belial", "Belial !", Emoji.fromCustom("belial", 1036531971892985906L, false))
                .addOption("Kirollas", "value:kirollas", "Kirollas !", Emoji.fromCustom("kirollas", 1036531987051192390L, false))
                .addOption("Carno", "value:carno", "Carno !", Emoji.fromCustom("carno", 1036531973411307540L, false))
                .addOption("Fernon", "value:fernon", "Fernon !", Emoji.fromCustom("fernon", 1036531979803439134L, false))
                .addOption("Zenas", "value:zenas", "Zenas !", Emoji.fromCustom("zenas", 1036531996677111818L, false))
                .addOption("Erenia", "value:erenia", "Erenia !", Emoji.fromCustom("erenia", 1036531976003395665L, false))
                .addOption("Grenigas", "value:grenigas", "Grenigas !", Emoji.fromCustom("grenigas", 1036531982508752916L, false))
                .addOption("Valakus", "value:valakus", "Valakus !", Emoji.fromCustom("Valakus", 1036531992826753127L, false))
                .addOption("Kertos", "value:kertos", "Kertos !", Emoji.fromCustom("kertos", 1036531985344114741L, false))
                .addOption("Ibrahim", "value:ibrahim", "Ibrahim !", Emoji.fromCustom("ibrahim", 1036531983544750150L, false))
                .addOption("Glacerus/Draco", "value:glacerus", "Glacerus/Draco !", Emoji.fromCustom("glacerus", 1036531981455998996L, false))
                .addOption("Laurena", "value:laurena", "Laurena !", Emoji.fromCustom("laurena", 1036531988196237322L, false))
                .addOption("Yertirand", "value:yertirand", "Yertirand !", Emoji.fromCustom("yertirand", 1036531995255246870L, false))
                .addOption("Fafnir", "value:fafnir", "Fafnir !", Emoji.fromCustom("fafnir", 1036531978641608735L, false))
                .addOption("Event", "value:event", "an Event's raid !", Emoji.fromCustom("event", 1036531977144242276L, false))
                .addOption("Other Raid", "value:autre", "a non-listed raid here !", Emoji.fromCustom("autre", 1035894318197657601L, false))
                .build();

        MessageChannel salon = event.getMessageChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Welcome to the raid creation !");
        embed.setDescription(":flag_gb: Select the raid you wish to run. Once selected, you will be asked to fill in a form with the necessary information !");
        embed.addField("", ":flag_fr: Sélectionnez le raid que vous souhaitez lancer. Une fois sélectionnez, vous serez amené à remplir un formulaire avec les informations nécessaires ! Attention, le formulaire n'est disponible qu'en anglais", false);
        embed.addField("", ":flag_pl: Wybierz nalot, który chcesz uruchomić. Po wybraniu zostaniesz poproszony o wypełnienie formularza z niezbędnymi informacjami ! Proszę zwrócić uwagę, że formularz jest dostępny tylko w języku angielskim\n", false);
        embed.addField("", ":flag_es: ¡Seleccione el raid que desee empezar! ¡Una vez seleccionado, rellena el formulario con la información necesaria! Ten en cuenta que el formulario esta  disponible sólo en inglés.\n", false);
        embed.addField("", ":flag_de: Wählen Sie den Schlachtzug aus, den Sie starten möchten. Sobald Sie ausgewählt haben, werden Sie aufgefordert, ein Formular mit den erforderlichen Informationen auszufüllen ! Bitte beachten Sie, dass das Formular nur auf Englisch verfügbar ist.\n", false);
        embed.addField("", ":flag_ru: Выберите рейд, который вы хотите запустить. После выбора вам будет предложено заполнить форму с необходимой информацией ! Пожалуйста, обратите внимание, что форма доступна только на английском языке\n", false);
        embed.setColor(Color.white);

        salon.sendMessageEmbeds(embed.build()).addActionRow(menu).queue();

        event.reply("Message created !").setEphemeral(true).queue();
    }
}
