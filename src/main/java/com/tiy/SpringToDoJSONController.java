package com.tiy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DTG2 on 09/19/16.
 */
@RestController
//  Rest API is a JSON API over HTTP (uses HTTP to talk to network and the content it sends is JSON content)
//  Says this controller is going to have endpoints that take in data and returns JSON.

public class SpringToDoJSONController {
    @Autowired //repo is initialized by the framework, so we don't have to pass a connection (among other things). Uses the application properties to connect to the database.
    ToDoRepository todos;

    @RequestMapping(path = "/todos.json", method = RequestMethod.GET) //REST JSOn endpoint is NOT required to end in .json. We can name it whatever we want, but we like to be expressive.
    public List<ToDo> getTodos(HttpSession session) {
        User user = (User)session.getAttribute("user");
        ArrayList<ToDo> todoList = new ArrayList<ToDo>();
        if (user != null) {
            Iterable<ToDo> allTodos = todos.findByUser(user);  //this is where Hibernate come into play.  Object Relational Mapping
            for (ToDo todo : allTodos) {
                todoList.add(todo);
            }
        }
//        try {
//            System.out.println("Catching a nap..");
//            Thread.sleep(3000);
//            System.out.println("Power naps are the best!!!");
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }

        return todoList;
//  this is an endpoint for a microservice
    }

    @RequestMapping(path = "/toggleTodo.json", method = RequestMethod.GET)
    public List<ToDo> toggleTodo(HttpSession session, int todoID) {
        System.out.println("toggling todo with ID " + todoID);
        ToDo todo = todos.findOne(todoID);
        todo.is_done = !todo.is_done;
        todos.save(todo);

        return getTodos(session);
    }

    @RequestMapping(path = "/addTodo.json", method = RequestMethod.POST)
    public List<ToDo> addTodo(HttpSession session, @RequestBody ToDo todo) throws Exception {
        User user = (User)session.getAttribute("user");

        if (user == null) {
            throw new Exception("Unable to add todo without an active user in the session");
        } else if (todo.text != null) {
            todo.user = user;
            todos.save(todo);
        }
        return getTodos(session);
    }

    @RequestMapping(path = "/deleteTodo.json", method = RequestMethod.GET)
    public List<ToDo> deleteTodo(HttpSession session/*, @RequestBody ToDo todo*/, int todoID) throws Exception {
        System.out.println("deleting todo with ID " + todoID);
        ToDo todo = todos.findOne(todoID);
        todos.delete(todo);

        return getTodos(session);
    }

    @RequestMapping(path = "/todoByUser.json", method = RequestMethod.POST)
    public List<ToDo> findTodoByUser(HttpSession session, @RequestBody ToDo todo) throws Exception {
        User user = (User)session.getAttribute("user");

        if (user == null) {
            throw new Exception("Unable to add todo without an active user in the session");
        }
        todo.user = user;
        todos.save(todo);

        return getTodos(session);
    }
}
