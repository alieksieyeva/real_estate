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
			return new ResponseEntity<Object> (aRepo.save(toInsert), HttpStatus.CREATED);
		return new ResponseEntity<Object> ("non è andata bene", HttpStatus.BAD_REQUEST);
		
	}
	
	//Mappature su ID
	@GetMapping("/agents/{id}")
	public Agent getOne(@PathVariable int id)
	{
		if(aRepo.findById(id).isEmpty())
			throw new NoSuchElementException("non ho trovato il prodotto che vuoi leggere");
			
		return aRepo.findById(id).get();
	}
	

	@PutMapping("/agents/{id}")
	public Agent updateOne(@PathVariable int id,@RequestBody Agent toModify)
	{
		if(aRepo.findById(id).isEmpty())
			throw new NoSuchElementException("Non ho trovato su Db prodotto che vuoi modificare");
		toModify.setId(id);
		return aRepo.save(toModify);
	}
	
	@DeleteMapping("/agents/{id}")
	public void deleteOne(@PathVariable int id)
	{
		if(aRepo.findById(id).isEmpty())
			throw new NoSuchElementException("Non ho trovato su Db prodotto che vuoi cancellare");
		aRepo.deleteById(id);
	}
	
	//ExceptionHandler cattura questa eccezione per qualsiasi metodo di questa classe controller
					//il tipo di eccezione da gestire
	@ExceptionHandler(NoSuchElementException.class) //come catch(NoSuchElementException e)
	public  ResponseEntity <String> handleNoSuchElementException(NoSuchElementException e)
	{
		return new ResponseEntity <String>(e.getMessage(), HttpStatus.NOT_FOUND);
		//questo metodo d ala response nel caso in qui questa eccezione si verifichi in qualsiasi punto di questa classe 
	}
	
}
