package me.goats;

import io.github.cdimascio.dotenv.Dotenv;
import me.goats.act4.PercentageCommand;
import me.goats.act4.ReadyEvent;
import me.goats.commands.AddRoleCommand;
import me.goats.commands.SetupRoleCommand;
import me.goats.misc.*;
import me.goats.raids.*;
import me.goats.registering.RegisterButtonEvent;
import me.goats.registering.RegisterModalEvent;
import me.goats.registering.UnregisterButtonEvent;
import me.goats.roleNotifications.SelectMenuInteractionEvent;
import me.goats.roleNotifications.SelectMenuMessageCommand;
import me.goats.roleProfile.SelectRoleProfilEvent;
import me.goats.roleProfile.SelectRoleProfileCommand;
import me.goats.translator.ReactionTranslateEvent;
import me.goats.translator.TranslatorText;
import me.goats.tutorial.*;
import me.goats.utils.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class GoatsBot {

    public static void main(String[] args) throws InterruptedException {
        Dotenv dotenv = Dotenv.load();
        String urlDB = dotenv.get("URLDB");


        JDA bot = JDABuilder.createDefault(dotenv.get("TOKEN"))
                .setActivity(Activity.playing("NosTale"))
                .addEventListeners(new SetupRoleCommand())
                .addEventListeners(new SelectMenuMessageCommand())
                .addEventListeners(new SelectMenuInteractionEvent())
                .addEventListeners(new RaidCreateCommand())
                .addEventListeners(new RaidSelectInteractionEvent())
                .addEventListeners(new JoinRoleAddEvent())
                .addEventListeners(new RaidFilledModalEvent())
                .addEventListeners(new DeleteRoleCommand())
                .addEventListeners(new RegisterButtonEvent())
                .addEventListeners(new UnregisterButtonEvent())
                .addEventListeners(new RegisterModalEvent())
                .addEventListeners(new AcceptButtonEvent())
                .addEventListeners(new DenyButtonEvent())
                .addEventListeners(new EnableSelectRaidCommand())
                .addEventListeners(new DisableSelectRaidCommand())
                .addEventListeners(new CancelRaidButtonEvent())
                .addEventListeners(new CloseRegisterButtonEvent())
                .addEventListeners(new CompleteRaidButtonEvent())
                .addEventListeners(new SelectRoleProfileCommand())
                .addEventListeners(new SelectRoleProfilEvent())
                .addEventListeners(new PatchNoteCommand())
                .addEventListeners(new TutorialCommand())
                .addEventListeners(new LogPrivateMessageEvent())
                .addEventListeners(new EngButtonEvent())
                .addEventListeners(new DeButtonEvent())
                .addEventListeners(new FrButtonEvent())
                .addEventListeners(new RuButtonEvent())
                .addEventListeners(new PlButtonEvent())
                .addEventListeners(new ReadyEvent())
                .addEventListeners(new PercentageCommand())
                .addEventListeners(new AddRoleCommand())
                .addEventListeners(new TranslatorText())
                .addEventListeners(new ReactionTranslateEvent())
                .addEventListeners(new EsButtonEvent())
                .addEventListeners(new RulesCommand())
                .addEventListeners(new WarnUserCommand())
                .addEventListeners(new KickAllCommand())
                .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .build().awaitReady();

        Database connect = new Database();
        connect.dbConnect(urlDB);

        Guild mav = bot.getGuildById(537073420207259668L);
        Guild intserver = bot.getGuildById(1037259106832089129L);
        if (mav != null) {
            mav.upsertCommand("setuproles", "Setup the roles for the bot").queue();
            mav.upsertCommand("selectrolemenu", "Sends the select menu message for role notification selection").queue();
            mav.upsertCommand("raidcreateselect", "Create a raid planification now !").queue();
        }
        if (intserver != null) {
            intserver.upsertCommand("setuproles", "Setup the roles for the bot").queue();
            intserver.upsertCommand("selectrolemenu", "Sends the select menu message for role notification selection").queue();
            intserver.upsertCommand("raidcreateselect", "Create a raid planification now !").queue();
            intserver.upsertCommand("deleteroles", "delete the roles").queue();
            intserver.upsertCommand("enableraidcreate", "Enable the select menu for raid creations").queue();
            intserver.upsertCommand("disableraidcreate", "Disable the select menu for raid creations").queue();
            intserver.upsertCommand("selectroleprofile", "Sends the select menu message for profile's role selection").queue();
            intserver.upsertCommand("patchnote", "Sends the patch note of the day !").queue();
            intserver.upsertCommand("setuptutorial", "Sends the tutorial setup message").queue();
            intserver.upsertCommand("act4", "Sends the ice crown (act4) percentages in real time !").queue();
            intserver.upsertCommand("addroles", "dev only").queue();
            intserver.upsertCommand("translator", "dev only").queue();
            intserver.upsertCommand("rules", "dev only").queue();
            intserver.upsertCommand("warn", "dev only")
                    .addOption(OptionType.USER, "user", "the user")
                    .queue();
            intserver.upsertCommand("kickall", "dev only").queue();

        }

    }
}
