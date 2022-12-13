package me.goats.commands;

import io.github.cdimascio.dotenv.Dotenv;
import me.goats.utils.Database;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.PreparedStatement;
import java.util.List;

public class TestData extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("createtable")){
            Database database = new Database();


        String sql = "CREATE TABLE IF NOT EXISTS prostitute2 (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	prenom text NOT NULL,\n"
                + "	prix INTEGER NOT NULL\n"
                + ");";

            database.createNewTable(dotenv.get("URLDB"), sql);

            event.reply("voyons voir ça...").queue();
        }

        if (event.getName().equals("createtablerole")) {
            Database database = new Database();

            String sql = "CREATE TABLE IF NOT EXISTS guilds (\n"
                    + " id INTEGER PRIMARY KEY,\n"
                    + "roleSetup INTEGER NOT NULL\n"
                    + ");";

            database.createTableRole(dotenv.get("URLDB"), sql);
            event.reply("voyons voir ça...").queue();
        }

        if (event.getName().equals("insertdata")) {
            Database database = new Database();

            String sql = "INSERT INTO guilds(roleSetup) VALUES(?)";

            // database.roleSetup(sql);
            event.reply("voyons voir ça...").queue();


        }
    }
}
