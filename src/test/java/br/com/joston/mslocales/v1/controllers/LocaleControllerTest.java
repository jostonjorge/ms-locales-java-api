package br.com.joston.mslocales.v1.controllers;

import br.com.joston.mslocales.mocks.MockedCountryList;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.services.RetrieveCountryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class LocaleControllerTest {
    private List<Country> mockedCountryList;
    private RetrieveCountryService retrieveCountryService;

    private MockMvc mvc;

    @BeforeEach
    public void setup() throws URISyntaxException, IOException {
        mockedCountryList = MockedCountryList.getMock();
        retrieveCountryService = mock(RetrieveCountryService.class);
        LocaleController countryController = new LocaleController(retrieveCountryService);
        mvc = MockMvcBuilders.standaloneSetup(countryController).build();
    }

    @Test
    void shouldRetrieveAllCountries() throws Exception {
        final String requestUrl = "/api/v1/locales";
        when(retrieveCountryService.findAll()).thenReturn(mockedCountryList);
        String expectedResponseBody = toJson(mockedCountryList);

        MockHttpServletResponse response = mvc.perform(
                get(requestUrl).accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(expectedResponseBody,response.getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldRetrieveCountryAndYourLocales() throws Exception {
        final String countryCode = "BRA";
        final String requestUrl = "/api/v1/locales/"+countryCode;
        Country country = mockedCountryList.stream()
                .filter(c -> c.getCode().equals(countryCode))
                .findFirst()
                .orElseThrow();
        when(retrieveCountryService.findOne(countryCode)).thenReturn(country);
        String expectedResponseBody = toJson(country);

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