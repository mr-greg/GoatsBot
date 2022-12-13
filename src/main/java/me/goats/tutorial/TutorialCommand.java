package me.goats.tutorial;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class TutorialCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("setuptutorial")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            System.out.println("not admin perm");
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }

        TextChannel channel = event.getChannel().asTextChannel();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Welcome on the server !");
        eb.setDescription(":flag_gb: Click on a button down below to get a quick tutorial in the selected language !");
        eb.addField("", ":flag_fr: Cliquez sur un bouton ci-dessous afin de voir le tutoriel dans la langue sélectionnée !", false);
        eb.addField("",":flag_pl: Kliknij na przycisk poniżej, aby otrzymać szybki tutorial w wybranym języku !", false);
        eb.addField("",":flag_es: ¡ Haga clic en un botón de abajo para obtener un breve tutorial en el idioma elegido !", false);
        eb.addField("",":flag_de: Klicken Sie unten auf eine Schaltfläche, um eine kurze Anleitung in der gewählten Sprache zu bekommen !", false);
        eb.addField("",":flag_ru: Нажмите иа кнопку под текстом чтобы получить информацию про использование на выбранном языке !", false);
        eb.setColor(Color.WHITE);

        Button eng = Button.secondary("tutorial:eng", Emoji.fromUnicode("\uD83C\uDDEC\uD83C\uDDE7"));
        Button fr = Button.secondary("tutorial:fr", Emoji.fromUnicode("\uD83C\uDDEB\uD83C\uDDF7"));
        Button pl = Button.secondary("tutorial:pl", Emoji.fromUnicode("\uD83C\uDDF5\uD83C\uDDF1"));
        Button es = Button.secondary("tutorial:es", Emoji.fromUnicode("\uD83C\uDDEA\uD83C\uDDF8"));
        Button de = Button.secondary("tutorial:de", Emoji.fromUnicode("\uD83C\uDDE9\uD83C\uDDEA"));
        Button ru = Button.secondary("tutorial:ru", Emoji.fromUnicode("\uD83C\uDDF7\uD83C\uDDFA"));

        channel.sendMessageEmbeds(eb.build()).addActionRow(eng, fr, pl, es, de).addActionRow(ru).queue();
        event.reply("Done !").setEphemeral(true).queue();

    }
}
