package me.goats.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class PatchNoteCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("patchnote")) return;

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
        TextChannel tutorial = event.getGuild().getTextChannelById(1043084557261475971L);
        TextChannel raidsAct = event.getGuild().getTextChannelById(1044440542320267284L);
        TextChannel percentAct = event.getGuild().getTextChannelById(1044443156600594512L);
        TextChannel notifications = event.getGuild().getTextChannelById(1037261346414280734L);


        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Patch note from 22/11/22");
        eb.setColor(Color.PINK);
        eb.addField("Roles", "There is now separator roles (--------) to get a clearer view of member's roles, you will easily see if they are mage, which raid they're interested in or participating to", false);
        eb.addField("Ice Crown", "The act4 category is now available again, had to reconnect everything because of the maintenance", false);
        eb.addField("Auto-translation", "You can now react to a message with one of the following emoji : :flag_gb: :flag_fr: :flag_pl: :flag_es: :flag_de: :flag_ru: you will then receive the translated message in DM ! (you can try by reacting to this message for example !)", false);
        eb.setFooter("If you have any suggestion, please do not hesitate !");

        event.getChannel().sendMessage(event.getGuild().getRolesByName("raider", true).get(0).getAsMention()).queue();
        event.getChannel().sendMessageEmbeds(eb.build()).queue();
        event.reply("Done").setEphemeral(true).queue();
    }
}
