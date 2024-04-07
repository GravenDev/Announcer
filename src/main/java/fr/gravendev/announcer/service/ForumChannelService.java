package fr.gravendev.announcer.service;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForumChannelService {

    private final JDA jda;
    private final DeserializeService deserializeService;

    private Optional<ForumChannel> getForumChannel(long guildId, long channelId) {
        return Optional.ofNullable(jda.getGuildById(guildId))
                .map(guild -> guild.getForumChannelById(channelId));
    }

    public boolean createPost(long guildId, long channelId, String messageData) {
        DataObject dataObject = deserializeService.fromJson(messageData);
        String forumName = dataObject.getString("thread_name", "name");
        List<EmbedBuilder> embedBuilders = deserializeService.getEmbeds(dataObject).toList();

        MessageCreateData messageCreateData = new MessageCreateBuilder()
                .addContent(dataObject.getString("content", ""))
                .addEmbeds(embedBuilders.stream().map(EmbedBuilder::build).toList())
                .build();

        getForumChannel(guildId, channelId)
                .ifPresent(forumChannel -> forumChannel
                        .createForumPost(forumName, messageCreateData)
                        .queue());
        return true;
    }
}
