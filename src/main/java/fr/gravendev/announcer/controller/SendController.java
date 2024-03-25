package fr.gravendev.announcer.controller;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/guilds/{guildId}")
@RequiredArgsConstructor
public class SendController {

    private final JDA jda;

    @PostMapping("/channels/{channelId}/send")
    public boolean sendToChannel(@PathVariable long guildId, @PathVariable long channelId, @RequestBody String messageData) {
        DataObject dataObject = DataObject.fromJson(messageData);

        DataArray dataEmbeds = dataObject.getArray("embeds");
        List<MessageEmbed> messageEmbeds = new ArrayList<>();
        for (int i = 0; i < dataEmbeds.length(); i++) {
            messageEmbeds.add(EmbedBuilder.fromData(dataEmbeds.getObject(i)).build());
        }

        jda.getGuildById(guildId)
                .getTextChannelById(channelId)
                .sendMessage(new MessageCreateBuilder()
                        .addContent(dataObject.getString("content"))
                        .addEmbeds(messageEmbeds)
                        .build())
                .queue();
        return true;
    }
}
