package br.com.joston.mslocales.v1.controllers;

import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.services.RetrieveCountryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locales")
@AllArgsConstructor
public class LocaleController {
    private final RetrieveCountryService retrieveCountryService;

    @GetMapping(produces={"application/json; charset=UTF-8"})
    @ResponseBody()
    public List<Country> retrieveAllLocales(){
        return retrieveCountryService.findAll();
    }

    @GetMapping(path = "/{countryCode}",produces={"application/json; charset=UTF-8"})
    @ResponseBody()
    public Country retrieveAllByCountry(@PathVariable("countryCode") String countryCode){
        return retrieveCountryService.findOne(countryCode);
    }
}
