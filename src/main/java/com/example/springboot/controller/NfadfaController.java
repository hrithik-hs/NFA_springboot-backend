package com.example.springboot.controller;

import java.util.*;

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
import com.example.springboot.model.Nfadfa;
import com.example.springboot.repository.NfadfaRepository;


//@CrossOrigin(origins="https://nfatodfa.azurewebsites.net/")
@CrossOrigin(origins= {"http://localhost:3000/", "https://spe-nfatodfa.netlify.app"})
@RestController
@RequestMapping("/") // Mapping api url
public class NfadfaController {

	private static final Logger logger = LogManager.getLogger(NfadfaController.class);
	@Autowired
	private NfadfaRepository nfadfaRepository;
	
	//all nfadfa
	@GetMapping("/nfa")
	public List<Nfadfa>getAllNfas(){
		logger.info("[Get All NFA]");
		return nfadfaRepository.findAll();
	}
	
	//create nfadfa 
	@PostMapping("/nfa")
	public Nfadfa createNfa(@RequestBody Nfadfa nfadfa) throws InvalidResourceException {
		logger.info("[Create NFA]");
		String[] stateList = nfadfa.getStates().split(",");
		String[] symbolList = nfadfa.getSymbols().split(",");
		String[] finalStateList = nfadfa.getFinalState().split(",");
		String[] initialStateList = nfadfa.getInitialState().split(",");
		String[] transitionList = nfadfa.getTransition().split(",");

		Map <String, Boolean> stateMap=new HashMap<>();
		Map <String, Boolean> symbolMap=new HashMap<>();
		for(String state: stateList){
			stateMap.put(state,Boolean.TRUE);
		}
		for(String symbol: symbolList){
			symbolMap.put(symbol,Boolean.TRUE);
		}
		boolean valid=Boolean.TRUE;
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
			return nfadfaRepository.save(nfadfa);
		}
		else {
			throw new InvalidResourceException("Input for NFA invalid");
		}
	}
	
	//get nfadfa by id
	@GetMapping("/nfa/{id}")
	public ResponseEntity<Nfadfa> getNfabyId(@PathVariable Long id){
		logger.info("[Get NFA by ID] - "+ id);
		Nfadfa nfadfa=nfadfaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: "+ id) );
		return ResponseEntity.ok(nfadfa);
	}


	@GetMapping("/nfa/dfa/{id}")
	public ResponseEntity<List<Nfadfa>> getNfaDfabyId(@PathVariable Long id){
		logger.info("[Get NFA and DFA by ID] - "+ id);
		Nfadfa nfadfa=nfadfaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: "+ id) );

		List <Nfadfa> list=new ArrayList<>();
		list.add(nfadfa);
		Nfadfa nfadfa1 =new Nfadfa(nfadfa.getId()+1,nfadfa.getStates(),nfadfa.getSymbols(),nfadfa.getInitialState(),nfadfa.getFinalState(),nfadfa.getTransition());
		list.add(nfadfa1);

		return ResponseEntity.ok(list);
	}
	// update nfadfa rest api
	@PutMapping("/nfa/{id}")
	public ResponseEntity<Nfadfa> updateNfa(@PathVariable Long id, @RequestBody Nfadfa nfadfaDetails) throws InvalidResourceException{
		logger.info("[Update NFA] - "+ id);
		String[] stateList = nfadfaDetails.getStates().split(",");
		String[] symbolList = nfadfaDetails.getSymbols().split(",");
		String[] finalStateList = nfadfaDetails.getFinalState().split(",");
		String[] initialStateList = nfadfaDetails.getInitialState().split(",");
		String[] transitionList = nfadfaDetails.getTransition().split(",");

		Map <String, Boolean> stateMap=new HashMap<>();
		Map <String, Boolean> symbolMap=new HashMap<>();
		for(String state: stateList){
			stateMap.put(state,Boolean.TRUE);
		}
		for(String symbol: symbolList){
			symbolMap.put(symbol,Boolean.TRUE);
		}
		boolean valid=Boolean.TRUE;
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
			Nfadfa nfadfa=nfadfaRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: "+ id) );

			nfadfa.setStates(nfadfaDetails.getStates());
			nfadfa.setSymbols(nfadfaDetails.getSymbols());
			nfadfa.setInitialState(nfadfaDetails.getInitialState());
			nfadfa.setFinalState(nfadfaDetails.getFinalState());
			nfadfa.setTransition(nfadfaDetails.getTransition());


			Nfadfa updatedNfadfa=nfadfaRepository.save(nfadfa);
			return ResponseEntity.ok(updatedNfadfa);
		}
		else {
			throw new InvalidResourceException("Input for NFA invalid");
		}
	}
	
	// delete nfadfa api
	@DeleteMapping("/nfa/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteNfa(@PathVariable Long id){
		logger.info("[Delete NFA] - "+ id);
		Nfadfa nfadfa = nfadfaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: " + id));
		
		nfadfaRepository.delete(nfadfa);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
