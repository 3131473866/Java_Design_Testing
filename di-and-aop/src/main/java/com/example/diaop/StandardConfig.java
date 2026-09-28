package com.example.diaop;

import com.example.diaop.interfaces.*;
import com.example.diaop.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.example.diaop.DiAopApplication.TASK_1_ENV;

@Profile(TASK_1_ENV)
@Configuration
public class StandardConfig {

    @Bean
    public Authentication authentication() {
        return new AuthenticationSSL();
    }

    @Bean
    public FileSystem fileSystem() {
        return new FileSystemNTFS();
    }

    @Bean
    public Connection connection() {
        return new ConnectionPooled();
    }

    @Bean
    public Frontend frontend(Authentication authentication) {
        return new FrontendHTML(authentication);
    }

    @Bean
    public Middleware middleware() {
        return new MiddlewareTomcat();
    }

    @Bean
    public Persistence persistence(FileSystem fileSystem, Connection connection) {
        return new PersistenceMySQL(fileSystem, connection);
    }
}
