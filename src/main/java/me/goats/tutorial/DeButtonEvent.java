package me.goats.tutorial;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class DeButtonEvent extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("tutorial:de")) return;
        Guild guild = event.getGuild();
        System.out.println("de tutorial button pressed");

        String notifChannelMention = guild.getTextChannelById(dotenv.get("NOTIFCHANNELID")).getAsMention();
        String helpChannelMention = guild.getForumChannelById(dotenv.get("HELPCHANNELID")).getAsMention();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Dies ist die Deutsche Anleitung!");
        eb.setThumbnail("https://i.imgur.com/C5VbYAb.png");
        eb.setDescription("Zum Start können Sie unter dieser Nachricht eine Rolle mit Ihrer Klasse auswählen, dies ist jedoch nicht pflicht");
        eb.addField("Benennen Sie sich um", "Wir empfehlen Ihnen dringend, den Discord Namen mit Ihrem Namen und Level im Spiel umzubenennen (Goats+65)", false);
        eb.addField("Benachrichtigungen", "Unter " + notifChannelMention + "kannst du Raids auswählen, für die du benachrichtigt werden möchtest", false);
        eb.addField("Registrieren Sie sich für Raids", "Du kannst dich für Raids in der Kategorie \"Register to raid\" anmelden \n" +
                "Wenn ein Raidleiter rekrutiert, erscheint ein Kanal mit dem Raid und dem Namen des Anführers mit allen Informationen, die Sie benötigen !", false);
        eb.addField("Infos zur Anmeldung", "Bei der Registrierung werden Sie nach folgenden Informationen gefragt :\n" +
                "- Name im Spiel \n" +
                "- Klasse + Level\n" +
                "- SP, die Sie verwenden können\n" +
                "- Die Anzahl der Charaktere, die Sie mitbringen möchten (geben Sie 1 ein, wenn nur Sie alleine kommen)\n" +
                "- Alle anderen Informationen, die der Anführer wissen soll\n" +
                "Damit der Bot richtig funktioniert und eine Zeile leer gelassen wird, gebe einfach eine 0 ein (oder einen beliebigen Buchstaben oder eine Zahl)", false);
        eb.addField("Persönliche Informationen", "Aus datenschutz Gründen übermitteln Sie bitte keine privaten Informationen (oder Discord im Allgemeinen)", false);
        eb.addField("Einstieg in den Raid", "Sobald Sie das Formular abgeschickt haben, erhält der Leiter des Raids eine Benachrichtigung mit den Informationen, die Sie im Formular angegeben haben\n" +
                "Er entscheidet dann, ob er Ihren Antrag annimmt oder ablehnt", false);
        eb.addField("Anfrage angenommen", "Wenn Sie akzeptiert werden, erhalten Sie Zugang zum privaten Textkanal des Teams (zusaätzlich auch eine DM, um Sie daran zu erinnern, in welchen Raid Sie sind und wann der Raid beginnt !)", false);
        eb.addField("Anfrage abgelehnt", "Wenn Sie abgelehnt werden, erhalten Sie vom Bot eine DM, der Sie darüber informiert", false);
        eb.addField("Melde dich von einem Raid ab", "Wenn Sie sich von einem Raid abmelden müssen, können Sie dies im Registrierungskanal auf der Schaltfläche \"Abmelden\" tun. Der Bot informiert dann den Anführer und entfernt Ihren Zugang zum Raidkanal für Sie!\n" +
                "*(Möglicherweise nicht verfügbar, wenn die Registrierung bereits geschlossen ist, in diesem Fall wenden Sie sich bitte direkt an den Leiter!)*", false);
        eb.addField("Werden Sie ein Anführer", "Wenn Sie Ihre eigenen Raids auf diesem Server führen möchten, wenden Sie sich bitte an Zaack#1160", false);
        eb.addField("Selbstübersetzung", "Du kannst auf diese Nachricht mit einer der Flaggen flags :flag_gb: :flag_fr: :flag_pl: :flag_es: :flag_de: :flag_ru: reagieren. Dadurch erhältst du  eine Übersetzung in die jeweilige Sprache als DM !", false);
        eb.addField("Fragen?", "Sollten noch Fragen offen sein stelle diese in dem Kanal : " + helpChannelMention, false);

        StringSelectMenu menu = StringSelectMenu.create("menu:profile")
                .setPlaceholder("Select your class !")
                .setRequiredRange(0,5)
                .addOption("Archer", "value:archer", "I am an Archer", Emoji.fromCustom("arc", 1043081953567576124L, false))
                .addOption("Swordsman", "value:sword", "I am a Swordsman", Emoji.fromCustom("epee", 1043081956327440464L, false))
                .addOption("Mage", "value:mage", "I am a Mage", Emoji.fromCustom("baton", 1043081954796511272L, false))
                .addOption("Martial Artist", "value:ma", "I am a Martial Artist", Emoji.fromCustom("ma", 1043082939384205332L, false))
                .addOption("Adventurer", "value:adv", "I am an Adventurer for some reason", Emoji.fromCustom("adv", 1043082938155278356L, false))
                .build();

        event.replyEmbeds(eb.build()).addActionRow(menu).setEphemeral(true).queue();
    }
}
