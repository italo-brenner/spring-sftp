package br.com.italo.springftp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class ErrorChannelConfig {

    @Bean(name = "errorChannel")
    public MessageChannel errorChannel() {
        return new PublishSubscribeChannel(); // Cria um canal para mensagens de erro
    }

    @Bean
    @ServiceActivator(inputChannel = "errorChannel")
    public MessageHandler errorHandler() {
        return message -> {
            System.err.println("Erro no fluxo de integração: " + message.getPayload());
        };
    }

}
