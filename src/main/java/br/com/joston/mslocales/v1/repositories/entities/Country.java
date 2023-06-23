package br.com.joston.mslocales.v1.repositories.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("countries")
@Getter
@Setter
@NoArgsConstructor
public class Country {
    private String code;
    private String name;
    private List<State> states;
}
