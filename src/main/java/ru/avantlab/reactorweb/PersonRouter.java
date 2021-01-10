package ru.avantlab.reactorweb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import ru.avantlab.handlers.PersonHandler;

@Configuration
public class PersonRouter {

    private final PersonHandler handler;

    @Autowired
    public PersonRouter(PersonHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> routePerson() {
        return RouterFunctions
                .route(RequestPredicates.GET("/email/{email}"), handler::getByEmail)
                .andRoute(RequestPredicates.GET("/"), handler::getAll)
                .andRoute(RequestPredicates.GET("/filter"), handler::getFilteredPersons)
                .andRoute(RequestPredicates.DELETE("/delete/{email}"), handler::delete)
                .andRoute(RequestPredicates.PUT("/update")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::update)
                .andRoute(RequestPredicates.POST("/add")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::save);
    }
}
