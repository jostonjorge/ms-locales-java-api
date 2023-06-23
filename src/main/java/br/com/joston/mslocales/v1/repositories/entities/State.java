package br.com.joston.mslocales.v1.repositories.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class State {
    private String code;
    private String name;
    private List<City> cities;
}
