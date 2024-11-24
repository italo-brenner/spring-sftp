package br.com.italo.springftp;

import br.com.italo.springftp.infrastructure.storage.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SpringFtpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFtpApplication.class, args);
	}

}
