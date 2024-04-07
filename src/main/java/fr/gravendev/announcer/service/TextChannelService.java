package fr.gravendev.announcer.service;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TextChannelService {

    private final JDA jda;
    private final DeserializeService deserializeService;

    private Optional<TextChannel> getTextChannel(long guildId, long channelId) {
        return Optional.ofNullable(jda.getGuildById(guildId))
                .map(guild -> guild.getTextChannelById(channelId));
    }

    private MessageCreateBuilder createMessage(String messageData) {
        DataObject dataObject = deserializeService.fromJson(messageData);
        String content = dataObject.getString("content", "");
        Stream<EmbedBuilder> embedBuilders = deserializeService.getEmbeds(dataObject);
        List<FileUpload> fileUploads = new ArrayList<>();
        embedBuilders = embedBuilders.map(embedBuilder -> {
            dataObject.optObject("thumbnail").ifPresent(thumbnail -> {
                try {
                    FileUpload fileUpload = FileUpload.fromData(new BufferedInputStream(new URI("https://ftmnet.com/img/graven_logo.png").toURL().openStream()), "thumbnail.png");
                    fileUploads.add(fileUpload);
                    embedBuilder.setThumbnail("attachment://thumbnail.png");
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
            return embedBuilder;
        });

        return new MessageCreateBuilder()
                .addContent(content)
                .addFiles(fileUploads)
                .addEmbeds(embedBuilders.map(EmbedBuilder::build).toList());
    }

    public boolean send(long guildId, long channelId, String messageData) {
        try (MessageCreateData messageCreateData = createMessage(messageData).build()) {
            getTextChannel(guildId, channelId)
                    .orElseThrow()
                    .sendMessage(messageCreateData)
                    .queue();
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    public boolean edit(long guildId, long channelId, long messageId, String messageData) {
        try (MessageEditData messageEditData = MessageEditBuilder.fromCreateData(createMessage(messageData).build()).build()) {
            getTextChannel(guildId, channelId)
                    .orElseThrow()
                    .editMessageById(messageId, messageEditData);
        }
        return true;
    }
}
