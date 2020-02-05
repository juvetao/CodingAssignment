package se.ecutb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.model.Address;
import se.ecutb.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PersonRepositoryImpl implements PersonRepository {
    private List<Person> persons = new ArrayList<>();

    public PersonRepositoryImpl(List<Person> persons) {
        this.persons = persons;
    }

    public PersonRepositoryImpl(){};

    @Override
    public Optional<Person> findById(int personId) throws IllegalArgumentException {
        return persons.stream()
                .filter(person -> person.getPersonId()==personId)
                .findFirst();
    }

    @Override
    public Person persist(Person person) throws IllegalArgumentException {
        if(persons.toString().contains(person.getEmail()))
                {
            throw new IllegalArgumentException();
        }else {
            persons.add(person);
        }
        return person;
    }

    @Override
    public Optional<Person> findByEmail(String email) throws IllegalArgumentException{
        return persons.stream()
                .filter(person -> person.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<Person> findByAddress(Address address) throws IllegalArgumentException{
        if(address != null){
            return persons.stream()
                    .filter(person -> person.getAddress()!=null && person.getAddress().equals(address))
                    .collect(Collectors.toList());
        }
        return persons.stream()
                .filter(person -> person.getAddress()==null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> findByCity(String city) throws IllegalArgumentException {
        return persons.stream()
                .filter(person -> person.getAddress()!=null && person.getAddress().getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> findByLastName(String lastName) {
        return persons.stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> findByFullName(String fullName) {
        return persons.stream()
                .filter(person -> (person.getFirstName()+" "+person.getLastName()).equalsIgnoreCase(fullName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> findAll() {
        return persons;
    }

    @Override
    public boolean delete(int personId) throws IllegalArgumentException {
        boolean determine = false;
        Person unluckyOne = new Person();
        if(findById(personId).isPresent()){
            unluckyOne = findById(personId).get();
            persons.remove(unluckyOne);
            determine = true;
            return determine;}
        else if (!findById(personId).isPresent()){
            throw new IllegalArgumentException();
        }
        return determine;
    }

    @Override
    public void clear() {
        persons.clear();
    }
}
