package ru.avantlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.avantlab.model.PersonDto;
import ru.avantlab.repository.specification.PersonFilter;
import ru.avantlab.service.PersonService;
import ru.avantlab.utility.validation.GroupUpdated;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public Page<PersonDto> getAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public PersonDto getById(@PathVariable("id") long id) {
        return service.findById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDto add(@Validated @RequestBody PersonDto personDto) {
        return service.save(personDto);
    }

    @PutMapping("/update")
    public void update(@Validated(value = GroupUpdated.class) @RequestBody PersonDto personDto) {
        service.update(personDto);
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        service.delete(new PersonDto(id));
    }

    @GetMapping("/filtered")
    public Page<PersonDto> getFilteredPerson(PersonFilter personFilter, Pageable pageable) {
        return service.getFilteredPersons(personFilter, pageable);
    }
}
