package ru.avantlab.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.avantlab.model.Person;

@Repository
public interface ReactivePersonRepository extends ReactiveMongoRepository<Person, String>  {
    Mono<Person> findByEmail(String email);
}
