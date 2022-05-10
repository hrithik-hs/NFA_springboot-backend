package com.example.springboot.source;

import com.example.springboot.model.Nfa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FiniteAutomaton {
    private final Graph auto_graph;
    private final ArrayList <String> state_list;
    private final String start_state;
    private final ArrayList <String> finish_states;
    private final HashMap <Pair <String, String>, ArrayList <String>> transitions;

    public FiniteAutomaton(ArrayList <String> istate_list, String istart_state, ArrayList <String> ifinish_states
            ,HashMap <Pair <String, String>, ArrayList <String>> itransitions)
    {
        this.state_list = new ArrayList<>(istate_list);
        this.finish_states = new ArrayList<>(ifinish_states);
        this.start_state = istart_state;
        this.transitions = new HashMap<>(itransitions);
        this.auto_graph=new Graph();
//        this.state_list = istate_list;
//        this.finish_states = ifinish_states;
//        this.start_state = istart_state;
//        this.transitions = itransitions;

        // add nodes in graph
        for(String state: istate_list)
        {
            auto_graph.addNode(new Node(state));
        }

        // add edges in graph

        for(Pair <String, String> p: transitions.keySet())
        {
            for(String s: transitions.get(p))
                    auto_graph.addEdge(new Edge(new Node(p.getFirst()), new Node(s)), p.getSecond());
        }
    }

    public FiniteAutomaton(Nfa nfa){

        String[] stateList = nfa.getStates().split(",");
        String[] symbolList = nfa.getSymbols().split(",");
        String[] finalStateList = nfa.getFinalState().split(",");
        String[] initialStateList = nfa.getInitialState().split(",");
        String[] transitionList = nfa.getTransition().split(",");

        this.state_list=new ArrayList<>();
        this.finish_states=new ArrayList<>();
//        this.start_state="";
        this.transitions =new HashMap<>();
        this.auto_graph=new Graph();

        this.state_list.addAll(Arrays.asList(stateList));
        this.finish_states.addAll(Arrays.asList(finalStateList));
        this.start_state = initialStateList[0];

        for(String transition: transitionList){
            List<String> transList = Arrays.asList(transition.split(":"));
            Pair<String,String> key=new Pair<>(transList.get(0), transList.get(1));
            if (!this.transitions.containsKey(key)) {
                this.transitions.put(key, new ArrayList<String>());
            }
//            System.out.println(key);
            this.transitions.get(key).add(transList.get(2));
        }

        // add nodes in graph
        for(String state: this.state_list)
        {
            auto_graph.addNode(new Node(state));
        }

        // add edges in graph

        for(Pair <String, String> p: transitions.keySet())
        {
            for(String s: transitions.get(p))
                auto_graph.addEdge(new Edge(new Node(p.getFirst()), new Node(s)), p.getSecond());
        }
    }
    public ArrayList <String> getStates()
    {
        return this.state_list;
    }
    public ArrayList <String> getFinalStates()
    {
        return this.finish_states;
    }
    public String getStartState()
    {
        return this.start_state;
    }
    public Graph getGraph()
    {
        return this.auto_graph;
    }
    public HashMap <Pair <String, String>, ArrayList <String>> getTransitions()
    {
        return this.transitions;
    }

    public static FiniteAutomaton Nfa_to_Gfa(FiniteAutomaton nfa)
    {
        ArrayList <String> gfa_states = nfa.getStates();
        HashMap <Pair <String, String>, ArrayList <String>> gfa_transitions = nfa.getTransitions();
        gfa_states.add("gfa_start");
        gfa_states.add("gfa_end");

        ArrayList <String> to_start = new ArrayList<>();
        to_start.add(nfa.getStartState());

        gfa_transitions.put(new Pair <String, String> ("gfa_start", "$"), to_start);

        for(String end_state: nfa.getFinalStates())
        {
            if(!gfa_transitions.containsKey(new Pair <String, String>(end_state, "$")))
            {
                //System.out.println("check");
                ArrayList <String> empty = new ArrayList<>();
                gfa_transitions.put(new Pair <String, String>(end_state, "$"), empty);
            }
            if(!gfa_transitions.containsKey(new Pair <String, String>(end_state, "$")))
            {
                //System.out.println("check");
            }
            gfa_transitions.get(new Pair <String, String> (end_state, "$")).add("gfa_end");
        }
        ArrayList <String> gfa_end_states = new ArrayList<>();
        gfa_end_states.add("gfa_end");
        FiniteAutomaton gfa = new FiniteAutomaton(gfa_states, "gfa_start", gfa_end_states, gfa_transitions);

        return gfa;
    }

    public void addTransition(String s1, String s2, String tr)
    {
        Node ns1 = new Node(s1);
        Node ns2 = new Node(s2);
        Edge e = new Edge(ns1, ns2);
        String newtr = tr;
        if(this.auto_graph.getEdgeTransition().containsKey(e))
        {
            newtr = "(" + "(" + newtr + ")" + "+" + "(" + this.auto_graph.getEdgeTransition().get(e) + ")" + ")";
        }
        if(newtr == "")
            this.auto_graph.addEdge(e, tr);
        else
            this.auto_graph.addEdge(e, newtr);
    }

    public void removeState(String s)
    {
        this.state_list.remove(s);
        this.auto_graph.deleteNode(new Node(s));
    }
    public void removeStates()
    {
        while(this.state_list.size()>2)
        {
            //System.out.println(state_list.size());
            for(String to_remove: this.getStates())
            {
//                System.out.println("Removed State:" + to_remove);
                if((to_remove != "gfa_start")&&(to_remove!= "gfa_end"))
                {
                    String mid = "";

                    if(this.auto_graph.getEdgeTransition().containsKey(new Edge(new Node(to_remove), new Node (to_remove))))
                    {
                        mid = "(" + this.auto_graph.getEdgeTransition().get(new Edge (new Node(to_remove), new Node(to_remove)))
                                + ")" + "*";
                    }
                    for(Node prev: this.auto_graph.getRevList().get(new Node(to_remove)))
                    {
                        if(prev.getlabel()!= to_remove)
                        {
                            for(Node child: this.auto_graph.getAdjList().get(new Node(to_remove)))
                            {
                                if(child.getlabel()!= to_remove)
                                {
                                    String pref = this.auto_graph.getEdgeTransition().get(new Edge(prev, new Node(to_remove)));
                                    String suff = this.auto_graph.getEdgeTransition().get(new Edge(new Node(to_remove), child));
                                    this.addTransition(prev.getlabel(), child.getlabel(), pref + "."+ mid + "." + suff);
                                }
                            }
                        }
                    }
                    this.removeState(to_remove);
                    break;
                }
            }
        }
    }
    public String getRE()
    {
        String ans = "";
        FiniteAutomaton gfa = FiniteAutomaton.Nfa_to_Gfa(this);
        gfa.removeStates();
        ans = gfa.auto_graph.getEdgeTransition().get(new Edge(new Node ("gfa_start"), new Node ("gfa_end")));
        return ans;
    }

//    public static void main(String[] args)
//    {
//        // test1
//        ArrayList <String> statelist1 = new ArrayList<>();
//        statelist1.add("A");
//        statelist1.add("B");
//        statelist1.add("D");
//        String startstate1 = "A";
//        ArrayList <String> finish_states1 = new ArrayList<>();
//        finish_states1.add("D");
//        HashMap <Pair <String, String>, ArrayList <String>> transitions1 = new HashMap<>();
//        ArrayList <String> t1 = new ArrayList<>();
//        ArrayList <String> t2 = new ArrayList<>();
////        ArrayList <String> t3 = new ArrayList<>();
//        t1.add("B");
//        t2.add("D");
////        t3.add("D");
//        transitions1.put(new Pair <String, String> ("A", "b"), t1);
//        transitions1.put(new Pair <String, String> ("B", "a"), t2);
//        transitions1.put(new Pair <String, String> ("A", "d"), t2);
//
//        Nfa nfa =new Nfa(5,"A,B,D","a,b,d","A","D","A:b:B,B:a:D,A:d:D");
//        String[] stateList = nfa.getStates().split(",");
////        String[] symbolList = nfa.getSymbols().split(",");
//        String[] finalStateList = nfa.getFinalState().split(",");
//        String[] initialStateList = nfa.getInitialState().split(",");
//        String[] transitionList = nfa.getTransition().split(",");
//
//        ArrayList<String> state_list=new ArrayList<>();
//        ArrayList<String> finish_states=new ArrayList<>();
//        String start_state="";
//        HashMap <Pair <String, String>, ArrayList <String>> transitions =new HashMap<>();
//
//        state_list.addAll(Arrays.asList(stateList));
//        finish_states.addAll(Arrays.asList(finalStateList));
//        start_state = initialStateList[0];
//
//        for(String transition: transitionList){
//            List<String> transList = Arrays.asList(transition.split(":"));
//            Pair<String,String> key=new Pair<>(transList.get(0), transList.get(1));
//            if (!transitions.containsKey(key)) {
//                transitions.put(key, new ArrayList<String>());
//            }
//            transitions.get(key).add(transList.get(2));
//        }
//        System.out.println("States");
//        System.out.println(Arrays.toString(statelist1.toArray()));
//        System.out.println(Arrays.toString(state_list.toArray()));
//        System.out.println("initial");
//        System.out.println(startstate1);
//        System.out.println(start_state);
//        System.out.println("finish");
//        System.out.println(Arrays.toString(finish_states1.toArray()));
//        System.out.println(Arrays.toString(finish_states.toArray()));
//        System.out.println("transition ");
//        transitions1.entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        });
//        System.out.println(" ");
//        transitions.entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        });
//
//        System.out.println(state_list.equals(statelist1));
//        System.out.println(start_state.equals(startstate1));
//        System.out.println(finish_states.equals(finish_states1));
//        System.out.println(transitions.equals(transitions1));
//
////        FiniteAutomaton nfa1 = new FiniteAutomaton(statelist1, startstate1, finish_states1, transitions1);
////        System.out.println(nfa1.getRE());
//        FiniteAutomaton nfa2 = new FiniteAutomaton(state_list,start_state,finish_states,transitions);
//        System.out.println(nfa2.getRE());
//
//    }
}
