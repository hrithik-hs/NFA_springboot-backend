package com.example.springboot.source;

import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    private HashSet<Node> NodeSet;
    private HashMap<Edge, String> Edge_expr;
    private HashMap <Node, HashSet <Node>> Adj_list;
    private HashMap <Node, HashSet <Node>> Rev_Adj_list;

    public Graph()
    {
        NodeSet = new HashSet <Node> ();
        Edge_expr = new HashMap <> ();
        Adj_list = new HashMap <Node, HashSet <Node>> ();
        Rev_Adj_list = new HashMap <Node, HashSet <Node>> ();
    }

    public HashMap <Edge, String> getEdgeTransition()
    {
        return this.Edge_expr;
    }

    public HashMap <Node, HashSet <Node>> getRevList()
    {
        return this.Rev_Adj_list;
    }

    public HashMap <Node, HashSet <Node>> getAdjList()
    {
        return this.Adj_list;
    }

    public void addNode(Node n)
    {
        NodeSet.add(n);
        Adj_list.put(n, new HashSet <Node> ());
        Rev_Adj_list.put(n, new HashSet <Node> ());
    }

    public void addEdge(Edge e, String tr)
    {
        Edge_expr.put(e, tr);
//        if (Adj_list.get(e.gettail())!=null)
        Adj_list.get(e.gettail()).add(e.gethead());
//        if (Rev_Adj_list.get(e.gethead())!=null)
        Rev_Adj_list.get(e.gethead()).add(e.gettail());
    }

    public void deleteNode(Node n)
    {
        HashMap <Node, HashSet <Node>> Adj_list1 = new HashMap<>(Adj_list);
        NodeSet.remove(n);
        Adj_list.remove(n);

        for(Node n_it: Rev_Adj_list.get(n))
        {
            Adj_list.get(n_it).remove(n);
        }

        for(Node n_it: Adj_list1.get(n))
        {
            Rev_Adj_list.get(n_it).remove(n);
        }

        Rev_Adj_list.remove(n);

    }

    public void printG()
    {
        for(Node n_it: NodeSet)
        {
            System.out.println(n_it);
            System.out.println("Children are:");
            for(Node n_it1: Adj_list.get(n_it))
            {
                System.out.println(n_it1);
            }
            System.out.println("");
        }
    }

}
