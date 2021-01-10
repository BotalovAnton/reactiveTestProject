package ru.avantlab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avantlab.model.Person;
import ru.avantlab.repository.ReactivePersonRepository;

@Service
public class ReactivePersonServiceImpl implements ReactivePersonService {
    private final ReactivePersonRepository repository;

    @Autowired
    public ReactivePersonServiceImpl(ReactivePersonRepository repository) {
        this.repository = repository;
    }

    public Mono<Person> save(Person person) {
        return repository.save(person);
    }

    public Mono<Person> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Flux<Person> findAll() {

        return repository.findAll(Sort.by("firstName", "lastName")).publish().autoConnect();
    }

    public Flux<Person> getFilteredPerson(Person person) {


        return repository.findAll(Example.of(person), Sort.by("firstName", "lastName"));
    }

    public Mono<Person> update(Person person) {
        return repository.save(person);
    }

    public Mono<Void> delete(Person person) {
        return repository.delete(person);
    }
}
