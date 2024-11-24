package br.com.italo.springftp.infrastructure.storage.config;

import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageChannel;

@Configuration
@IntegrationComponentScan
public class SftpIntegrationConfig {

    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost("0.0.0.0");
        factory.setPort(2222);
        factory.setUser("sftpuser");
        factory.setPassword("sftppassword");
        factory.setAllowUnknownKeys(true);
//        factory.setTestSession(true);
        CachingSessionFactory<SftpClient.DirEntry> result = new CachingSessionFactory<>(factory);
        result.setTestSession(true);
        return result;
    }

    @Bean
    public MessageChannel sftpChannel() {
        return new DirectChannel();
    }

//    @Bean
//    public IntegrationFlow sftpInboundFlow() {
//        return IntegrationFlow
//                .from(Sftp.inboundAdapter(sftpSessionFactory())
//                                .preserveTimestamp(true)
//                                .remoteDirectory("//")
//                                .regexFilter(".*\\.txt$")
//                                .localFilenameExpression("#this.toUpperCase() + '.a'")
//                                .localDirectory(new File("sftp-inbound")),
//                        e -> e.id("sftpInboundAdapter")
//                                .autoStartup(true)
//                                .poller(Pollers.fixedDelay(5000)))
//                .handle(m -> System.out.println(m.getPayload()))
//                .get();
//    }

}