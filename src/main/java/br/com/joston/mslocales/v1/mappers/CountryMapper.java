package br.com.joston.mslocales.v1.mappers;

import br.com.joston.mslocales.v1.dto.CountryDto;
import br.com.joston.mslocales.v1.repositories.entities.Country;

import java.util.List;
import java.util.stream.Collectors;

public class CountryMapper {
    private CountryMapper(){}
    public static List<CountryDto> toDto(List<Country> countries){
        return countries.stream()
                .map(country -> new CountryDto(country.getCode(),country.getName()))
                .collect(Collectors.toList());
    }

    public static CountryDto toDto(Country country){
        return new CountryDto(country.getCode(),country.getName());
    }
}
