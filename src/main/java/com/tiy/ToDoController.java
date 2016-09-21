package com.tiy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DTG2 on 09/15/16.
 */
@Controller
public class ToDoController {
    @Autowired
    ToDoRepository todos;

    @Autowired
    UserRepository users;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, HttpSession session, String text/*, Boolean is_done*/) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
        }

        List<ToDo> todoList = new ArrayList<ToDo>();

        if (text != null) {
            todoList = todos.findByText(text);
        } //else if (is_done != null) {
          //  todoList = todos.findByIs_done(is_done);
        //}
        else {
            User savedUser = (User)session.getAttribute("user");
            if (savedUser != null) {
                todoList = todos.findByUser(savedUser);
            } else {
                Iterable<ToDo> allToDos = todos.findAll();
                for (ToDo thisTodo : allToDos) {
                    todoList.add(thisTodo);
                }
            }
        }
        model.addAttribute("todos", todoList);
        model.addAttribute("user", session.getAttribute("user"));
        return "home";
    }

    @RequestMapping(path = "/add-todo", method = RequestMethod.POST)
    public String addToDo(HttpSession session, String text, boolean is_done) {
        User user = (User) session.getAttribute("user");
        if (text != null) {
            ToDo todo = new ToDo(text, is_done, user);
            todos.save(todo);
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/searchByToDo", method = RequestMethod.GET)
    public String queryTodosByText(Model model, String search) {
        System.out.println("Searching by ..." + search);
        List<ToDo> toDoList = todos.findByNameStartsWith(search);
        model.addAttribute("todos", toDoList);
        return "home";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.GET)
    public String deleteTodo(Model model, Integer todoID) {
        if (todoID != null) {
            todos.delete(todoID);
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, String userName, String password) throws Exception {
        User user = users.findFirstByName(userName);
        if (user == null) {
            init();
        }
        else if (!password.equals(user.getPassword())) {
            throw new Exception("Incorrect password");
        }
        session.setAttribute("user", user);
        return "redirect:/todos";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session/*, String userName, User user*/) {
        session.invalidate();
        return "redirect:/";
    }

    @PostConstruct
    public void init() {
        if (users.count() == 0) {
            User user = new User();
            user.name = "default user";
            user.password = "defaultpassword";
            users.save(user);
        }
    }

    @RequestMapping(path = "/todos", method = RequestMethod.GET)
    public String todos(Model model, HttpSession session, User user, String text, boolean is_done) {

        return "todos";
    }



}
