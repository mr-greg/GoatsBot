package me.goats.misc;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DeleteRoleCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("deleteroles")) return;
        Guild guild = event.getGuild();
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
        List<Role> asgobasRole = event.getJDA().getRolesByName("Asgobas IC", true);
        List<Role> eventRole = event.getJDA().getRolesByName("Event", true);
        List<Role> autreRole = event.getJDA().getRolesByName("Other Raid", true);

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
            allRole.add(asgobasRole.get(0));
            allRole.add(eventRole.get(0));
            allRole.add(autreRole.get(0));
        } catch (IndexOutOfBoundsException e) {
            event.reply("Some roles are missing from this server, please contact Zaack#1160 with error code #03").setEphemeral(true).queue();
        }

        int b = 0;
        while (b < allRole.size()) {
            try {
                guild.getRoleById(allRole.get(b).getIdLong()).delete().queue();

            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
            b++;
        }
    }
}
