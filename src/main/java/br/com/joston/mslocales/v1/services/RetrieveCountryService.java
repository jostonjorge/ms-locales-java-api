package br.com.joston.mslocales.v1.services;

import br.com.joston.mslocales.v1.exceptions.LocaleNotFoundException;
import br.com.joston.mslocales.v1.repositories.CountryRepository;
import br.com.joston.mslocales.v1.repositories.entities.Country;
import br.com.joston.mslocales.v1.utils.cache.Cache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service("RetrieveCountriesServiceV1")
@AllArgsConstructor
public class RetrieveCountryService {
    private final CountryRepository countryRepository;
    private final Cache cache;

    public List<Country> findAll(){
        return cache.build("countries", builder -> builder
                .dataSupplier(countryRepository::findAll)
                .emptyCondition(List::isEmpty)
                .duration(Duration.ofDays(1))
        );
    }

    public Country findOne(String countryCode){
        Optional<Country> country =  cache.build("country"+countryCode,builder ->
                builder.dataSupplier(()-> countryRepository.findByCode(countryCode))
                        .emptyCondition(Optional::isEmpty)
                        .duration(Duration.ofDays(1))
        );
        if(country.isPresent()) {
            return country.get();
        }
        throw new LocaleNotFoundException(String.format("Country not found for code %s", countryCode));
    }
}
