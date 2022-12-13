package me.goats.misc;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DisableSelectRaidCommand extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("disableraidcreate")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            System.out.println("not admin perm");
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }

        Guild guild = event.getGuild();
        TextChannel salon = guild.getTextChannelById(Long.parseLong(dotenv.get("SALONSELECTID")));


        salon.retrieveMessageById(Long.parseLong(dotenv.get("SELECTCREATEMSGID"))).queue(message -> {
            message.editMessageComponents(message.getActionRows().get(0).asDisabled()).queue();
        });

        event.reply("Selection menu disabled !").setEphemeral(true).queue();
    }
}
