package br.com.joston.mslocales.v1.repositories;

import br.com.joston.mslocales.v1.repositories.entities.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("CountryRepositoryV1")
public interface CountryRepository extends MongoRepository<Country,String> {
    List<Country> findAll();

    @Query(value = "{ 'code' : ?0 }")
    Optional<Country> findByCode(String code);
}
