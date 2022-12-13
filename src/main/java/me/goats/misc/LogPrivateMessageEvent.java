package me.goats.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.TimeFormat;
import net.dv8tion.jda.api.utils.Timestamp;

import java.awt.*;
import java.util.List;

public class LogPrivateMessageEvent extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannelType().isGuild()) return;
        if (event.getAuthor().isBot()) return;

        List<Message.Attachment> pj = event.getMessage().getAttachments();

        String currentTime = String.valueOf(TimeFormat.RELATIVE.now());

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("New private message");
        eb.setDescription("Message from : " + event.getAuthor().getAsMention());
        eb.addField("Message sent : ", currentTime, false);
        eb.addField("Content : ", event.getMessage().getContentStripped(), false);
        if (!pj.isEmpty()){
            for (Message.Attachment attachment : pj) {
                String linkImage = attachment.getUrl();
                eb.addField("PJ : ", linkImage, false);
            }
        }

        eb.setColor(Color.YELLOW);

        Guild guild = event.getJDA().getGuildById("1037259106832089129");
        TextChannel salon = guild.getTextChannelById("1044150491258560612");
        salon.sendMessage("<@281570140888367114>").setEmbeds(eb.build()).queue();
    }
}
