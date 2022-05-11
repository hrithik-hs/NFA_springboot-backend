package com.example.springboot.controller;

import java.util.*;

import com.example.springboot.model.Nfa;
import com.example.springboot.source.FiniteAutomaton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.exception.ResourceNotFoundException;
import com.example.springboot.exception.InvalidResourceException;
import com.example.springboot.repository.NfaRepository;


//@CrossOrigin(origins="https://nfatodfa.azurewebsites.net/")
@CrossOrigin(origins= {"http://localhost:3000/", "https://spe-nfatodfa.netlify.app"})
@RestController
@RequestMapping("/") // Mapping api url
public class NfaController {

	private static final Logger logger = LogManager.getLogger(NfaController.class);
	@Autowired
	private NfaRepository nfaRepository;
	
	//all nfadfa
	@GetMapping("/nfa")
	public List<Nfa>getAllNfas(){
		logger.info("[Get All NFA]");
		return nfaRepository.findAll();
	}
	
	//create nfadfa 
	@PostMapping("/nfa")
	public Nfa createNfa(@RequestBody Nfa nfa) throws InvalidResourceException {
		logger.info("[Create NFA]");
		String[] stateList = nfa.getStates().split(",");
		String[] symbolList = nfa.getSymbols().split(",");
		String[] finalStateList = nfa.getFinalState().split(",");
		String[] initialStateList = nfa.getInitialState().split(",");
		String[] transitionList = nfa.getTransition().split(",");

		Map <String, Boolean> stateMap=new HashMap<>();
		Map <String, Boolean> symbolMap=new HashMap<>();
		for(String state: stateList){
			stateMap.put(state,Boolean.TRUE);
		}
		for(String symbol: symbolList){
			symbolMap.put(symbol,Boolean.TRUE);
		}
		boolean valid=Boolean.TRUE;
		if(initialStateList.length!=1){
			valid=Boolean.FALSE;
		}
		for(String state: finalStateList){
			if(stateMap.containsKey(state)==Boolean.FALSE  || stateMap.get(state)== Boolean.FALSE){
				valid=Boolean.FALSE;
			}
		}
		for(String state: initialStateList){
			if(stateMap.containsKey(state)==Boolean.FALSE  || stateMap.get(state)== Boolean.FALSE){
				valid=Boolean.FALSE;
			}
		}
		for(String transition: transitionList){
			List<String> transList = Arrays.asList(transition.split(":"));
			if(transList.size()!=3 ||
				symbolMap.containsKey(transList.get(1))==Boolean.FALSE  || symbolMap.get(transList.get(1))== Boolean.FALSE ||
				stateMap.containsKey(transList.get(0))==Boolean.FALSE  || stateMap.get(transList.get(0))== Boolean.FALSE ||
				stateMap.containsKey(transList.get(2))==Boolean.FALSE  || stateMap.get(transList.get(2))== Boolean.FALSE
			){
				valid = Boolean.FALSE;
			}
		}
		if (valid){
			FiniteAutomaton nfa1=new FiniteAutomaton(nfa);

			nfa.setRegularExpression(nfa1.getRE());

			return nfaRepository.save(nfa);
		}
		else {
			throw new InvalidResourceException("Input for NFA invalid");
		}
	}
	
	//get nfadfa by id
	@GetMapping("/nfa/{id}")
	public ResponseEntity<Nfa> getNfabyId(@PathVariable Long id){
		logger.info("[Get NFA by ID] - "+ id);
		Nfa nfa = nfaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: "+ id) );
		return ResponseEntity.ok(nfa);
	}


	@GetMapping("/nfa/dfa/{id}")
	public ResponseEntity<List<Nfa>> getNfaDfabyId(@PathVariable Long id){
		logger.info("[Get NFA and DFA by ID] - "+ id);
		Nfa nfa = nfaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: "+ id) );

		List <Nfa> list=new ArrayList<>();
		list.add(nfa);
		Nfa nfa1 =new Nfa(nfa.getId()+1, nfa.getStates(), nfa.getSymbols(), nfa.getInitialState(), nfa.getFinalState(), nfa.getTransition());
		list.add(nfa1);

		return ResponseEntity.ok(list);
	}
	// update nfadfa rest api
	@PutMapping("/nfa/{id}")
	public ResponseEntity<Nfa> updateNfa(@PathVariable Long id, @RequestBody Nfa nfaDetails) throws InvalidResourceException{
		logger.info("[Update NFA] - "+ id);
		String[] stateList = nfaDetails.getStates().split(",");
		String[] symbolList = nfaDetails.getSymbols().split(",");
		String[] finalStateList = nfaDetails.getFinalState().split(",");
		String[] initialStateList = nfaDetails.getInitialState().split(",");
		String[] transitionList = nfaDetails.getTransition().split(",");

		Map <String, Boolean> stateMap=new HashMap<>();
		Map <String, Boolean> symbolMap=new HashMap<>();
		for(String state: stateList){
			stateMap.put(state,Boolean.TRUE);
		}
		for(String symbol: symbolList){
			symbolMap.put(symbol,Boolean.TRUE);
		}
		boolean valid=Boolean.TRUE;
		if(initialStateList.length!=1){
			valid=Boolean.FALSE;
		}
		for(String state: finalStateList){
			if(stateMap.containsKey(state)==Boolean.FALSE  || stateMap.get(state)== Boolean.FALSE){
				valid=Boolean.FALSE;
			}
		}
		for(String state: initialStateList){
			if(stateMap.containsKey(state)==Boolean.FALSE  || stateMap.get(state)== Boolean.FALSE){
				valid=Boolean.FALSE;
			}
		}
		for(String transition: transitionList){
			List<String> transList = Arrays.asList(transition.split(":"));
			if(transList.size()!=3 ||
					symbolMap.containsKey(transList.get(1))==Boolean.FALSE  || symbolMap.get(transList.get(1))== Boolean.FALSE ||
					stateMap.containsKey(transList.get(0))==Boolean.FALSE  || stateMap.get(transList.get(0))== Boolean.FALSE ||
					stateMap.containsKey(transList.get(2))==Boolean.FALSE  || stateMap.get(transList.get(2))== Boolean.FALSE
			){
				valid = Boolean.FALSE;
			}
		}
		if (valid){
			Nfa nfa = nfaRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: "+ id) );

			nfa.setStates(nfaDetails.getStates());
			nfa.setSymbols(nfaDetails.getSymbols());
			nfa.setInitialState(nfaDetails.getInitialState());
			nfa.setFinalState(nfaDetails.getFinalState());
			nfa.setTransition(nfaDetails.getTransition());

			FiniteAutomaton nfa1=new FiniteAutomaton(nfa);
			nfa.setRegularExpression(nfa1.getRE());

			Nfa updatedNfa = nfaRepository.save(nfa);

			return ResponseEntity.ok(updatedNfa);
		}
		else {
			throw new InvalidResourceException("Input for NFA invalid");
		}
	}
	
	// delete nfadfa api
	@DeleteMapping("/nfa/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteNfa(@PathVariable Long id){
		logger.info("[Delete NFA] - "+ id);
		Nfa nfa = nfaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: " + id));
		
		nfaRepository.delete(nfa);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
