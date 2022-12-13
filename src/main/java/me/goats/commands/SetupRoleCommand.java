package me.goats.commands;

import io.github.cdimascio.dotenv.Dotenv;
import me.goats.utils.Database;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class SetupRoleCommand extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Database database = new Database();

        if (event.getName().equals("setuproles")){

            if (!event.isFromGuild()) {
                System.out.println("isFromGuild false");
                event.reply("This command only works on guidls").queue();
                return;
            }

            if (event.getGuild() == null) return;

            if (event.getMember() == null) return;

            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                System.out.println("not admin perm");
                event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
                return;
            }

            String createtablerole = "CREATE TABLE IF NOT EXISTS guilds (\n"
                    + "id INTEGER PRIMARY KEY, \n"
                    + "guildID STRING, \n"
                    + "roleSetup INTEGER"
                    + ");";

            database.createNewTable(dotenv.get("URLDB"), createtablerole );

            Integer setupRoleState = database.checkSetupRole("SELECT roleSetup FROM guilds");


            // check if déjà fait (1 = déjà fait, null pas encore fait)
            if (setupRoleState != null || setupRoleState != 0) {
                event.reply("Roles already set up, please contact Zaack#1160 with the error code if you have an issue (error code #01)").setEphemeral(true).queue();
                return;
            }

            event.getGuild().createRole().setName("Leader").setColor(Color.YELLOW).queue();
            event.getGuild().createRole().setName("Alzanor").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Valehir").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Paimon Revenant").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Paimon").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Belial").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Kirollas").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Carno").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Fernon").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Zenas").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Erenia").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Grenigas").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Valakus").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Kertos").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Ibrahim").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Glacerus/Draco").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Laurena").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Yertirand").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Fafnir").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Asgobas IC").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Event").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Other Raid").setColor(Color.ORANGE).queue();
            event.getGuild().createRole().setName("Raider").setColor(Color.ORANGE).queue();

            String sql = "INSERT INTO guilds(guildID, roleSetup) VALUES(?,?)";

            String guildID = event.getGuild().getId();

            database.roleSetup(sql, guildID);

            event.reply("The roles has been setup. You can now use /selectrolemenu to allow your users selecting roles").queue();


        }
    }
}
