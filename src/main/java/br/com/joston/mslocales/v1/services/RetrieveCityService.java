package br.com.joston.mslocales.v1.services;

import br.com.joston.mslocales.v1.repositories.entities.City;
import br.com.joston.mslocales.v1.utils.cache.Cache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service("RetrieveCityServiceV1")
@AllArgsConstructor
public class RetrieveCityService {
    private final RetrieveStateService retrieveStateService;
    private final Cache cache;

    public List<City> findAll(String countryCode,String stateCode){
        return cache.build(countryCode+"states"+countryCode+"cities",builder -> builder
                .dataSupplier(() -> retrieveStateService.findOne(countryCode,stateCode).getCities())
                .emptyCondition(List::isEmpty)
                .duration(Duration.ofDays(1))
        );
    }
}
