package ru.avantlab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.avantlab.model.Person;
import ru.avantlab.repository.ReactivePersonRepository;

import java.util.Arrays;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class PersonRouterTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ReactivePersonRepository repository;

    private final String firstName = "Ivan";
    private final String lastName = "Ivanov";
    private final String email = "ivanov@mail.ru";
    private final String phoneNumber = "7-123-1888926";

    @BeforeEach
    public void fillDataBase() {
        repository.deleteAll().block();

        List<Person> persons = Arrays.asList(
                new Person(firstName, lastName, email, phoneNumber),
                new Person("Petr", "Petrov", "petrov@mail.ru", "7-124-1888926"),
                new Person("Ivan", "Botalov", "botalov@mail.ru", "7-125-1888926")
        );

        repository.saveAll(persons).blockLast();

    }

    @Test
    public void getAll() {
        webClient.get().uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Person.class).hasSize(3);
    }

    @Test
    public void getByEmail() {
        webClient.get().uri(String.format("/email/%s", email))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.email").isEqualTo(email);

    }

    @Test
    public void getFilteredPerson() {
        Person filter = new Person();
        filter.setFirstName(firstName);

        webClient.get().uri("/filter/?firstName=Ivan")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Person.class)
                .hasSize(2);
    }

    @Test
    public void delete() {
        webClient.delete().uri(String.format("/delete/%s", email))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void save() {
        Person person = new Person("Egor", "egorov", "egorov@mail.ru", "7-313-1234567");

        webClient.post().uri("/add")
                .bodyValue(person)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.firstName").isNotEmpty()
                .jsonPath("$.lastName").isNotEmpty();

    }

    @Test
    public void update() {
        Person person = new Person("Egor", "egorov", "egorov@mail.ru", "7-323-1234567");

        webClient.put().uri("/update")
                .bodyValue(person)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.phoneNumber").isEqualTo("7-323-1234567");
    }
}
