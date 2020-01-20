package me.agramon.albedo.commands.administration;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.agramon.albedo.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;

import java.util.List;

public class UpdateUsers extends Command {
    public UpdateUsers() {
        super.name = "updateusers";
        super.help = "Adds all users to the database";
        super.category = new Category("Administration");
    }

    @Override
    protected void execute(CommandEvent e) {
        if (!e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            e.reply("Begone! You cannot tell me what to do, scum!");
            return;
        }

        List<Member> members = e.getGuild().getMembers();
        int added = 0;
        String URI = Config.getURI("URI");
        MongoClient mongoClient = MongoClients.create(URI);
        MongoDatabase db = mongoClient.getDatabase(Config.getDB("DATABASE"));
        MongoCollection<Document> collection = db.getCollection(Config.getCol("COLLECTION"));

        for (int i = 0; i < members.size(); i++) {
            String user = members.get(i).getId();
            Document found = collection.find(new Document("UserID", user)).first();

            if (found == null) {
                Document document = new Document("UserID", user);
                document.append("Adores", 0);
                collection.insertOne(document);
                added++;
            }
        }
        e.reply(added + "/" + members.size() + " users has been added to the database");
    }
}
