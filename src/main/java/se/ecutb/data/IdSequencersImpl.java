package se.ecutb.data;

import org.springframework.stereotype.Component;
import org.springframework.util.comparator.Comparators;
import se.ecutb.model.Person;
import se.ecutb.model.Todo;

import java.util.ArrayList;
import java.util.List;

@Component
public class IdSequencersImpl implements IdSequencers {
    private static int personIdCounter;

    private static int todoIdCounter;

    @Override
    public int nextPersonId() {

        return ++personIdCounter;
    }

    @Override
    public int nextTodoId() {
        return ++todoIdCounter;
    }

    @Override
    public void clearPersonId() {
        personIdCounter = 0;
    }

    @Override
    public void clearTodoId() {
        todoIdCounter = 0;
    }
}
