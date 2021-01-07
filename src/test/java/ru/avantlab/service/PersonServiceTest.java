package ru.avantlab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.avantlab.model.Person;
import ru.avantlab.model.PersonDto;
import ru.avantlab.repository.PersonRepository;
import ru.avantlab.repository.specification.PersonFilter;
import ru.avantlab.repository.specification.PersonSpecification;
import ru.avantlab.service.exception.EntityNotFoundException;
import ru.avantlab.utility.PersonMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PersonServiceTest {

    private final PersonMapper mapper = Mockito.mock(PersonMapper.class);
    private final PersonRepository repository = Mockito.mock(PersonRepository.class);
    private final PersonSpecification personSpecification = Mockito.mock(PersonSpecification.class);
    private final PersonService service = new PersonServiceImpl(repository, mapper, personSpecification);

    private final long id = 1;
    private final String firstName = "Ivan";

    @Test
    public void save() {
        PersonDto savingPersonDto = new PersonDto();
        savingPersonDto.setFirstName(firstName);

        Person savingPerson = new Person();
        savingPerson.setFirstName(firstName);

        PersonDto savedPersonDto = new PersonDto();
        savedPersonDto.setFirstName(firstName);
        savedPersonDto.setId(id);

        Person savedPerson = new Person();
        savedPerson.setFirstName(firstName);
        savedPerson.setId(id);

        Mockito.when(mapper.personDtoToPerson(savingPersonDto)).thenReturn(savingPerson);
        Mockito.when(repository.save(savingPerson)).thenReturn(savedPerson);
        Mockito.when(mapper.personToPersonDto(savedPerson)).thenReturn(savedPersonDto);

        PersonDto actual = service.save(savingPersonDto);

        Mockito.verify(mapper, Mockito.times(1)).personDtoToPerson(savingPersonDto);
        Mockito.verify(repository, Mockito.times(1)).save(savingPerson);
        Mockito.verify(mapper, Mockito.times(1)).personToPersonDto(savedPerson);

        Assertions.assertEquals(savedPersonDto, actual);
    }

    @Test
    public void findById() {
        PersonDto personDto = new PersonDto(id);
        Person person = new Person(id);

        Mockito.when(mapper.personToPersonDto(person)).thenReturn(personDto);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(person));

        PersonDto actual = service.findById(id);

        Mockito.verify(mapper, Mockito.times(1)).personToPersonDto(person);
        Mockito.verify(repository, Mockito.times(1)).findById(id);

        Assertions.assertEquals(personDto, actual);
    }

    @Test
    public void findAll() {
        List<PersonDto> personDtos = Arrays.asList(new PersonDto(1L), new PersonDto(2L));
        List<Person> persons = Arrays.asList(new Person(1), new Person(2));
        Page<Person> page = new PageImpl<>(persons);
        Pageable pageable = Pageable.unpaged();


        Mockito.when(mapper.personToPersonDto(persons)).thenReturn(personDtos);
        Mockito.when(repository.findAll(pageable)).thenReturn(page);

        service.findAll(pageable);

        Mockito.verify(mapper, Mockito.times(1)).personToPersonDto(persons);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void update() {
        PersonDto personDto = new PersonDto(id);
        Person person = new Person(id);

        Mockito.when(mapper.personDtoToPerson(personDto)).thenReturn(person);

        service.update(personDto);

        Mockito.verify(mapper, Mockito.times(1)).personDtoToPerson(personDto);
        Mockito.verify(repository, Mockito.times(1)).save(person);
    }

    @Test
    public void delete() {
        service.delete(new PersonDto(1L));
        Mockito.verify(repository, Mockito.times(1)).delete(new Person(id));
    }

    @Test
    public void getFilteredPerson() {
        List<PersonDto> personDtos = Arrays.asList(new PersonDto(1L), new PersonDto(2L));
        List<Person> persons = Arrays.asList(new Person(1), new Person(2));
        Page<Person> page = new PageImpl<>(persons);
        Pageable pageable = Pageable.unpaged();
        Specification<Person> specification = Specification.where(null);
        PersonFilter personFilter = new PersonFilter();

        Mockito.when(mapper.personToPersonDto(persons)).thenReturn(personDtos);
        Mockito.when(repository.findAll(specification, pageable)).thenReturn(page);
        Mockito.when(personSpecification.filterPerson(personFilter)).thenReturn(specification);

        service.getFilteredPersons(personFilter, pageable);

        Mockito.verify(mapper, Mockito.times(1)).personToPersonDto(persons);
        Mockito.verify(repository, Mockito.times(1)).findAll(specification, pageable);
        Mockito.verify(personSpecification, Mockito.times(1)).filterPerson(personFilter);
    }

    @Test
    public void findByIdShouldThrowExceptionIfIdNotExist() {
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(null));

        Throwable throwable = Assertions.assertThrows(EntityNotFoundException.class, () -> service.findById(id));
        Assertions.assertEquals(String.format("person with id %s not found", id), throwable.getMessage());
    }
}
