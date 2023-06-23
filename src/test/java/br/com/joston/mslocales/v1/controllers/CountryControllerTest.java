package br.com.joston.mslocales.v1.controllers;

import br.com.joston.mslocales.mocks.MockedCountryList;
import br.com.joston.mslocales.v1.exceptions.LocaleNotFoundException;
import br.com.joston.mslocales.v1.mappers.CityMapper;
import br.com.joston.mslocales.v1.mappers.CountryMapper;
import br.com.joston.mslocales.v1.mappers.StateMapper;
import br.com.joston.mslocales.v1.repositories.entities.City;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.repositories.entities.State;
import br.com.joston.mslocales.v1.services.RetrieveCityService;
import br.com.joston.mslocales.v1.services.RetrieveCountryService;
import br.com.joston.mslocales.v1.services.RetrieveStateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class CountryControllerTest {

    private List<Country> mockedCountryList;
    private RetrieveCountryService retrieveCountryService;
    private RetrieveStateService retrieveStateService;
    private RetrieveCityService retrieveCityService;
    private MockMvc mvc;

    @BeforeEach
    public void setup() throws URISyntaxException, IOException {

        mockedCountryList = MockedCountryList.getMock();
        retrieveCountryService = mock(RetrieveCountryService.class);
        retrieveStateService = mock(RetrieveStateService.class);
        retrieveCityService = mock(RetrieveCityService.class);
        CountryController countryController = new CountryController(retrieveCountryService,retrieveStateService,retrieveCityService);

        mvc = MockMvcBuilders.standaloneSetup(countryController).build();
    }

    @Test
    void shouldRetrieveAllCountries() throws Exception {
        final String requestUrl = "/api/v1/countries";
        when(retrieveCountryService.findAll()).thenReturn(mockedCountryList);
        String expectedResponseBody = toJson(CountryMapper.toDto(mockedCountryList));

        MockHttpServletResponse response = mvc.perform(
                        get(requestUrl).accept(MediaType.APPLICATION_JSON)
                ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(expectedResponseBody,response.getContentAsString());
    }

    @Test
    void shouldRetrieveOneCountry() throws Exception {
        final String countryCode = "BRA";
        final String requestUrl = "/api/v1/countries/"+countryCode;
        Country country = mockedCountryList.stream()
                .filter(c -> c.getCode().equals(countryCode))
                .findFirst()
                .orElseThrow();

        when(retrieveCountryService.findOne(countryCode)).thenReturn(country);
        String expectedResponseBody = toJson(CountryMapper.toDto(country));

        MockHttpServletResponse response = mvc.perform(
                        get(requestUrl).accept(MediaType.APPLICATION_JSON)
                ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(expectedResponseBody,response.getContentAsString());
    }

    @Test
    void shouldReturn404ResponseWhenCountryNotFound() throws Exception {
        final String countryCode = "NOT_EXISTS";
        final String requestUrl = "/api/v1/countries/"+countryCode;
        when(retrieveCountryService.findOne(countryCode)).thenThrow(LocaleNotFoundException.class);

        MockHttpServletResponse response = mvc.perform(
                get(requestUrl).accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus());
    }

    @Test
    void shouldRetrieveAllStatesFromCountry() throws Exception {
        final String countryCode = "BRA";
        final String requestUrl = "/api/v1/countries/"+countryCode+"/states";
        List<State> states = mockedCountryList.stream()
                .filter(country -> country.getCode().equals(countryCode))
                .findFirst().orElseThrow()
                .getStates();

        when(retrieveStateService.findAll(countryCode)).thenReturn(states);
        String expectedResponseBody = toJson(StateMapper.toDto(states));

        MockHttpServletResponse response = mvc.perform(
                get(requestUrl).accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(expectedResponseBody,response.getContentAsString());
    }

    @Test
    void shouldReturnTheStateSearched() throws Exception {
        final String countryCode = "BRA";
        final String stateCode = "MG";
        final String requestUrl = "/api/v1/countries/"+countryCode+"/states/"+stateCode;
        State state = mockedCountryList.stream()
                .filter(country -> country.getCode().equals(countryCode))
                .findFirst().orElseThrow()
                .getStates().stream()
                .filter(s -> s.getCode().equals("MG"))
                .findFirst().orElseThrow();
        when(retrieveStateService.findOne(countryCode,stateCode)).thenReturn(state);
        String expectedResponseBody = toJson(StateMapper.toDto(state));

        MockHttpServletResponse response = mvc.perform(
                get(requestUrl).accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(expectedResponseBody,response.getContentAsString());
    }

    @Test
    void shouldReturn404ResponseWhenStateNotFound() throws Exception {
        final String requestUrl = "/api/v1/countries/BRA/states/NOT_EXISTS";
        when(retrieveStateService.findOne(any(),any())).thenThrow(LocaleNotFoundException.class);

        MockHttpServletResponse response = mvc.perform(
                get(requestUrl)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus());
    }

    @Test
    void shouldRetrieveAllCitiesFromState() throws Exception {
        final String countryCode = "BRA";
        final String stateCode = "MG";
        final String requestUrl = "/api/v1/countries/"+countryCode+"/states/"+stateCode+"/cities";

        List<City> cities = mockedCountryList.stream()
                .filter(country -> country.getCode().equals(countryCode))
                .findFirst().orElseThrow()
                .getStates().stream()
                .filter(state -> state.getCode().equals("MG"))
                .findFirst().orElseThrow()
                .getCities();

        when(retrieveCityService.findAll(any(),any())).thenReturn(cities);

        String expectedResponseBody = toJson(CityMapper.toDto(cities));

        MockHttpServletResponse response = mvc.perform(
                get(requestUrl).accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(expectedResponseBody,response.getContentAsString());
    }

    private String toJson(Object value) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(value);
    }
}