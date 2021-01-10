package ru.avantlab.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.avantlab.model.Person;
import ru.avantlab.service.ReactivePersonService;

@Component
public class PersonHandler {

    private final ReactivePersonService service;

    @Autowired
    public PersonHandler(ReactivePersonService service) {
        this.service = service;
    }


    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(Person.class)
                .flatMap(person -> Mono.just(service.update(person)))
                .flatMap(personMono -> ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromProducer(personMono, Person.class)));
    }


    public Mono<ServerResponse> getAll(ServerRequest request) {
        String page = request.queryParam("page").orElse("0");
        int numberInPage = 20;

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(
                        service.findAll().skip(Long.parseLong(page)*numberInPage).limitRequest(numberInPage), Person.class
                ));
    }


    public Mono<ServerResponse> getByEmail(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(
                        service.findByEmail(request.pathVariable("email")),
                        Person.class
                ));
    }



    public Mono<ServerResponse> getFilteredPersons(ServerRequest request) {
        Person filter = new Person();

        MultiValueMap<String, String> map = request.queryParams();

        filter.setFirstName(map.getFirst("firstName"));
        filter.setLastName(map.getFirst("lastName"));
        filter.setEmail(map.getFirst("email"));
        filter.setPhoneNumber(map.getFirst("phoneNumber"));

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        BodyInserters.fromProducer(
                                service.getFilteredPerson(filter), Person.class
                        ));
    }


    public Mono<ServerResponse> delete(ServerRequest request) {

        return Mono.just(service.delete(new Person(request.pathVariable("id"))).subscribe())
                .flatMap(val -> ServerResponse.noContent().build());
    }


    public Mono<ServerResponse> update(ServerRequest request) {

        return request.bodyToMono(Person.class)
                .flatMap(person -> Mono.just(service.update(person)))
                .flatMap(personMono -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromProducer(personMono, Person.class)));
    }
}
