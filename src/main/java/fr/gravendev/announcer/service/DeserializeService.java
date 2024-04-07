package fr.gravendev.announcer.service;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class DeserializeService {

    public DataObject fromJson(String messageData) {
        return DataObject.fromJson(messageData);
    }

    public Stream<EmbedBuilder> getEmbeds(DataObject dataObject) {
        return dataObject.getArray("embeds")
                .stream(DataArray::getObject)
                .map(EmbedBuilder::fromData);
    }
}
