package me.goats.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;

public class WarnUserCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("warn")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            System.out.println("not admin perm");
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }
        OptionMapping option = event.getOption("user");
        if (option == null) {
            event.reply("please select a valid option/user").queue();
            return;
        }

        TextChannel warnLog = event.getGuild().getTextChannelById(1045856204863311892L);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("WARNING !");
        eb.setDescription("You have been reported as absent for a raid that you registered to and didn't un-register to inform your team, they've been waiting for you !");
        eb.addField("What happens now ?", "This will be your only warning, next time you will automatically get banned from the discord server :( \n" +
                "If this happens again and you get banned, you can still contact Zaack#1160 and explain the situation if it was valid, you might get a last chance !", true);
        eb.setColor(Color.RED);
        eb.setThumbnail("https://i.imgur.com/w7D8f71.png");

        User user = option.getAsUser();

        user.openPrivateChannel().queue(reply -> {
            reply.sendMessageEmbeds(eb.build()).submit()
                    .whenComplete((s, error) -> {
                        if (error != null){
                            event.reply(user.getAsMention() + " doesn't have his MP open, I cannot DM him").setEphemeral(true).queue();
                            warnLog.sendMessage(user.getAsMention() + " has been warned, yet his DMs were closed").queue();

                        } else {
                            event.reply(user.getAsMention() + " has been warned").setEphemeral(true).queue();
                            warnLog.sendMessage(user.getAsMention() + " has been warned").queue();
                        }
                    });
        });


    }
}
