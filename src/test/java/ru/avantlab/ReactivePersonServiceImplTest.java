package ru.avantlab;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;
import ru.avantlab.model.Person;
import ru.avantlab.repository.ReactivePersonRepository;
import ru.avantlab.service.ReactivePersonService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ReactivePersonServiceImplTest {

    @Autowired
    ReactivePersonService service;

    @Autowired
    private ReactivePersonRepository repository;

    private final String firstName = "Ivan";
    private final String lastName = "Ivanov";
    private final String email = "ivanov@mail.ru";
    private final String phoneNumber = "7-123-1888926";

    @BeforeEach
    public void fillDataBase() {
        List<Person> persons = Arrays.asList(
                new Person(firstName, lastName, email, phoneNumber),
                new Person("Petr", "Petrov", "petrov@mail.ru", "7-124-1888926"),
                new Person("Ivan", "Botalov", "botalov@mail.ru", "7-125-1888926")
        );

        repository.saveAll(persons).blockLast();
    }

    @AfterEach
    public void clearDataBase() {
        repository.deleteAll().block();
    }

    @Test
    public void save() {
        String email1 = "person@mail.ru";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "7-123-1234567";

        service.save(
                new Person(firstName, lastName, email1, phoneNumber)).block();

        StepVerifier
                .create(service.findByEmail(email1))
                .assertNext(person -> assertEquals(firstName, person.getFirstName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findByEmail() {
        StepVerifier
                .create(service.findByEmail(email))
                .assertNext(person -> assertEquals(lastName, person.getLastName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findAll() {
        StepVerifier
                .create(service.findAll())
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    public void getFilteredPerson() {
        Person filter = new Person();
        filter.setFirstName(firstName);

        StepVerifier
                .create(service.getFilteredPerson(filter))
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void delete() {
        Person person = service.findByEmail(email).block();

        service.delete(person).block();

        StepVerifier
                .create(service.findAll())
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void update() {
        String sergey = "Sergey";
        Person person = service.findByEmail(email).block();

        person.setFirstName(sergey);

        service.update(person).block();

        StepVerifier
                .create(service.findByEmail(email))
                .assertNext(actual ->
                {
                    assertEquals(sergey, actual.getFirstName());
                    assertEquals(lastName, actual.getLastName());
                })
                .expectComplete()
                .verify();
    }
}
