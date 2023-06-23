package br.com.joston.mslocales.v1.services;

import br.com.joston.mslocales.v1.exceptions.LocaleNotFoundException;
import br.com.joston.mslocales.v1.repositories.CountryRepository;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.repositories.entities.State;
import br.com.joston.mslocales.v1.utils.cache.Cache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service("RetrieveStateServiceV1")
@AllArgsConstructor
public class RetrieveStateService {

    private final CountryRepository countryRepository;
    private final Cache cache;

    public List<State> findAll(String countryCode){
        Optional<Country> country = cache.build(countryCode+"states", builder -> builder
                        .dataSupplier(() -> countryRepository.findByCode(countryCode))
                        .emptyCondition(Optional::isEmpty)
                        .duration(Duration.ofDays(1))
                );
        if(country.isPresent()){
            return country.get().getStates();
        }
        throw new LocaleNotFoundException(String.format("Country no found for code %s",countryCode));
    }

    public State findOne(String countryCode,String stateCode){
        return  cache.build(countryCode+"states"+stateCode,builder -> builder
                .dataSupplier(() -> findStateFromStates(countryCode,stateCode))
                .duration(Duration.ofDays(1))
        );
    }

    private State findStateFromStates(String countryCode,String stateCode){
        List<State> states = findAll(countryCode);
        return states.stream()
                .filter(state -> state.getCode().equalsIgnoreCase(stateCode))
                .findFirst()
                .orElseThrow(() -> new LocaleNotFoundException(stateCode));
    }
}
