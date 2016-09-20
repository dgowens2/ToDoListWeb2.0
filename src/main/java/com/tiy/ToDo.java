package com.tiy;

import javax.persistence.*;

/**
 * Created by DTG2 on 09/15/16.
 */
@Entity
@Table(name = "todos")
public class ToDo {
    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false)
    String text;

    @Column(nullable = false) //do we add this since false is a boolean condition?
    boolean is_done;

    @ManyToOne
    User user;

    public ToDo() {
    }

    public ToDo(String text, boolean not_done, User user) {
        this.text = text;
        this.is_done = not_done;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean is_done() {
        return is_done;
    }

    public void setIs_done(boolean is_done) {
        this.is_done = is_done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
