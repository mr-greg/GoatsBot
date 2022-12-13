package me.goats.misc;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinRoleAddEvent extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        System.out.println(event.getMember().getEffectiveName() + " joined the server");
        Role role = event.getGuild().getRolesByName("Raider", true).get(0);
        Role roleSepOne = event.getGuild().getRolesByName("---------------------------------------------", true).get(0);
        Role roleSepTwo = event.getGuild().getRolesByName("----------------------------------------------", true).get(0);
        event.getGuild().addRoleToMember(event.getMember(), role).queue();
        event.getGuild().addRoleToMember(event.getMember(), roleSepOne).queue();
        event.getGuild().addRoleToMember(event.getMember(), roleSepTwo).queue();
    }
}
