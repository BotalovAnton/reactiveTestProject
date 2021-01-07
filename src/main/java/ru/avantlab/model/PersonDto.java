package ru.avantlab.model;

import ru.avantlab.utility.validation.GroupUpdated;

import javax.validation.constraints.*;
import java.util.Objects;

public class PersonDto {
    @Positive(groups = GroupUpdated.class)
    private long id;

    @Size(min = 3, max = 15)
    private String firstName;

    @Size(min = 3, max = 15)
    private String lastName;

    @Email
    private String email;

    @Pattern(regexp = "\\+7-\\d{3}-\\d{7}")
    private String phoneNumber;

    public PersonDto() {
    }

    public PersonDto(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDto personDto = (PersonDto) o;
        return id == personDto.id &&
                Objects.equals(email, personDto.email) &&
                Objects.equals(phoneNumber, personDto.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phoneNumber);
    }
}
