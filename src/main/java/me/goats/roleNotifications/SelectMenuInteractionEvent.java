package me.goats.roleNotifications;

import me.goats.utils.Database;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.ArrayList;
import java.util.List;

public class SelectMenuInteractionEvent extends ListenerAdapter {

    Database database = new Database();

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        if (event.getComponent().getId() == null) return;
        if (!event.getComponent().getId().equalsIgnoreCase("menu:role")) return;
        System.out.println("role notif select");

        List<SelectOption> liste = event.getSelectedOptions();

        Member member = event.getMember();
        Guild guild = event.getGuild();

        ForumChannel helpChannel = guild.getForumChannelById(1044090932137578506L);

        if (member == null) return;
        if (guild == null) return;

        // System.out.println("test : " + event.getSelectedOptions());




        List<Role> alzanorRole = event.getJDA().getRolesByName("Alzanor", true);
        List<Role> valehirRole = event.getJDA().getRolesByName("Valehir", true);
        List<Role> paimonRevenantRole = event.getJDA().getRolesByName("Paimon Revenant", true);
        List<Role> paimonRole = event.getJDA().getRolesByName("Paimon", true);
        List<Role> belialRole = event.getJDA().getRolesByName("Belial", true);
        List<Role> kirollasRole = event.getJDA().getRolesByName("Kirollas", true);
        List<Role> carnoRole = event.getJDA().getRolesByName("Carno", true);
        List<Role> fernonRole = event.getJDA().getRolesByName("Fernon", true);
        List<Role> zenasRole = event.getJDA().getRolesByName("Zenas", true);
        List<Role> ereniaRole = event.getJDA().getRolesByName("Erenia", true);
        List<Role> grenigasRole = event.getJDA().getRolesByName("Grenigas", true);
        List<Role> valakusRole = event.getJDA().getRolesByName("Valakus", true);
        List<Role> kertosRole = event.getJDA().getRolesByName("Kertos", true);
        List<Role> ibrahimRole = event.getJDA().getRolesByName("Ibrahim", true);
        List<Role> glacerusRole = event.getJDA().getRolesByName("Glacerus/Draco", true);
        List<Role> laurenaRole = event.getJDA().getRolesByName("Laurena", true);
        List<Role> yertirandRole = event.getJDA().getRolesByName("Yertirand", true);
        List<Role> fafnirRole = event.getJDA().getRolesByName("Fafnir", true);
        List<Role> eventRole = event.getJDA().getRolesByName("Event", true);
        List<Role> autreRole = event.getJDA().getRolesByName("Other Raid", true);
        List<Role> dailyRole = event.getJDA().getRolesByName("Daily Raids", true);
        List<Role> angelRole = event.getJDA().getRolesByName("Angel", true);
        List<Role> demonRole = event.getJDA().getRolesByName("Demon", true);

        List<Role> allRole = new ArrayList<>();

        try {
            allRole.add(alzanorRole.get(0));
            allRole.add(valehirRole.get(0));
            allRole.add(paimonRevenantRole.get(0));
            allRole.add(paimonRole.get(0));
            allRole.add(belialRole.get(0));
            allRole.add(kirollasRole.get(0));
            allRole.add(carnoRole.get(0));
            allRole.add(fernonRole.get(0));
            allRole.add(zenasRole.get(0));
            allRole.add(ereniaRole.get(0));
            allRole.add(grenigasRole.get(0));
            allRole.add(valakusRole.get(0));
            allRole.add(kertosRole.get(0));
            allRole.add(ibrahimRole.get(0));
            allRole.add(glacerusRole.get(0));
            allRole.add(laurenaRole.get(0));
            allRole.add(yertirandRole.get(0));
            allRole.add(fafnirRole.get(0));
            allRole.add(eventRole.get(0));
            allRole.add(autreRole.get(0));
            allRole.add(dailyRole.get(0));
            allRole.add(angelRole.get(0));
            allRole.add(demonRole.get(0));
        } catch (IndexOutOfBoundsException e) {
            event.reply("Some roles are missing from this server, please contact Zaack#1160 with error code #03").setEphemeral(true).queue();
        }


        int b = 0;
        while (b < allRole.size()) {
            guild.removeRoleFromMember(member, allRole.get(b)).queue();
            b++;
        }

        if (event.getSelectedOptions().isEmpty()) {
            event.reply("You won't get notified for any raids anymore ! \nPlease double check if the role(s) got correctly removed, if not, ask in " + helpChannel.getAsMention() + "! *(they can take up to 5 minutes to be removed)*").setEphemeral(true).queue();
            return;
        }

        ArrayList<String> rolesSelect = new ArrayList<String>();


