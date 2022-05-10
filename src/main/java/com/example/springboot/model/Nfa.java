package com.example.springboot.model;

import com.example.springboot.source.FiniteAutomaton;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "nfa")
public class Nfa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//primary key generation.
	private long id;
	
	@Column(name = "states") //column name to field table
	private String states;

	@Column(name = "symbols")
	private String symbols;
	
	@Column(name = "initial_state")
	private String initialState;
	
	@Column(name = "final_state")
	private String finalState;
	
	@Column(name = "transition")
	private String transition;

	@Column(name = "regular_expression")
	private String regularExpression;
	
	public Nfa() {
		
	}
	
	public Nfa(long id, String states, String symbols, String initialState, String finalState, String transition) {
		super();
		this.id = id;
		this.states = states;
		this.symbols = symbols;
		this.initialState = initialState;
		this.finalState = finalState;
		this.transition = transition;
	}

	public long getId() {
		return id;
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public String getSymbols() {
		return symbols;
	}

	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}

	public String getInitialState() {
		return initialState;
	}

	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}

	public String getFinalState() {
		return finalState;
	}

	public void setFinalState(String finalState) {
		this.finalState = finalState;
	}

	public String getTransition() {
		return transition;
	}

	public void setTransition(String transition) {
		this.transition = transition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Nfa nfa = (Nfa) o;

//		if(nfadfa.id != null && nfadfa.id != id) return false;
		return states.equals(nfa.states) && symbols.equals(nfa.symbols) && initialState.equals(nfa.initialState) && finalState.equals(nfa.finalState) && transition.equals(nfa.transition);

	}

	@Override
	public int hashCode() {
		return Objects.hash(id, states, symbols, initialState, finalState, transition);
	}
}
