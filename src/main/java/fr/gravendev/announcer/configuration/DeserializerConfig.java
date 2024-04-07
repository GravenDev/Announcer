package fr.gravendev.announcer.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.gravendev.announcer.configuration.deserializer.MessageCreateBuilderDeserializer;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeserializerConfig {

    @Bean
    public Module messageCreateBuilderDeserializerModule(MessageCreateBuilderDeserializer messageCreateBuilderDeserializer) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MessageCreateBuilder.class, messageCreateBuilderDeserializer);
        return module;
    }
}