        int i = 0;
        while (i < liste.size()) {
            SelectOption option = liste.get(i);

            switch (option.getValue()){
                case "value:alzanor" -> {
                    guild.addRoleToMember(member, alzanorRole.get(0)).queue();
                    rolesSelect.add(alzanorRole.get(0).getAsMention());
                }
                case "value:valehir" -> {
                    guild.addRoleToMember(member, valehirRole.get(0)).queue();
                    rolesSelect.add(valehirRole.get(0).getAsMention());

                }
                case "value:paimonrev" -> {
                    guild.addRoleToMember(member, paimonRevenantRole.get(0)).queue();
                    rolesSelect.add(paimonRevenantRole.get(0).getAsMention());

                }
                case "value:paimon" -> {
                    guild.addRoleToMember(member, paimonRole.get(0)).queue();
                    rolesSelect.add(paimonRole.get(0).getAsMention());

                }
                case "value:belial" -> {
                    guild.addRoleToMember(member, belialRole.get(0)).queue();
                    rolesSelect.add(belialRole.get(0).getAsMention());

                }
                case "value:kirollas" -> {
                    guild.addRoleToMember(member, kirollasRole.get(0)).queue();
                    rolesSelect.add(kirollasRole.get(0).getAsMention());

                }
                case "value:carno" -> {
                    guild.addRoleToMember(member, carnoRole.get(0)).queue();
                    rolesSelect.add(carnoRole.get(0).getAsMention());

                }
                case "value:fernon" -> {
                    guild.addRoleToMember(member, fernonRole.get(0)).queue();
                    rolesSelect.add(fernonRole.get(0).getAsMention());

                }
                case "value:zenas" -> {
                    guild.addRoleToMember(member, zenasRole.get(0)).queue();
                    rolesSelect.add(zenasRole.get(0).getAsMention());

                }
                case "value:erenia" -> {
                    guild.addRoleToMember(member, ereniaRole.get(0)).queue();
                    rolesSelect.add(ereniaRole.get(0).getAsMention());

                }
                case "value:grenigas" -> {
                    guild.addRoleToMember(member, grenigasRole.get(0)).queue();
                    rolesSelect.add(grenigasRole.get(0).getAsMention());

                }
                case "value:valakus" -> {
                    guild.addRoleToMember(member, valakusRole.get(0)).queue();
                    rolesSelect.add(valakusRole.get(0).getAsMention());

                }
                case "value:kertos" -> {
                    guild.addRoleToMember(member, kertosRole.get(0)).queue();
                    rolesSelect.add(kertosRole.get(0).getAsMention());

                }
                case "value:ibrahim" -> {
                    guild.addRoleToMember(member, ibrahimRole.get(0)).queue();
                    rolesSelect.add(ibrahimRole.get(0).getAsMention());

                }
                case "value:glacerus" -> {
                    guild.addRoleToMember(member, glacerusRole.get(0)).queue();
                    rolesSelect.add(glacerusRole.get(0).getAsMention());

                }
                case "value:laurena" -> {
                    guild.addRoleToMember(member, laurenaRole.get(0)).queue();
                    rolesSelect.add(laurenaRole.get(0).getAsMention());

                }
                case "value:yertirand" -> {
                    guild.addRoleToMember(member, yertirandRole.get(0)).queue();
                    rolesSelect.add(yertirandRole.get(0).getAsMention());

                }
                case "value:fafnir" -> {
                    guild.addRoleToMember(member, fafnirRole.get(0)).queue();
                    rolesSelect.add(fafnirRole.get(0).getAsMention());

                }
                case "value:event" -> {
                    guild.addRoleToMember(member, eventRole.get(0)).queue();
                    rolesSelect.add(eventRole.get(0).getAsMention());

                }
                case "value:autre" -> {
                    guild.addRoleToMember(member, autreRole.get(0)).queue();
                    rolesSelect.add(autreRole.get(0).getAsMention());
                }
                case "value:daily" -> {
                    guild.addRoleToMember(member, dailyRole.get(0)).queue();
                    rolesSelect.add(dailyRole.get(0).getAsMention());
                }
                case "value:angel" -> {
                    guild.addRoleToMember(member, angelRole.get(0)).queue();
                    rolesSelect.add(angelRole.get(0).getAsMention());
                }
                case "value:demon" -> {
                    guild.addRoleToMember(member, demonRole.get(0)).queue();
                    rolesSelect.add(demonRole.get(0).getAsMention());
                }
            }


            i++;
        }

        String string = "";
        for (String s : rolesSelect) {
            if (!string.isBlank()) { string += ", "; }
            string += s;
        }

        event.reply("You will now be notified when the following raids are getting planned : " + string + "\nPlease verify that the roles have correctly been added to you, if not, try again by unselecting the roles and re-selecting them, or ask in " + helpChannel.getAsMention() + "! *(they can take up to 5 minutes to be added)*").setEphemeral(true).queue();
    }
}
