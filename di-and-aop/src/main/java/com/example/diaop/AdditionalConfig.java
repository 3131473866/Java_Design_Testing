package com.example.diaop;

import com.example.diaop.interfaces.*;
import com.example.diaop.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.example.diaop.DiAopApplication.TASK_2_ENV;

@Profile(TASK_2_ENV)
@Configuration
public class AdditionalConfig {

    @Bean
    public Authentication authentication() {
        return new AuthenticationTSL();
    }

    @Bean
    public FileSystem fileSystem() {
        return new FileSystemNFS();
    }

    @Bean
    public Connection connection() {
        return new ConnectionJDBC();
    }

    @Bean
    public Frontend frontend(Authentication authentication) {
        return new FrontendGWT(authentication);
    }

    @Bean
    public Middleware middleware() {
        return new MiddlewareJBoss();
    }

    @Bean
    public Persistence persistence(FileSystem fileSystem, Connection connection) {
        return new PersistenceOracle(fileSystem, connection);
    }
}
