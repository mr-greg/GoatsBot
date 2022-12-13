package me.goats.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RulesCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("rules")) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            System.out.println("not admin perm");
            event.reply("You do not have the permission to do that :)").setEphemeral(true).queue();
            return;
        }

        Guild guild = event.getGuild();
        TextChannel screen = guild.getTextChannelById(1045219798365253732L);
        ForumChannel feedback = guild.getForumChannelById(1044093686985793567L);

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Rules !")
                .setDescription("Any intentional or severe infraction against the rules of the server will most likely result in a permanent ban, I don't particularly fancy doing moderation, sorry ! \n" +
                        "*(I'll be understanding if you're an innocent soul that meant no harm tho)*")
                .addField("Language / Behavior", " - Any hostile insult are prohibited \n" +
                        "- Any discrimination or racism against any race, country, religion, gender or whatever are prohibited \n" +
                        "- - Please do not make fun of people for asking questions, there is no stupid ones :)", false)
                .addField("Spamming", "- Spamming the text channels are prohibited \n" +
                        "- Spamming the bot for no reason is prohibited (special instant ban if it's a register spam to annoy a leader)", false)
                .addField("Buying, selling, looking for family...", "- Unfortunately, this server is for raiding purposes only, you man share images in the " + screen.getAsMention() + " but please do not spam the server to try and sell/buy shit, you can do that in the official nostale server or in DM I guess \n" +
                        "- This may change in the future if you guys ask for it in the " + feedback.getAsMention() + " channel, right now I'm trying to keep this server as simple as possible !", false)
                .addField("Illegal stuff", "- Selling gold\n" +
                        "- Selling accounts \n" +
                        "- Promotion towards private servers\n" +
                        "- Promotion towards cheats and stuff\n" +
                        "- Promotion towards your discord server for no reason\n" +
                        "- Not calling <@156432016714366976> mafiou instead of Telisnir\n" +
                        "- Talking alone with the bot in private message (yes I can see the bot's private messages, please don't confess your love to him)\n" +
                        "- Sending weird pictures (gore, porn, really sussy weeb stuff, animals getting hurt *(childrens falling is fine)*\n" +
                        "- Any other weird, unnecessary shit that people don't want to see and report to me \n" +
                        "- Crying because someone you met here inted a league of legends game", false)
                .addField("Raids in-game", "- Trolling a raid that was planned here will result in a permanent ban\n" +
                        "- Un-registering to raids over and over at last minute may result in a permanent ban\n" +
                        "- Any disrespectful behavior within a raid hosted here maybe result in a permanent ban if the leader consider it necessary\n" +
                        "- No, I dnt give a shit that X person on this server is killing you in arena, I won't ban them for that *(unless they're using holy+15 with 100 defense points, then I will laugh at you)*", false)
                .addField("Non exhaustive list", "This list is non-exhaustive, if you manage to be super annoying to everyone without breaking any rule, you still might get banned", false)
                .setFooter("I know I wrote those rules for nothing bc no one will read them, I don't read server's rules myself lol");

        event.getChannel().sendMessageEmbeds(eb.build()).queue(reply -> {
            reply.addReaction(Emoji.fromUnicode("\uD83C\uDDEB\uD83C\uDDF7")).queue();
            reply.addReaction(Emoji.fromUnicode("\uD83C\uDDEC\uD83C\uDDE7")).queue();
            reply.addReaction(Emoji.fromUnicode("\uD83C\uDDE9\uD83C\uDDEA")).queue();
            reply.addReaction(Emoji.fromUnicode("\uD83C\uDDEA\uD83C\uDDF8")).queue();
            reply.addReaction(Emoji.fromUnicode("\uD83C\uDDF5\uD83C\uDDF1")).queue();
            reply.addReaction(Emoji.fromUnicode("\uD83C\uDDF7\uD83C\uDDFA")).queue();
        });

    }
}
