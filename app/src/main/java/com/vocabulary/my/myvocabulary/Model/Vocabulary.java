package com.vocabulary.my.myvocabulary.Model;

/**
 * Created by David on 25/09/2017.
 */

public class Vocabulary {
    private int id;
    private String name;
    private String definition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setWord(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
