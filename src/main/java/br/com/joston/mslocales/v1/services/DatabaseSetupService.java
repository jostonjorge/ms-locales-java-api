package br.com.joston.mslocales.v1.services;

import br.com.joston.mslocales.v1.repositories.entities.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("DatabaseSetupServiceV1")
@AllArgsConstructor
@Slf4j
public class DatabaseSetupService {
    private final MongoTemplate mongoTemplate;
    private final static URI PATH_TO_COUNTRIES_DATA_JSON;

    private final ObjectMapper objectMapper = new ObjectMapper();

    static {
        Path path = Paths.get("data/json/countries").toAbsolutePath();
        PATH_TO_COUNTRIES_DATA_JSON = path.toUri();
    }

    public void bindLocalesOnDatabase() throws IOException {
        boolean collectionCountriesExists = mongoTemplate.collectionExists("countries");
        if(!collectionCountriesExists){
            List<Country> countries = getJsonLocales();
            mongoTemplate.insertAll(countries);
        }
    }

    private List<Country> getJsonLocales() throws IOException {
        try(Stream<Path> paths = Files.list(Paths.get(PATH_TO_COUNTRIES_DATA_JSON))) {
            return paths
                    .map(Path::toFile)
                    .map(this::getDataFromJsonLocaleFile)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    private Country getDataFromJsonLocaleFile(File file) {
        Country country = null;
        try {
            country = objectMapper.readValue(file,Country.class);
        } catch (IOException e) {
            log.error("Unable read file with locale data",e);
        }
        return country;
    }
}
