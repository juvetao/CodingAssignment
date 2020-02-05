package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.data.IdSequencers;
import se.ecutb.data.PersonRepository;
import se.ecutb.data.TodoRepository;
import se.ecutb.dto.PersonDto;
import se.ecutb.dto.PersonDtoWithTodo;
import se.ecutb.dto.TodoDto;
import se.ecutb.model.Address;
import se.ecutb.model.Person;
import se.ecutb.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonServiceImpl implements PersonService {
    private TodoRepository todoRepository;
    private PersonDtoConversionService personDtoConversionService;
    private CreatePersonService createPersonService;
    private PersonRepository personRepository;
    private IdSequencers idSequencers;

    @Autowired
    public PersonServiceImpl(CreatePersonService createPersonService, PersonRepository personRepository,
                             IdSequencers idSequencers,PersonDtoConversionService personDtoConversionService, TodoRepository todoRepository) {
        this.createPersonService = createPersonService;
        this.personRepository = personRepository;
        this.idSequencers = idSequencers;
        this.personDtoConversionService = personDtoConversionService;
        this.todoRepository = todoRepository;
    }

    @Override
    public Person createPerson(String firstName, String lastName, String email, Address address){

        return personRepository.persist(createPersonService.create(firstName, lastName, email, address));
    }

    @Override
    public List<PersonDto> findAll() {
        List<PersonDto> personDtolist = new ArrayList<>();
        List<Person> personList = personRepository.findAll();
        for(Person person : personList){
            personDtolist.add(personDtoConversionService.convertToPersonDto(person));
        }
        return personDtolist;
    }

    @Override
    public PersonDto findById(int personId) throws IllegalArgumentException {
        return personDtoConversionService.convertToPersonDto(personRepository.findById(personId).get());
    }

    @Override
    public Person findByEmail(String email) throws IllegalArgumentException {
        return personRepository.findByEmail(email).get();
    }

    @Override
    public List<PersonDtoWithTodo> findPeopleWithAssignedTodos() {
        List<Todo> todoList = todoRepository.findAll().stream()
                .collect(Collectors.toList());
        List<PersonDtoWithTodo> personDtoWithTodoList = personRepository.findAll().stream()
                .map(person -> personDtoConversionService.convertToPersonDtoWithTodo(person,todoList))
                .collect(Collectors.toList());

        return personDtoWithTodoList.stream()
                .filter(personDtoWithTodo -> personDtoWithTodo.getAssignedTodo().size() != 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> findAllPeopleWithNoTodos() {
        List<Todo> todoList = todoRepository.findAll().stream()
                .collect(Collectors.toList());
        List<PersonDtoWithTodo> personDtoWithTodoList = personRepository.findAll().stream()
                .map(person -> personDtoConversionService.convertToPersonDtoWithTodo(person,todoList))
                .collect(Collectors.toList());

        return personDtoWithTodoList.stream()
                .filter(personDtoWithTodo -> personDtoWithTodo.getAssignedTodo().size() == 0)
                .map(personDtoWithTodo -> personDtoConversionService.convertToPersonDto(personRepository.findById(personDtoWithTodo.getPersonId()).get()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> findPeopleByAddress(Address address) {
        List<PersonDto> personDtolist = new ArrayList<>();
        List<Person> personList = personRepository.findByAddress(address);
        for(Person person : personList){
            personDtolist.add(personDtoConversionService.convertToPersonDto(person));
        }
        return personDtolist;
    }

    @Override
    public List<PersonDto> findPeopleByCity(String city) {
        List<PersonDto> personDtolist = new ArrayList<>();
        List<Person> personList = personRepository.findByCity(city);
        for(Person person : personList){
            personDtolist.add(personDtoConversionService.convertToPersonDto(person));
        }
        return personDtolist;
    }

    @Override
    public List<PersonDto> findByFullName(String fullName) {
        List<PersonDto> personDtolist = new ArrayList<>();
        List<Person> personList = personRepository.findByFullName(fullName);
        for(Person person : personList){
            personDtolist.add(personDtoConversionService.convertToPersonDto(person));
        }
        return personDtolist;
    }

    @Override
    public List<PersonDto> findByLastName(String lastName) {
        List<PersonDto> personDtolist = new ArrayList<>();
        List<Person> personList = personRepository.findByLastName(lastName);
        for(Person person : personList){
            personDtolist.add(personDtoConversionService.convertToPersonDto(person));
        }
        return personDtolist;
    }

    @Override
    public boolean deletePerson(int personId) throws IllegalArgumentException {
        return personRepository.delete(findById(personId).getPersonId());
    }
}
