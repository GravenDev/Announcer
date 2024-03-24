package fr.gravendev.announcer.configuration;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JDAConfig {

    @Bean
    public JDA jda(@Value("${bot.token}") String token) {
        return JDABuilder.create(token, List.of())
                .build();
    }
}
