package me.goats.tutorial;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class EngButtonEvent extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("tutorial:eng")) return;
        Guild guild = event.getGuild();
        System.out.println("eng tutorial button pressed");


        String notifChannelMention = guild.getTextChannelById(dotenv.get("NOTIFCHANNELID")).getAsMention();
        String helpChannelMention = guild.getForumChannelById(dotenv.get("HELPCHANNELID")).getAsMention();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("English tutorial is it !");
        eb.setThumbnail("https://i.imgur.com/C5VbYAb.png");
        eb.setDescription("To get started, you can select a role with your class below this message, but it's not mandatory");
        eb.addField("Rename yourself", "We highly recommend you to rename with your in-game name and level (Goats+65)", false);
        eb.addField("Notifications", "You can select raids you want notifications for in " + notifChannelMention, false);
        eb.addField("Register to raids", "You can register to raids in the category \"Register to raid\" \n" +
                "When a raid is recruiting, a channel with the raid and the name of the leader will appear with all the informations you need !", false);
        eb.addField("Infos to register", "When registering, you will be asked the following informations : \n" +
                "- In-game name \n" +
                "- Class + Level\n" +
                "- SP you can use\n" +
                "- The number of alts you want to bring (type 1 if it's just you without any alts)\n" +
                "- Any other infos you want the leader to know\n" +
                "If, for some reason, you need to let a row empty, you need to type a 0 inside it (or any letter or number)", false);
        eb.addField("Personal informations", "For obvious reasons, please do not submit any informations that you need to keep private in this server (nor discord in general)", false);
        eb.addField("Getting into the raid", "Once you submit the form, the leader of the raid will receive a notification with the infos you gave inside the form\n" +
                "He will then decide whether he accept or deny your request", false);
        eb.addField("Accepted into the raid", "If you're accepted, you will get access to the team's private text channel (also a DM to remind you which raid you got into and what time it is !)", false);
        eb.addField("Request denied", "If you're denied, you will receive a DM from the bot informiing you so you don't wait for nothing !", false);
        eb.addField("Un-register from a raid", "If you need to un-register from a raid, you can quickly come to the registering channel and click on the \"un-register\" button, the bot will then inform the leader and remove your access to the raid for you !\n" +
                "*(might not be available if registrations are already closed, in that case please contact the leader directly !)*", false);
        eb.addField("Become a leader", "If you want to be able to lead your own raids on this server, please contact Zaack#1160", false);
        eb.addField("Auto-Translator", "You can react to any message with once of those flags :flag_gb: :flag_fr: :flag_pl: :flag_es: :flag_de: :flag_ru: to get the translation according to the flag's language of the message you reacted to sent to you in DM !", false);
        eb.addField("Questions ?", "Please ask any question you have (there is no stupid one) in this channel : " + helpChannelMention, false);

        StringSelectMenu menu = StringSelectMenu.create("menu:profile")
                .setPlaceholder("Select your class !")
                .setRequiredRange(0,5)
                .addOption("Archer", "value:archer", "I am an Archer", Emoji.fromCustom("arc", 1043081953567576124L, false))
                .addOption("Swordsman", "value:sword", "I am a Swordsman", Emoji.fromCustom("epee", 1043081956327440464L, false))
                .addOption("Mage", "value:mage", "I am a Mage", Emoji.fromCustom("baton", 1043081954796511272L, false))
                .addOption("Martial Artist", "value:ma", "I am a Martial Artist", Emoji.fromCustom("ma", 1043082939384205332L, false))
                .addOption("Adventurer", "value:adv", "I am an Adventurer for some reason", Emoji.fromCustom("adv", 1043082938155278356L, false))
                .build();

        event.replyEmbeds(eb.build()).addActionRow(menu).setEphemeral(true).queue();
    }
}
