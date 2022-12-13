package me.goats.misc;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class KickAllCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("kickall")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            System.out.println("not admin perm");
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }
        Guild guild = event.getGuild();
        Role mod = guild.getRoleById(1045142277410013234L);
        guild.loadMembers().onSuccess(success -> {
            for (Member member : success) {
                if (!member.getRoles().contains(mod)) {
                    member.kick().queue();
                    System.out.println(member.getEffectiveName() + " has been kicked");
                }
            }
        });
    }
}