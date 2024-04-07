package fr.gravendev.announcer.configuration.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageCreateBuilderDeserializer extends JsonDeserializer<MessageCreateBuilder> {

    @Override
    public MessageCreateBuilder deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        DataObject dataObject = DataObject.fromJson(jsonParser.readValueAsTree().toString());

        DataArray dataEmbeds = dataObject.getArray("embeds");
        List<MessageEmbed> messageEmbeds = new ArrayList<>();
        for (int i = 0; i < dataEmbeds.length(); i++) {
            messageEmbeds.add(EmbedBuilder.fromData(dataEmbeds.getObject(i)).build());
        }

        return new MessageCreateBuilder()
                .addContent(dataObject.getString("content"))
                .addEmbeds(messageEmbeds);
    }
}
