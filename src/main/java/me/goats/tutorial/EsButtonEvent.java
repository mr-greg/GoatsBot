package me.goats.tutorial;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class EsButtonEvent extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getId().equals("tutorial:es")) return;
        Guild guild = event.getGuild();
        System.out.println("es tutorial button pressed");


        String notifChannelMention = guild.getTextChannelById(dotenv.get("NOTIFCHANNELID")).getAsMention();
        String helpChannelMention = guild.getForumChannelById(dotenv.get("HELPCHANNELID")).getAsMention();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("E¡Tutorial en español!");
        eb.setThumbnail("https://i.imgur.com/C5VbYAb.png");
        eb.setDescription("Para empezar, puedes seleccionar un rol con tu clase debajo de este mensaje, también te recomendamos cambiar tu apodo al que uses en el juego junto a tu nivel (\"apodo\"\"lvl\") ej: Goats+65 aunque no es obligatorio");
        eb.addField("Notificaciones", "Puedes seleccionar los raids de los cuales quieras recibir notificaciones en " + notifChannelMention, false);
        eb.addField("Registrarse en Raids", "Puedes registrarte a un raid en la categoría \"Register to raid\" \n" +
                "Cuando se esté reclutando a jugadores para una raid un canal con el nombre de la raid aparecerá junto al nombre del líder junto a toda la información que puedas necesitar", false);
        eb.addField("Registro", "Cuando te estés registrando  se te pedirá : \n" +
                "- Nombre en el juego \n" +
                "- Clase + Nivel\n" +
                "- Las Sp que puedes usar\n" +
                "- El número de cuentas que quieras traer contigo\n" +
                "- Cualquier información que quieras que el líder sepa\n" +
                "Si por cualquier motivo necesitas dejar un campo vacío, rellénalo con un 0", false);
        eb.addField("Información personal", "Por motivos evidentes, por favor no des ninguno de tus datos personales", false);
        eb.addField("Unirse a un raid", "Una vez rellenes el formulario con la información, el líder recibirá una notificación con los datos\n" +
                "Entonces decidirá si acepta tu petición o no", false);
        eb.addField("Aceptado en la raid", "Una vez aceptado en el equipo, tendrás acceso al canal privado del raid, también recibirás un MD para recordarte en qué raid has entrado y a qué hora es", false);
        eb.addField("Peticion rechazada", "Si te rechazan, recibirás un MD del bot informándote", false);
        eb.addField("Abandonar un raid", "Si necesitas abandonar, puedes muy fácilmente ir al canal de registros y presionar el botón \"un-register\", el bot informará al líder y te expulsará del equipo. Aunque si el registro está cerrado, tendrás que pedirle al líder que te expulse\n", false);
        eb.addField("Convertirse en líder", "Si quieres poder crear tus propios raids, contacta con Zaack#1160", false);
        eb.addField("autotraducción","puedes reaccionar a  cualquier mensaje con una de estas banderas :flag_gb: :flag_fr: :flag_pl: :flag_es: :flag_de: :flag_ru: para que el mensaje se traduzca al  idioma de la bandera con la que has reaccionado",false);
        eb.addField("Dudas", "Si tienes alguna pregunta no dudes en ir a : " + helpChannelMention, false);

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
