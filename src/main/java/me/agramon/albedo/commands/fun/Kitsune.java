package me.agramon.albedo.commands.fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Kitsune extends Command {

    private static final OkHttpClient client = new OkHttpClient();

    public Kitsune() {
        super.name = "kitsune";
        super.help = "Fox girls <3";
        super.cooldown = 5;
        super.category = new Category("Fun");
    }

    protected void execute(CommandEvent e) {
        String url;
        if (e.getMessage().getTextChannel().isNSFW()) {
            url = "https://nekos.life/api/v2/img/lewdk";
        } else {
            url = "https://nekos.life/api/v2/img/kemonomimi";
        }
        String image = null;

        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try (ResponseBody responseBody = response.body()) {
            if (!response.isSuccessful()) try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                image = new JSONObject(Objects.requireNonNull(responseBody).string()).get("url").toString();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        MessageEmbed embed;
        embed = EmbedUtils.embedImage(image)
                .setColor(Color.MAGENTA)
                .build();
        e.reply(embed);
    }
}

