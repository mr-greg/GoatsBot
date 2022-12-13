package me.goats.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AddRoleCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("addroles")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            System.out.println("not admin perm");
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }
        Guild guild = event.getGuild();
        Role roleSepOne = guild.getRoleById(1044754279086428290L);
        Role roleSepTwo = guild.getRoleById(1044755157373681684L);
        guild.loadMembers().onSuccess(success -> {
            for (Member member : success) {
                guild.addRoleToMember(member,roleSepOne).queue();
                guild.addRoleToMember(member,roleSepTwo).queue();
            }
        });

        event.reply("done").setEphemeral(true).queue();
    }
}
