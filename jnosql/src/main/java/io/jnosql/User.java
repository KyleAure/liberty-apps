package io.jnosql;

import org.jnosql.artemis.Entity;
import org.jnosql.artemis.Id;

import java.io.Serializable;
import java.util.List;


@Entity
public class User implements Serializable {

    @Id
    private String userName;

    private String name;

    private List<String> phones;
    //Getters and setters are not required.
    //However, the class must have a non-private constructor with no arguments.
}