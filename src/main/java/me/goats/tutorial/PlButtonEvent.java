package me.goats.tutorial;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class PlButtonEvent extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("tutorial:pl")) return;
        Guild guild = event.getGuild();
        System.out.println("pl tutorial button pressed");


        String notifChannelMention = guild.getTextChannelById(dotenv.get("NOTIFCHANNELID")).getAsMention();
        String helpChannelMention = guild.getForumChannelById(dotenv.get("HELPCHANNELID")).getAsMention();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Polski poradnik !");
        eb.setThumbnail("https://i.imgur.com/C5VbYAb.png");
        eb.setDescription("Możesz wybrać rolę ze swoją klasą pod tą wiadomością, ale nie jest to obowiązkowe");
        eb.addField("Zmień swój nick", "Zachęcamy zmienić nazwę na discordzie na nick i lvl, który posiadacie w grze. np. (Goats+65)", false);
        eb.addField("Powiadomienia", "Możesz wybrać na jakie rajdy chcesz być wołany " + notifChannelMention, false);
        eb.addField("Dołączanie do rajdów", "Możesz się zarejestrować do rajdów w kategorii \"Register to raid\" \n" +
                "Kiedy będą poszukiwane osoby do rajdów, pojawi się kanał z rajdami i nick lidera ze wszystkimi informacjami, których potrzebujesz", false);
        eb.addField("Informacja jak się zarejestrować", "Kiedy się zapiszesz, zostaniesz zapytany o : \n" +
                "- Nick w grze \n" +
                "- Klasa + poziom\n" +
                "- Sp na którym idziesz\n" +
                "- Ilość altów, które chcesz wziąć ze sobą(Wpisz 1 kiedy idziesz bez altów)\n" +
                "- Pozostałe informacje, które chcesz przekazać liderowi\n" +
                "jeśli z jakiegoś powodu zostawiłeś pusty formularz to musisz napisać w środku 0 (albo jakąś literę, bądź cyfrę)", false);
        eb.addField("Dane personalne", "Z oczywistych powodów, nie wysyłaj żadnych informacji, które musisz zachować prywatnymi", false);
        eb.addField("Dostanie się do rajdu", "Po przesłaniu formularza, lider rajdu dostanie powiadomienie z informacjami, które podałeś w formularzu\n" +
                "A następnie zdecyduje czy zaakceptuje twoją prośbę do rajdu", false);
        eb.addField("ZZatwierdzona prośba do rajdu", "Jeśli twój formularz został zaakceptowany, uzyskasz dostęp do prywatnego kanału tekstowego drużyny.(dostaniesz również PW, aby przypomnieć Ci, jaki i o której godzinie jest rajd na który się zapisałeś)", false);
        eb.addField("Odrzucony zapis na rajd", "Jeśli lider odrzucił twój formularz to dostaniesz wiadomość od bota na PW informującą, że nie musisz czekać !", false);
        eb.addField("Wypisanie się z rajdu", "Jeśli musisz się wypisać z rajdu, musisz wejść na kanał rejestracji i kliknąć przycisk \"un-register\" Bot poinformuje lidera o twoim wypisie !\n" +
                "*(Może być niedostępne, jeśli rejestracja do rajdu jest już zamknięta. W takim przypadku skontaktuj się z liderem !)*", false);
        eb.addField("Zostanie Liderem rajdu", "Jeśli chcesz mieć możliwość prowadzenia własnych rajdów na tym serwerze to musisz się skontaktować z Zaack#1160", false);
        eb.addField("Automatyczne tłumaczenie", "Możesz zareagować na dowolną wiadomość przy pomocy jednej z tych flag. :flag_gb: :flag_fr: :flag_pl: :flag_es: :flag_de: :flag_ru: Uzyskasz w ten sposób przetłumaczona wersję wiadomości na wybrany przez siebie język !", false);
        eb.addField("Pytania ?", "Jeżeli masz jakieś pytania(Nie ma głupich pytań) to pisz w : " + helpChannelMention, false);

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
