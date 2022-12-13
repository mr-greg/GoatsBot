package me.goats.tutorial;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class RuButtonEvent extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("tutorial:ru")) return;
        Guild guild = event.getGuild();
        System.out.println("ru tutorial button pressed");


        String notifChannelMention = guild.getTextChannelById(dotenv.get("NOTIFCHANNELID")).getAsMention();
        String helpChannelMention = guild.getForumChannelById(dotenv.get("HELPCHANNELID")).getAsMention();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Переименуй себя !");
        eb.setThumbnail("https://i.imgur.com/C5VbYAb.png");
        eb.setDescription("Для начала вы можете выбрать роль с вашим классом под этим сообщением, но это не обязательно");
        eb.addField("Переименуй себя", "Мы настоятельно рекомендуем вам переименовать с вашим игровым именем и уровнем (Goats+65)", false);
        eb.addField("Уведомления", "Вы можете выбрать рейды, о которых хотите получать уведомления, в " + notifChannelMention, false);
        eb.addField("Регистрация на рейды", "Зарегистрироваться на рейды можно в категории \"Register to raid\" \n" +
                "Когда рейд набирается, появится канал с рейдом и именем лидера со всей необходимой вам информацией!", false);
        eb.addField("Информация для регистрации", "При регистрации у вас будет спрошенна следующая информация: \n" +
                "- Имя в игре \n" +
                "- Класс + Уровень\n" +
                "- SP вы можете использовать\n" +
                "- Количество альтов, которые вы хотите принести (введите 1, если это только вы без альтов)\n" +
                "- Любая другая информация, которую вы хотите добавить, чтобы лидер знал\n" +
                "Если по какой-то причине вам нужно оставить строку пустой, вам нужно ввести внутри нее 0 (или любую букву или цифру)", false);
        eb.addField("Персональная информация", "По очевидным причинам, пожалуйста, не отправляйте какую-либо информацию, которую вам нужно сохранить в тайне на этом сервере (и вообще в Discord)", false);
        eb.addField("Попадание в рейд", "Как только вы отправите форму, лидер рейда получит уведомление с информацией, которую вы указали в форме\n" +
                "Затем он решит, принять или отклонить ваш запрос", false);
        eb.addField("Принят в рейд", "Если вы приняты, вы получите доступ к закрытому текстовому каналу команды (а также DM, чтобы напомнить вам, в какой рейд вы попали и в который час!)", false);
        eb.addField("Запрос отклонен", "Если вам откажут, вы получите DM от бота, информирующего вас, чтобы вы не ждали !", false);
        eb.addField("Отменить регистрацию в рейде", "Если вам нужно отписаться от рейда, вы можете зайти в канал регистрации и нажать на кнопку \"отменить регистрацию\" , после этого бот сообщит об этом лидера и удалит вас из рейда!\n" +
                "*(может быть недоступно, если регистрация уже закрыта, в этом случае свяжитесь напрямую с руководителем!)*", false);
        eb.addField("Стать лидером", "Если вы хотите иметь возможность проводить свои собственные рейды на этом сервере, пожалуйста, свяжитесь с Zaack#1160", false);
        eb.addField("самостоятельный перевод", "Вы можете отреагировать на любое сообщение одним и :flag_gb: :flag_fr: :flag_pl: :flag_es: :flag_de: :flag_ru: чтобы получить перевод в соответствии с языком флага. Перевод будет вам отправлен в DM !", false);
        eb.addField("Вопросы ?", "Пожалуйста, задавайте любой интересующий вас вопрос (глупых не бывает) в этом канале : " + helpChannelMention, false);

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
