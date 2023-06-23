package br.com.joston.mslocales.v1.mappers;

import br.com.joston.mslocales.mocks.MockedCountryList;
import br.com.joston.mslocales.v1.dto.StateDto;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.repositories.entities.State;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StateMapperTest {

    private static List<Country> mockedCountryList;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException {
        mockedCountryList = MockedCountryList.getMock();
    }

    @Test
    void testStateListToDto() {
        List<StateDto> expected = List.of(new StateDto("MG","Minas Gerais"));
        assertEquals(expected,StateMapper.toDto(getStateListFromBrazilAndFilterToMgOnly()));
    }

    private List<State> getStateListFromBrazilAndFilterToMgOnly(){
        return mockedCountryList.stream()
                .filter(country -> country.getCode().equals("BRA"))
                .findFirst()
                .orElseThrow()
                .getStates()
                .stream().filter(state -> state.getCode().equals("MG"))
                .collect(Collectors.toList());
    }

    @Test
    void testStateToDto() {
        StateDto expected = new StateDto("MG","Minas Gerais");
        assertEquals(expected,StateMapper.toDto(getStateMG()));
    }

    private State getStateMG(){
        return getStateListFromBrazilAndFilterToMgOnly().get(0);
    }
}