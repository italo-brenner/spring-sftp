package br.com.italo.springftp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DefaultHeaderChannelRegistry;
import org.springframework.integration.support.channel.HeaderChannelRegistry;

@Configuration
public class HeaderChannelRegistryConfig {

    @Bean(name = "integrationHeaderChannelRegistry")
    public HeaderChannelRegistry headerChannelRegistry() {
        // Você pode personalizar o registro, se necessário
        return new DefaultHeaderChannelRegistry();
    }
}