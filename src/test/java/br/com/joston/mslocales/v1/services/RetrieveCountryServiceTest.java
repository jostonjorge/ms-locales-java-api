package br.com.joston.mslocales.v1.services;

import br.com.joston.mslocales.mocks.MockedCountryList;
import br.com.joston.mslocales.v1.exceptions.LocaleNotFoundException;
import br.com.joston.mslocales.v1.repositories.CountryRepository;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.utils.cache.Cache;
import br.com.joston.mslocales.v1.utils.cache.MemoryCache;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class RetrieveCountryServiceTest {
    private static RetrieveCountryService service;
    private static List<Country> mockedCountryList;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException {
        mockedCountryList = MockedCountryList.getMock();

        CountryRepository countryRepository = mock(CountryRepository.class);
        when(countryRepository.findAll()).thenReturn(mockedCountryList);
        when(countryRepository.findByCode("BRA")).thenReturn(mockedCountryList.stream()
                .filter(country -> country.getCode().equals("BRA"))
                .findFirst());

        Cache cache = spy(new MemoryCache());

        service = new RetrieveCountryService(countryRepository,cache);
    }

    @Test
    void shouldReturnAllCountries(){
        List<Country> expected = mockedCountryList;
        assertEquals(expected,service.findAll());
    }

    @Test
    void shouldReturnTheCountrySearched(){
        Country expected = mockedCountryList.stream()
                .filter(country -> country.getCode().equals("BRA"))
                .findFirst()
                .orElseThrow();
        assertEquals(expected,service.findOne("BRA"));
    }

    @Test
    void shouldThrowsWhenCountryNotFound(){
        assertThrows(LocaleNotFoundException.class,() -> service.findOne("NOT_EXISTS"));
    }
}