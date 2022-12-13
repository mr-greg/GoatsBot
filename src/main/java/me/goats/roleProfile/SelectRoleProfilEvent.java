package me.goats.roleProfile;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.ArrayList;
import java.util.List;

public class SelectRoleProfilEvent extends ListenerAdapter {
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponent().getId() == null) return;
        if (!event.getComponent().getId().equalsIgnoreCase("menu:profile")) return;
        Member member = event.getMember();
        Guild guild = event.getGuild();
        System.out.println("role profile selected");

        if (member == null) return;
        if (guild == null) return;

        List<SelectOption> liste = event.getSelectedOptions();

        List<Role> allRole = new ArrayList<>();
        allRole.add(event.getJDA().getRolesByName("Archer", true).get(0));
        allRole.add(event.getJDA().getRolesByName("Sword", true).get(0));
        allRole.add(event.getJDA().getRolesByName("Mage", true).get(0));
        allRole.add(event.getJDA().getRolesByName("Martial Artist", true).get(0));
        allRole.add(event.getJDA().getRolesByName("Adventurer", true).get(0));

        int b = 0;
        while (b < allRole.size()) {
            guild.removeRoleFromMember(member, allRole.get(b)).queue();
            b++;
        }

        if (event.getSelectedOptions().isEmpty()) {
            event.reply("You don't have any class selected anymore ! \nPlease double check if the role(s) got correctly removed, if not, ask in the help channel ! *(they can take up to 5 minutes to be removed)*").setEphemeral(true).queue();
            return;
        }

        ArrayList<String> rolesSelect = new ArrayList<>();

        int i = 0;
        while (i < liste.size()) {
            SelectOption option = liste.get(i);

            switch (option.getValue()){
                case "value:archer" -> {
                    guild.addRoleToMember(member,allRole.get(0)).queue();
                    rolesSelect.add(allRole.get(0).getAsMention());
                }
                case "value:sword" -> {
                    guild.addRoleToMember(member,allRole.get(1)).queue();
                    rolesSelect.add(allRole.get(1).getAsMention());
                }
                case "value:mage" -> {
                    guild.addRoleToMember(member,allRole.get(2)).queue();
                    rolesSelect.add(allRole.get(2).getAsMention());
                }
                case "value:ma" -> {
                    guild.addRoleToMember(member,allRole.get(3)).queue();
                    rolesSelect.add(allRole.get(3).getAsMention());
                }
                case "value:adv" -> {
                    guild.addRoleToMember(member,allRole.get(4)).queue();
                    rolesSelect.add(allRole.get(4).getAsMention());
                }
            }
            i++;
        }
        String string = "";
        for (String s : rolesSelect) {
            if (!string.isBlank()) { string += ", "; }
            string += s;
        }

        event.reply("You now have the following class roles : " + string + "\nPlease verify that the roles have correctly been added to you, if not, please ask in the help channel ! *(they can take up to 5 minutes to be added)*").setEphemeral(true).queue();
    }
}
