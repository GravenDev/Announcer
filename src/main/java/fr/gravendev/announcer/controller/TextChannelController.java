package fr.gravendev.announcer.controller;

import fr.gravendev.announcer.service.TextChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guilds/{guildId}/channels/{channelId}")
@RequiredArgsConstructor
public class TextChannelController {

    private final TextChannelService textChannelService;

    @PostMapping("/send")
    public boolean send(@PathVariable long guildId,
                        @PathVariable long channelId,
                        @RequestBody String messageData) {
        return textChannelService.send(guildId, channelId, messageData);
    }
}
