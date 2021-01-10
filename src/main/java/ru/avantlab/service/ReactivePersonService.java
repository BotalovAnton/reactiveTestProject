package ru.avantlab.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avantlab.model.Person;

public interface ReactivePersonService {

    Mono<Person> findByEmail(String email);

    Flux<Person> findAll();

    Flux<Person> getFilteredPerson(Person person);

    Mono<Void> delete(Person person);

    Mono<Person> update(Person person);

    Mono<Person> save(Person person);
}
