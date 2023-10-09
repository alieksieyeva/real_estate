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
import com.generation.REA.model.entities.House;
import com.generation.REA.model.entities.dto.HouseDTO;
import com.generation.REA.model.repository.AgentRepository;
import com.generation.REA.model.repository.HouseRepository;
@CrossOrigin
@RestController
public class HouseController 
{
	@Autowired
	HouseRepository repo;
	 
	@Autowired
	AgentRepository aRepo;
 
 	@GetMapping("/houses")
	public List<HouseDTO> getAll()
	{
 			
		return repo.findAll()//lista di House
			   .stream() //la trasformo in uno Stream di House, qualcosa di molto simile ad un array javascript
			   .map(house -> new HouseDTO(house))//ogni casa nello stream la trasformo in una houseDTO
			   .toList();//ritrasformo lo Stream in una lista, la lista sarà di house dto
				
	}
 	
 	//Senza ID, brutale
	@GetMapping("/agents/{idPadre}/houses")
	public List<House> getAllByProduct(@PathVariable Integer idPadre)
	{
		return aRepo.findById(idPadre).get().getCaseGestite();
	}
	
	@PostMapping("/houses")
	public House insert(@RequestBody HouseDTO dto)
	{ 
		Agent padre= aRepo.findById(dto.getAgent_id()).get(); //uso l'ID del padre nel DTO per prenderlo dal db
		House toInsert = dto.convertToHouse(); //converto il dto in una House senza agente
		toInsert.setAgente(padre);//imposto l'agente padre
		return repo.save(toInsert); //la salvo
	}
	
	@PostMapping("/agents/{idPadre}/houses")
	public House insertOne
	(@PathVariable Integer idPadre,@RequestBody House toInsert)
	{
		Agent padre = aRepo.findById(idPadre).get();
		toInsert.setAgente(padre);
		
		return repo.save(toInsert);
	}
	//Mappature su ID
	@GetMapping("/houses/{id}")
	public House getOne(@PathVariable int id)
	{
		return repo.findById(id).get();
	}
	@GetMapping("/agents/{idPadre}/houses/{idFiglio}")
	public House getOneByAgent(@PathVariable Integer idPadre,@PathVariable Integer idFiglio)
	{
		House res = repo.findById(idFiglio).get();
		if(res.getAgente().getId()!=idPadre)
			return null;
		
		return res;
	}
	
	@PutMapping("/agents/{idPadre}/houses/{idFiglio}")
	public House updateOneByAgent
	(@PathVariable Integer idPadre,@PathVariable Integer idFiglio,@RequestBody House toUpdate)
	{
		//imposto l'id
		toUpdate.setId(idFiglio);
		Agent padre = aRepo.findById(idPadre).get();
		toUpdate.setAgente(padre);
		
		return repo.save(toUpdate);
	}
	           //        se metto proprietà qui significa fare ricerca per quella proprietà
	@GetMapping("/houses/city/{city}")
	public List<House> getByCity(@PathVariable String city)
	{
		return repo.findByCity(city);
	}
	
	
	@PutMapping("/houses/{id}")
	public House updateOne(@PathVariable int id,@RequestBody House toModify)
	{
		toModify.setId(id);
		return repo.save(toModify);
	}
	
	@DeleteMapping("/agents/{idPadre}/houses/{idFiglio}")
	public void deleteOneByProduct(@PathVariable Integer idPadre,@PathVariable Integer idFiglio)
	{
		House res = repo.findById(idFiglio).get();
		if(res.getAgente().getId()!=idPadre)
			return;
		
		repo.deleteById(idFiglio);
	}
	
	@DeleteMapping("/houses/{id}")
	public void deleteOne(@PathVariable int id)
	{
		repo.deleteById(id);
	}
	
	@ExceptionHandler(NoSuchElementException.class) //come catch(NoSuchElementException e)
	public  ResponseEntity <String> handleNoSuchElementException(NoSuchElementException e)
	{
		return new ResponseEntity <String>(e.getMessage(), HttpStatus.NOT_FOUND);
		//questo metodo d ala response nel caso in qui questa eccezione si verifichi in qualsiasi punto di questa classe 
	}
}
