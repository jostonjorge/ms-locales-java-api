package br.com.joston.mslocales.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "br.com.joston.mslocales.v1.repositories")
public class MongoConfig{
    private final String dbConnectionUri;
    private final String dbName;

    public MongoConfig(@Value("${spring.data.mongodb.uri}") String dbUri,@Value("${spring.data.mongodb.database}") String dbName){
        this.dbConnectionUri = dbUri;
        this.dbName = dbName;
    }

    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(dbConnectionUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongo(), dbName);
    }
}
