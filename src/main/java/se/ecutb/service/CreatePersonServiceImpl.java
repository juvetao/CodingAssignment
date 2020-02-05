package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.data.IdSequencers;
import se.ecutb.data.PersonRepository;
import se.ecutb.model.AbstractPersonFactory;
import se.ecutb.model.Address;
import se.ecutb.model.Person;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class CreatePersonServiceImpl extends AbstractPersonFactory implements CreatePersonService  {
    private PersonRepository personRepository;
    private IdSequencers idSequencers;

    @Autowired
    public CreatePersonServiceImpl(PersonRepository personRepository, IdSequencers idSequencers) {
        this.personRepository = personRepository;
        this.idSequencers = idSequencers;
    }

    CreatePersonServiceImpl(){};

    @Override
    public Person create(String firstName, String lastName, String email) throws IllegalArgumentException {
        List<Person> checkPersonList = personRepository.findAll();

        if(!checkPersonList.stream()
            .filter(person -> person.getFirstName().equalsIgnoreCase(firstName)
                    && person.getLastName().equalsIgnoreCase(lastName)
                    && person.getEmail().equalsIgnoreCase(email))
            .collect(Collectors.toList())
            .isEmpty())  {
            throw new IllegalArgumentException("user is already existing");
        }

        return createNewPerson(idSequencers.nextPersonId(), firstName, lastName, email, null);
    }

    @Override
    public Person create(String firstName, String lastName, String email, Address address) throws IllegalArgumentException {
        List<Person> checkPersonList = personRepository.findAll();
        if(!checkPersonList.stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName)
                        && person.getLastName().equalsIgnoreCase(lastName)
                        && person.getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList())
                .isEmpty())  {
            throw new IllegalArgumentException("user is already existing");
        }

        return createNewPerson(idSequencers.nextPersonId(), firstName, lastName, email, address);
    }
}
