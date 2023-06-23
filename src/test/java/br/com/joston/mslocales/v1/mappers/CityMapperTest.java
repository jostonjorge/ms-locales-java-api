package br.com.joston.mslocales.v1.mappers;

import br.com.joston.mslocales.v1.dto.CityDto;
import br.com.joston.mslocales.v1.repositories.entities.City;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityMapperTest {

    @Test
    void toListCityToDto() {
        List<CityDto> expected = List.of(new CityDto("Belo Horizonte"));
        City city = new City();
        city.setName("Belo Horizonte");
        List<City> cities = List.of(city);

        assertEquals(expected,CityMapper.toDto(cities));
    }


    @Test
    void testCityToDto() {
        CityDto expected = new CityDto("Belo Horizonte");
        City city = new City();
        city.setName("Belo Horizonte");

        assertEquals(expected,CityMapper.toDto(city));
    }
}