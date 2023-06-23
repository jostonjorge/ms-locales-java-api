package br.com.joston.mslocales.config;

import br.com.joston.mslocales.v1.services.DatabaseSetupService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class MongoInit {
    private DatabaseSetupService databaseSetupService;

    @PostConstruct
    void initializeDatabase() throws IOException {
        databaseSetupService.bindLocalesOnDatabase();
    }
}
