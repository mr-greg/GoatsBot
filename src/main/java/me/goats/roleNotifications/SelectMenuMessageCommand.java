package me.goats.roleNotifications;

import me.goats.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;

public class SelectMenuMessageCommand extends ListenerAdapter {

    Database database = new Database();
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;

        if (!event.getName().equals("selectrolemenu")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }

        Integer setupRoleState = database.checkSetupRole("SELECT roleSetup FROM guilds");


        // check if déjà fait (1 = déjà fait, null pas encore fait)
        if (setupRoleState == null) {
            event.reply("The roles have not been setup yet, please contact Zaack#1160 with the error code if you have an issue (error code #02)").setEphemeral(true).queue();
            return;
        }

        // recup le salon depuis lequel la commande a été envoyé puis mettre le msg dedans (pas de reply)

        StringSelectMenu menu = StringSelectMenu.create("menu:role")
                .setPlaceholder("Select your raids notifications")
                .setRequiredRange(0, 21)
                // add options archer sword mage ici
                .addOption("Alzanor", "value:alzanor", "Ping me when Alzanor raid is planned !", Emoji.fromCustom("alzanor", 1036531966830460928L, false))
                .addOption("Valehir", "value:valehir", "Ping me when Valehir raid is planned !", Emoji.fromCustom("valehir", 1036531993967599618L, false))
                .addOption("Paimon Revenant", "value:paimonrev", "Ping me when Paimon Revenant raid is planned !", Emoji.fromCustom("paimonresu", 1036531990591184918L, false))
                .addOption("Paimon", "value:paimon", "Ping me when Paimon raid is planned !", Emoji.fromCustom("paimon", 1036531989521645638L, false))
                .addOption("Belial", "value:belial", "Ping me when Belial raid is planned !", Emoji.fromCustom("belial", 1036531971892985906L, false))
                .addOption("Kirollas", "value:kirollas", "Ping me when Kirollas raid is planned !", Emoji.fromCustom("kirollas", 1036531987051192390L, false))
                .addOption("Carno", "value:carno", "Ping me when Carno raid is planned !", Emoji.fromCustom("carno", 1036531973411307540L, false))
                .addOption("Fernon", "value:fernon", "Ping me when Fernon raid is planned !", Emoji.fromCustom("fernon", 1036531979803439134L, false))
                .addOption("Zenas", "value:zenas", "Ping me when Zenas raid is planned !", Emoji.fromCustom("zenas", 1036531996677111818L, false))
                .addOption("Erenia", "value:erenia", "Ping me when Erenia raid is planned !", Emoji.fromCustom("erenia", 1036531976003395665L, false))
                .addOption("Grenigas", "value:grenigas", "Ping me when Grenigas raid is planned !", Emoji.fromCustom("grenigas", 1036531982508752916L, false))
                .addOption("Valakus", "value:valakus", "Ping me when Valakus raid is planned !", Emoji.fromCustom("Valakus", 1036531992826753127L, false))
                .addOption("Kertos", "value:kertos", "Ping me when Kertos raid is planned !", Emoji.fromCustom("kertos", 1036531985344114741L, false))
                .addOption("Ibrahim", "value:ibrahim", "Ping me when Ibrahim raid is planned !", Emoji.fromCustom("ibrahim", 1036531983544750150L, false))
                .addOption("Glacerus - Draco", "value:glacerus", "Ping me when Glacerus/Draco raid is planned !", Emoji.fromCustom("glacerus", 1036531981455998996L, false))
                .addOption("Laurena", "value:laurena", "Ping me when Laurena raid is planned !", Emoji.fromCustom("laurena", 1036531988196237322L, false))
                .addOption("Yertirand", "value:yertirand", "Ping me when Yertirand raid is planned !", Emoji.fromCustom("yertirand", 1036531995255246870L, false))
                .addOption("Fafnir", "value:fafnir", "Ping me when Fafnir raid is planned !", Emoji.fromCustom("fafnir", 1036531978641608735L, false))
                .addOption("Event", "value:event", "Ping me when an Event raid is planned !", Emoji.fromCustom("event", 1036531977144242276L, false))
                .addOption("Other Raids", "value:autre", "Ping me when a non-listed here raid is planned !", Emoji.fromCustom("autre", 1035894318197657601L, false))
                .addOption("Daily Raids", "value:daily", "Allow leader to ping me when doing daily raids !", Emoji.fromCustom("raid", 1036531992054992906L, false))
                .addOption("Angel act4 Raids", "value:angel", "Get a notification when an Angel raid opens !", Emoji.fromCustom("ange", 1036531968113901609L, false))
                .addOption("Demon act4 Raids", "value:demon", "Get a notification when a Demon raid opens !", Emoji.fromCustom("demon", 1036531974791237672L, false))
                .build();

        MessageChannel salon = event.getMessageChannel();

        EmbedBuilder test = new EmbedBuilder();
        test.setTitle("Welcome to the role selection !");
        test.setDescription(":flag_gb: Open the menu below this message and select raids in order to get notified when registering is available !");
        test.addField("", ":flag_fr: Ouvre le menu ci-dessous et sélectionne les raids à te notifier lorsque les inscriptions seront ouvertes !", false);
        test.addField("", ":flag_pl: Otwórz menu pod tą wiadomością i wybierz Raids, aby zostać poinformowanym o dostępności wpisów !", false);
        test.addField("", ":flag_es: ¡ Abre el menú debajo de este mensaje y seleccione los raids para recibir una notificación cuando el registro esté disponible !", false);
        test.addField("", ":flag_de: Öffne das Menü unter dieser Nachricht und wähle die Raids aus, um benachrichtigt zu werden, wenn du dich registrieren kannst !", false);
        test.addField("", ":flag_ru: откройте меню и выберите рейды, для которых вы хотите получать уведомления когда регистрация открыта !", false);
        test.setColor(Color.white);

        salon.sendMessageEmbeds(test.build()).addActionRow(menu).queue();

        event.reply("Message created !").setEphemeral(true).queue();



    }
}
