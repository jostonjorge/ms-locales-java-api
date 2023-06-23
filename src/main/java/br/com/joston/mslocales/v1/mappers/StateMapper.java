package br.com.joston.mslocales.v1.mappers;

import br.com.joston.mslocales.v1.dto.StateDto;
import br.com.joston.mslocales.v1.repositories.entities.State;

import java.util.List;
import java.util.stream.Collectors;

public class StateMapper {
    private StateMapper(){}
    public static List<StateDto> toDto(List<State> states){
        return states.stream()
                .map(state -> new StateDto(state.getCode(),state.getName()))
                .collect(Collectors.toList());
    }

    public static StateDto toDto(State state){
        return new StateDto(state.getCode(),state.getName());
    }
}
