package me.goats.roleProfile;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;

public class SelectRoleProfileCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;

        if (!event.getName().equals("selectroleprofile")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }

        MessageChannel salon = event.getMessageChannel();

        StringSelectMenu menu = StringSelectMenu.create("menu:profile")
                .setPlaceholder("Select your class !")
                .setRequiredRange(0,5)
                .addOption("Archer", "value:archer", "I am an Archer", Emoji.fromCustom("arc", 1043081953567576124L, false))
                .addOption("Swordsman", "value:sword", "I am a Swordsman", Emoji.fromCustom("epee", 1043081956327440464L, false))
                .addOption("Mage", "value:mage", "I am a Mage", Emoji.fromCustom("baton", 1043081954796511272L, false))
                .addOption("Martial Artist", "value:ma", "I am a Martial Artist", Emoji.fromCustom("ma", 1043082939384205332L, false))
                .addOption("Adventurer", "value:adv", "I am an Adventurer for some reason", Emoji.fromCustom("adv", 1043082938155278356L, false))
                .build();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Please select your class !");
        eb.setDescription("You can select your class here to let people know what you're playin' !\n *Totaly up to you, not an obligation ;)*");
        eb.setColor(Color.WHITE);

        salon.sendMessageEmbeds(eb.build()).addActionRow(menu).queue(reply -> {
            event.reply("Message created !").setEphemeral(true).queue();
        });
    }
}
