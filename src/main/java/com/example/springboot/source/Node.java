package com.example.springboot.source;

public class Node {
    private String label;

    public Node(String ilabel)
    {
        this.label = ilabel;

    }



    public String getlabel()
    {
        return this.label;
    }

    @Override
    public boolean equals(Object n)
    {
        return (this.label.equals(((Node)n).getlabel()));
    }

    @Override
    public int hashCode()
    {
        return this.label.hashCode();
    }

    @Override
    public String toString()
    {
        return this.label;
    }
}
