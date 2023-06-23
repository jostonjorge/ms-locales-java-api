package br.com.joston.mslocales.v1.controllers;

import br.com.joston.mslocales.v1.dto.CityDto;
import br.com.joston.mslocales.v1.dto.CountryDto;
import br.com.joston.mslocales.v1.dto.StateDto;
import br.com.joston.mslocales.v1.mappers.CityMapper;
import br.com.joston.mslocales.v1.mappers.CountryMapper;
import br.com.joston.mslocales.v1.mappers.StateMapper;
import br.com.joston.mslocales.v1.repositories.entities.City;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.repositories.entities.State;
import br.com.joston.mslocales.v1.services.RetrieveCityService;
import br.com.joston.mslocales.v1.services.RetrieveCountryService;
import br.com.joston.mslocales.v1.services.RetrieveStateService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@AllArgsConstructor
public class CountryController {
    private final RetrieveCountryService retrieveCountryService;
    private final RetrieveStateService retrieveStateService;
    private final RetrieveCityService retrieveCityService;

    @GetMapping(produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public List<CountryDto> findCountries(){
        return CountryMapper.toDto(retrieveCountryService.findAll());
    }

    @GetMapping(path="/{countryCode}",produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public CountryDto findCountry(@PathVariable("countryCode") String countryCode){
        Country country = retrieveCountryService.findOne(countryCode);
        return CountryMapper.toDto(country);
    }

    @GetMapping(path="/{countryCode}/states",produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public List<StateDto> findStates(@PathVariable("countryCode") String countryCode){
        List<State> states = retrieveStateService.findAll(countryCode);
        return StateMapper.toDto(states);
    }

    @GetMapping(path="/{countryCode}/states/{stateCode}",produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public StateDto findState(@PathVariable("countryCode") String countryCode,@PathVariable("stateCode") String stateCode){
        State state = retrieveStateService.findOne(countryCode,stateCode);
        return StateMapper.toDto(state);
    }

    @GetMapping(path = "/{countryCode}/states/{stateCode}/cities",produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public List<CityDto> findCities(@PathVariable("countryCode") String countryCode,@PathVariable("stateCode") String stateCode){
            List<City> cities = retrieveCityService.findAll(countryCode,stateCode);
            return CityMapper.toDto(cities);
    }
}
