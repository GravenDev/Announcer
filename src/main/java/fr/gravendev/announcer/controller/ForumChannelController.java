package fr.gravendev.announcer.controller;

import fr.gravendev.announcer.service.ForumChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guilds/{guildId}/channels/{channelId}")
@RequiredArgsConstructor
public class ForumChannelController {

    private final ForumChannelService forumChannelService;

    @PostMapping("/post")
    public boolean createPost(@PathVariable long guildId,
                              @PathVariable long channelId,
                              @RequestBody String messageData) {
        return forumChannelService.createPost(guildId, channelId, messageData);
    }
}
