package br.com.joston.mslocales.v1.services;

import br.com.joston.mslocales.mocks.MockedCountryList;
import br.com.joston.mslocales.v1.repositories.CountryRepository;
import br.com.joston.mslocales.v1.repositories.entities.City;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.utils.cache.Cache;
import br.com.joston.mslocales.v1.utils.cache.MemoryCache;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class RetrieveCityServiceTest {

    private static List<Country> mockedCountryList;
    private static RetrieveCityService service;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException {
        mockedCountryList = MockedCountryList.getMock();

        CountryRepository countryRepository = mock(CountryRepository.class);
        when(countryRepository.findAll()).thenReturn(mockedCountryList);
        when(countryRepository.findByCode("BRA")).thenReturn(mockedCountryList.stream()
                .filter(country -> country.getCode().equals("BRA"))
                .findFirst());

        Cache cache = spy(new MemoryCache());

        RetrieveStateService retrieveStateService = new RetrieveStateService(countryRepository,cache);
        service = new RetrieveCityService(retrieveStateService,cache);
    }

    @Test
    void shouldReturnAllCitiesFromState() {
        List<City> expected = mockedCountryList.stream()
                .filter(country -> country.getCode().equals("BRA"))
                .findFirst().orElseThrow()
                .getStates().stream()
                .filter(state -> state.getCode().equals("MG"))
                .findFirst().orElseThrow().
                getCities();

        assertEquals(expected,service.findAll("BRA","MG"));
    }
}