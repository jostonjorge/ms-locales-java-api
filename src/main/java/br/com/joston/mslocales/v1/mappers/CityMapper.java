package br.com.joston.mslocales.v1.mappers;

import br.com.joston.mslocales.v1.dto.CityDto;
import br.com.joston.mslocales.v1.repositories.entities.City;

import java.util.List;
import java.util.stream.Collectors;

public class CityMapper {
    private CityMapper(){}
    public static List<CityDto> toDto(List<City> cities){
        return cities.stream()
                .map(city -> new CityDto(city.getName()))
                .collect(Collectors.toList());
    }

    public static CityDto toDto(City city){
        return new CityDto(city.getName());
    }
}
