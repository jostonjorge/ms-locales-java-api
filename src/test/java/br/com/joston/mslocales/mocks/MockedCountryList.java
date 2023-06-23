package br.com.joston.mslocales.mocks;

import br.com.joston.mslocales.v1.repositories.entities.Country;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class MockedCountryList {
    public static List<Country> getMock() throws URISyntaxException, IOException {
        File file = new File(MockedCountryList.class.getClassLoader().getResource("countries.json").toURI());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return objectMapper.readValue(file, new TypeReference<>() {});
    }
}
