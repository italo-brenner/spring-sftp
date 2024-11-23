package br.com.italo.springftp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageChannel;

@Configuration
@IntegrationComponentScan
public class SftpIntegrationConfig {

    @Bean
    public DefaultSftpSessionFactory sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(22);
        factory.setUser("myuser");
        factory.setPassword("mypassword");
        factory.setAllowUnknownKeys(true);  // Para permitir hosts não conhecidos
        return factory;
    }

    @Bean
    public MessageChannel sftpChannel() {
        return new DirectChannel();
    }

}