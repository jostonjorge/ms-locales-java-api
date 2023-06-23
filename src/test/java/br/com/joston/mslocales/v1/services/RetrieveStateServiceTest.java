package br.com.joston.mslocales.v1.services;

import br.com.joston.mslocales.mocks.MockedCountryList;
import br.com.joston.mslocales.v1.exceptions.LocaleNotFoundException;
import br.com.joston.mslocales.v1.repositories.CountryRepository;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.repositories.entities.State;
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

class RetrieveStateServiceTest {

    private static List<Country> mockedCountryList;
    private static RetrieveStateService service;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException {
        mockedCountryList = MockedCountryList.getMock();

        CountryRepository countryRepository = mock(CountryRepository.class);
        when(countryRepository.findAll()).thenReturn(mockedCountryList);
        when(countryRepository.findByCode("BRA")).thenReturn(mockedCountryList.stream()
                .filter(country -> country.getCode().equals("BRA"))
                .findFirst());

        Cache cache = spy(new MemoryCache());

        service = new RetrieveStateService(countryRepository,cache);
    }

    @Test
    void shouldReturnAllStatesFromCountry() {
        List<State> expected = mockedCountryList.stream()
                .filter(country -> country.getCode().equals("BRA"))
                .findFirst()
                .orElseThrow()
                .getStates();
        assertEquals(expected,service.findAll("BRA"));
    }

    @Test
    void shouldThrowsExceptionWhenCountryNotFound(){
        assertThrows(LocaleNotFoundException.class,() -> service.findAll("NOT_EXISTS"));
    }

    @Test
    void shouldReturnTheStateSearched() {
        State expected = mockedCountryList.stream()
                .filter(country -> country.getCode().equals("BRA"))
                .findFirst()
                .orElseThrow()
                .getStates().stream()
                .filter(state -> state.getCode().equals("MG"))
                .findFirst().orElseThrow();
        assertEquals(expected,service.findOne("BRA","MG"));
    }

    @Test
    void shouldThrowsExceptionWhenStateNotFound(){
        assertThrows(LocaleNotFoundException.class,() -> service.findOne("BRA","NOT_EXIST"));
    }
}