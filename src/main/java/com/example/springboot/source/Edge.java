package com.example.springboot.source;

public class Edge {
    private Node head;
    private Node tail;

    public Edge(Node itail, Node ihead)
    {
        this.tail = new Node(itail.getlabel());
        this.head = new Node(ihead.getlabel());
    }

    public Node gettail()
    {
        return new Node(this.tail.getlabel());
    }

    public Node gethead()
    {
        return new Node(this.head.getlabel());
    }



    @Override
    public boolean equals(Object obj)
    {
        Edge e = (Edge)(obj);
        return (this.head.equals(e.gethead()) && this.tail.equals(e.gettail()));
    }

    @Override
    public int hashCode()
    {
        int p1 = 999999503;
        int p2 = 1000000007;
        return (this.head.hashCode() + p1*this.tail.hashCode())%p2;
    }
}
