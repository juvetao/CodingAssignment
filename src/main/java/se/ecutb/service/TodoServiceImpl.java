package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.data.IdSequencers;
import se.ecutb.data.TodoRepository;
import se.ecutb.dto.TodoDto;
import se.ecutb.model.Person;
import se.ecutb.model.Todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TodoServiceImpl implements TodoService {

    private CreateTodoService createTodoService;
    private IdSequencers idSequencers;
    private CreatePersonService createPersonService;
    private TodoDtoConversionService todoDtoConversionService;
    private TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(CreateTodoService createTodoService, IdSequencers idSequencers, CreatePersonService createPersonService,
                           TodoDtoConversionService todoDtoConversionService, TodoRepository todoRepository) {
        this.createTodoService = createTodoService;
        this.idSequencers = idSequencers;
        this.createPersonService = createPersonService;
        this.todoDtoConversionService = todoDtoConversionService;
        this.todoRepository = todoRepository;
    }
    @Override
    public Todo createTodo(String taskDescription, LocalDate deadLine, Person assignee) {
        return todoRepository.persist(createTodoService.createTodo(taskDescription,deadLine,assignee));
    }

    @Override
    public TodoDto findById(int todoId) throws IllegalArgumentException {
        if (!todoRepository.findById(todoId).isPresent()){
            throw new IllegalArgumentException();
        }
        return todoDtoConversionService.convertToDto(todoRepository.findById(todoId).get());
    }

    @Override
    public List<TodoDto> findByTaskDescription(String taskDescription) {

        List<TodoDto> todoDtoList = new ArrayList<>();
        List<Todo> todoList = todoRepository.findByTaskDescriptionContains(taskDescription);
        for(Todo todo : todoList){
            todoDtoList.add(todoDtoConversionService.convertToDto(todo));
        }
        return todoDtoList;
    }

    @Override
    public List<TodoDto> findByDeadLineBefore(LocalDate endDate) {
        List<TodoDto> todoDtoList = new ArrayList<>();
        List<Todo> todoList = todoRepository.findByDeadLineBefore(endDate);
        for(Todo todo : todoList){
            todoDtoList.add(todoDtoConversionService.convertToDto(todo));
        }
        return todoDtoList;
    }

    @Override
    public List<TodoDto> findByDeadLineAfter(LocalDate startDate) {
        List<TodoDto> todoDtoList = new ArrayList<>();
        List<Todo> todoList = todoRepository.findByDeadLineAfter(startDate);
        for(Todo todo : todoList){
            todoDtoList.add(todoDtoConversionService.convertToDto(todo));
        }
        return todoDtoList;
    }

    @Override
    public List<TodoDto> findByDeadLineBetween(LocalDate startDate, LocalDate endDate) {
        List<TodoDto> todoDtoList = new ArrayList<>();
        List<Todo> todoList = todoRepository.findByDeadLineBetween(startDate , endDate);
        for(Todo todo : todoList){
            todoDtoList.add(todoDtoConversionService.convertToDto(todo));
        }
        return todoDtoList;
    }

    @Override
    public List<TodoDto> findAssignedTasksByPersonId(int personId) {
        List<TodoDto> todoDtoList = new ArrayList<>();
        List<Todo> todoList = todoRepository.findByAssigneeId(personId);
        for(Todo todo : todoList){
            todoDtoList.add(todoDtoConversionService.convertToDto(todo));
        }
        return todoDtoList;
    }

    @Override
    public List<TodoDto> findUnassignedTasks() {
        return null;
    }

    @Override
    public List<TodoDto> findAssignedTasks() {
        return null;
    }

    @Override
    public List<TodoDto> findByDoneStatus(boolean done) {
        List<TodoDto> todoDtoList = new ArrayList<>();
        List<Todo> todoList = todoRepository.findByDone(done);
        for(Todo todo : todoList){
            todoDtoList.add(todoDtoConversionService.convertToDto(todo));
        }
        return todoDtoList;
    }

    @Override
    public List<TodoDto> findAll() {
        List<TodoDto> todoDtoList = new ArrayList<>();
        List<Todo> todoList = todoRepository.findAll();
        for(Todo todo : todoList){
            todoDtoList.add(todoDtoConversionService.convertToDto(todo));
        }
        return todoDtoList;
    }

    @Override
    public boolean delete(int todoId) throws IllegalArgumentException {
        return todoRepository.delete(findById(todoId).getTodoId());
    }
}
