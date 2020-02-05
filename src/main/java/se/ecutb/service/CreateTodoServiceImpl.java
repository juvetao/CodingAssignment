package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.data.IdSequencers;
import se.ecutb.data.TodoRepository;
import se.ecutb.model.AbstractTodoFactory;
import se.ecutb.model.Person;
import se.ecutb.model.Todo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateTodoServiceImpl extends AbstractTodoFactory implements CreateTodoService {
    private TodoRepository todoRepository;
    private IdSequencers idSequencers;

    @Autowired
    public CreateTodoServiceImpl(TodoRepository todoRepository, IdSequencers idSequencers) {
        this.todoRepository = todoRepository;
        this.idSequencers = idSequencers;
    }

    CreateTodoServiceImpl(){};

    @Override
    public Todo createTodo(String taskDescription, LocalDate deadLine, Person assignee) throws IllegalArgumentException {
        List<Todo> checkTodoList = todoRepository.findAll();
        if(!checkTodoList.stream()
            .filter(todo -> todo.getTaskDescription().equalsIgnoreCase(taskDescription)
                            && todo.getDeadLine().equals(deadLine)
                            && todo.getAssignee().getPersonId() == assignee.getPersonId()
                            && todo.getAssignee() != null)
                .collect(Collectors.toList())
                .isEmpty()){
            throw new IllegalArgumentException("todo is already existing");
        }
        return createTodoItem(idSequencers.nextTodoId(), taskDescription, deadLine, assignee);
    }

    @Override
    public Todo createTodo(String taskDescription, LocalDate deadLine) throws IllegalArgumentException {
        List<Todo> checkTodoList = todoRepository.findAll();
        if(!checkTodoList.stream()
                .filter(todo -> todo.getTaskDescription().equalsIgnoreCase(taskDescription)
                        && todo.getDeadLine().equals(deadLine))
                .collect(Collectors.toList())
                .isEmpty()){
            throw new IllegalArgumentException("todo is already existing");
        }
        return createTodoItem(idSequencers.nextTodoId(), taskDescription, deadLine);
    }
}
