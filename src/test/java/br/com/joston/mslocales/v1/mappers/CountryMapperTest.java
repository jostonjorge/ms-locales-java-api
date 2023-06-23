package br.com.joston.mslocales.v1.mappers;

import br.com.joston.mslocales.mocks.MockedCountryList;
import br.com.joston.mslocales.v1.dto.CountryDto;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryMapperTest {

    private static List<Country> mockedCountryList;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException {
        mockedCountryList = MockedCountryList.getMock();
    }

    @Test
    void testCountryListToDto() {
        List<CountryDto> expected = List.of(new CountryDto("BRA","Brazil"));
        assertEquals(expected,CountryMapper.toDto(mockedCountryList));
    }

    @Test
    void testCountryToDto() {
        CountryDto expected = new CountryDto("BRA","Brazil");
        assertEquals(expected,CountryMapper.toDto(mockedCountryList.stream()
                .filter(country -> country.getCode().equals("BRA"))
                .findFirst().orElseThrow())
        );
    }
}