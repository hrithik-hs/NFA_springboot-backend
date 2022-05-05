package com.example.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.springboot.model.Nfadfa;
import com.example.springboot.repository.NfadfaRepository;

@CrossOrigin(origins="http://localhost:3000/")
@RestController
@RequestMapping("/api/v1") // Mapping api url
public class NfadfaController {

	@Autowired
	private NfadfaRepository nfadfaRepository;
	
	//all nfadfa
	@GetMapping("/nfadfas")
	public List<Nfadfa>getAllNfadfas(){
		return nfadfaRepository.findAll();
	}
	
	//create nfadfa 
	@PostMapping("/nfadfas")
	public Nfadfa createNfadfa(@RequestBody Nfadfa nfadfa) {
		return nfadfaRepository.save(nfadfa);
	}
	
	//get nfadfa by id
	@GetMapping("/nfadfas/{id}")
	public ResponseEntity<Nfadfa> getNfadfasbyId(@PathVariable Long id){
		Nfadfa nfadfa=nfadfaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: "+ id) );
		return ResponseEntity.ok(nfadfa);
	}
	
	// update nfadfa rest api
	@PutMapping("/nfadfas/{id}")
	public ResponseEntity<Nfadfa> updateNfadfa(@PathVariable Long id, @RequestBody Nfadfa nfadfaDetails){
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
	
	// delete nfadfa api
	@DeleteMapping("/nfadfas/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteNfadfa(@PathVariable Long id){
		Nfadfa nfadfa = nfadfaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Does not exist with id: " + id));
		
		nfadfaRepository.delete(nfadfa);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
