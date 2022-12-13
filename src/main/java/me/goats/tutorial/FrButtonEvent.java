package me.goats.tutorial;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class FrButtonEvent extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("tutorial:fr")) return;
        Guild guild = event.getGuild();
        System.out.println("fr tutorial button pressed");


        String notifChannelMention = guild.getTextChannelById(dotenv.get("NOTIFCHANNELID")).getAsMention();
        String helpChannelMention = guild.getForumChannelById(dotenv.get("HELPCHANNELID")).getAsMention();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Tutoriel en français !");
        eb.setThumbnail("https://i.imgur.com/C5VbYAb.png");
        eb.setDescription("Pour commencer, tu peux sélectionner le rôle correspondant à ta classe sous ce message ! (facultatif)");
        eb.addField("Renomme-toi", "Nous te recommandons fortement de changer ton nom avec ton pseudo en jeu et ton niveau (Goats+65)", false);
        eb.addField("Notifications", "Tu peux sélectionner les raids dont tu souhaite être notifié dans " + notifChannelMention, false);
        eb.addField("Inscription aux raids", "Tu peux t'inscrire aux raids dans la catégorie \"Register to raid\" \n" +
                "Quand un raid recrute, un salon avec le nom du raid et du lanceur apparaît avec toutes les informations dont tu aura besoin !", false);
        eb.addField("Informations pour s'inscrire", "Lors de l'inscription, il te sera demandé les informations suivantes : \n" +
                "- Pseudo en jeu \n" +
                "- Classe + Niveau\n" +
                "- SP dispo pour le raid\n" +
                "- Le nombre de mules que tu veux ramener (tape 1 si tu n'en a pas)\n" +
                "- Toute autre information que tu souhaites passer au lanceur du raid\n" +
                "Si, pour quelconque raison, tu dois laisser un champ vide, met un 0 à l'intérieur afin de valider (ou n'importe quoi osef)", false);
        eb.addField("Informations personnelles", "Pour des raisons évidentes, merci de ne pas soumettre quelconque information personnelle qui doit rester privée, où que ce soit sur ce serveur (ni sur discord en général d'ailleurs)", false);
        eb.addField("Rejoindre le raid", "Une fois le formulaire validé, le lanceur du raid va recevoir une notification avec les infos que tu auras passé dans le formulaire\n" +
                "Il n'aura plus qu'à accepter ou non ta requête", false);
        eb.addField("Accepté dans le raid", "Si tu es accepté, tu aura accès à un salon privé pour discuter avec l'équipe du raid (tu recevra également un MP du bot pour te rappeler dans quel raid tu as été accepté et son heure de lancement)", false);
        eb.addField("Refusé", "Si tu es refusé, tu vas recevoir un MP du bot qui te le dira afin que tu n'attende pas pour rien !", false);
        eb.addField("Se désinscrire d'un raid", "Si tu dois te désinscrire d'un raid, tu peux rapidement ouvrir le salon d'inscription du raid concerné et cliquer sur le bouton \"un-register\", le bot se chargera d'en informer le launceur du raid et il te retirera de la liste ainsi que les accès aux salons privés du raid à ta place !\n" +
                "*(si les inscriptions au raid sont déjà fermées, il n'y aura pas ce bouton, il faudra en informer le lanceur toi-même !)*", false);
        eb.addField("Devenir un Leader", "Si tu souhaite lancer tes propres raids sur ce serveur, merci de me contacter Zaack#1160", false);
        eb.addField("Auto-traduction", "Vous pouvez réagir à un message avec un de ces drapeaux :flag_gb: :flag_fr: :flag_pl: :flag_es: :flag_de: :flag_ru: afin de recevoir la traduction (dans la langue du drapeau) du message auquel vous avez réagit en MP !", false);
        eb.addField("Questions ?", "S'il te reste des questions (il n'existe pas de question stupide), tu peux les poser dans ce salon : " + helpChannelMention, false);

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
