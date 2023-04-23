package com.example.demo.components;

import org.javatuples.Pair;
import org.springframework.stereotype.Component;

@Component
public class WhiteBoard {

    private StringBuffer tail = new StringBuffer();
    private String head = "";

    public void append(String line)
    {
        this.head = line;
        this.tail.append(line + "\n");
    }

    public Pair<String, String> read(){
        return new Pair<>(head, tail.toString());
    }

    public String getTail()
    {
        return this.tail.toString();
    }

    public void clear()
    {
        this.tail = new StringBuffer();
        this.head = "";
    }

    public String cut()
    {
        String rv = getTail();
        clear();
        return rv;
    }
}
