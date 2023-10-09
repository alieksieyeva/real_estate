package com.generation.REA.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.generation.REA.model.entities.Agent;
import com.generation.REA.model.entities.dto.agent.AgentDTOFull;
import com.generation.REA.model.entities.dto.agent.AgentDTONoList;
import com.generation.REA.model.repository.AgentRepository;
import com.generation.REA.controller.util.*;

@CrossOrigin
@RestController
public class AgentController 
{
	
	@Autowired
	AgentRepository aRepo;
	
	@GetMapping("/agents")
	public List<AgentDTOFull> getAll()
	{
		return getAllAgentToDTOs();
	}
	
	@GetMapping("/agents/full")
	public List<AgentDTOFull> getAllFull()
	{
		return getAllAgentToDTOs();
	}
	
	private List<AgentDTOFull> getAllAgentToDTOs()
	{
		return aRepo.findAll().stream().map(agent -> new AgentDTOFull(agent)).toList();
	}
	
	@GetMapping("/agents/nolist")
	public List<AgentDTONoList> getAllNoList()
	{
		return aRepo.findAll().stream().map(agent -> new AgentDTONoList(agent)).toList();
	}
	
	
	@PostMapping("/agents")
	public ResponseEntity<Object> insert(@RequestBody AgentDTONoList dto)
	{
		Agent toInsert = dto.convertToAgent();
		if(toInsert.isValid())
			return new ResponseEntity<Object> (new AgentDTONoList(aRepo.save(toInsert)), HttpStatus.CREATED);
		return new ResponseEntity<Object> ("non è andata bene", HttpStatus.BAD_REQUEST);
		
	}
	
	//Mappature su ID
	@GetMapping("/agents/{id}")
	public AgentDTOFull getOne(@PathVariable int id)
	{
		if(aRepo.findById(id).isEmpty())
			throw new NoSuchElementException("non ho trovato l'elemento che vuoi leggere");
		
		AgentDTOFull letto = new AgentDTOFull(aRepo.findById(id).get());
		return letto;
	}
	

	@PutMapping("/agents/{id}")
	public ResponseEntity <Object> updateOne(@PathVariable int id,@RequestBody AgentDTONoList dto)
	{
		if(aRepo.findById(id).isEmpty())
			throw new NoSuchElementException("Non ho trovato su Db persona che vuoi modificare");
		
		dto.setId(id);
		Agent toModify= dto.convertToAgent();
		return new ResponseEntity <Object> (new AgentDTONoList(aRepo.save(toModify)), HttpStatus.ACCEPTED);
	}
	
	//NON CREDO SIA DA MODIFICARE CON DTO
	@DeleteMapping("/agents/{id}")
	public void deleteOne(@PathVariable int id)
	{
		if(aRepo.findById(id).isEmpty())
			throw new NoSuchElementException("Non ho trovato su Db prodotto che vuoi cancellare");
		aRepo.deleteById(id);
	}
	
	
}
