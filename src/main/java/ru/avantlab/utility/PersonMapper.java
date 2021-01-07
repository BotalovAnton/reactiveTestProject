package ru.avantlab.utility;

import org.mapstruct.Mapper;
import ru.avantlab.model.Person;
import ru.avantlab.model.PersonDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDto personToPersonDto(Person person);

    Person personDtoToPerson(PersonDto personDto);

    List<PersonDto> personToPersonDto(List<Person> persons);

    List<Person> personDtoToPerson(List<PersonDto> personDtos);
}
