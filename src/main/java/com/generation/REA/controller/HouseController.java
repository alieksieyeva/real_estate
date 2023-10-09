package com.generation.REA.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.generation.REA.controller.util.InvalidEntityException;
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
	public ResponseEntity<?> getAll()
	{
 		//ci sono problemi di comunicazione con il db
 		try
 		{
 			 ResponseEntity<List<HouseDTO>> res = new ResponseEntity<List<HouseDTO>> 
			(
					repo.findAll()//lista di House
				   .stream() //la trasformo in uno Stream di House, qualcosa di molto simile ad un array javascript
				   .map(house -> new HouseDTO(house))//ogni casa nello stream la trasformo in una houseDTO
				   .toList(),//ritrasformo lo Stream in una lista, la lista sarà di house dto
				   HttpStatus.OK
			);
 			 
 			 return res;
 		}
 		catch(Exception e)
 		{
 			return new ResponseEntity<String>("Problemi di connessione verso il database",HttpStatus.SERVICE_UNAVAILABLE);
 		}
				
	}
 	
 	//Senza ID, brutale
	@GetMapping("/agents/{idPadre}/houses")
	public List<HouseDTO> getAllByAgent(@PathVariable Integer idPadre)
	{
		
		return aRepo.findById(idPadre).get().getCaseGestite()
				.stream()
				.map(house -> new HouseDTO(house))
				.toList();
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
	public HouseDTO insertOne
	(@PathVariable Integer idPadre,@RequestBody HouseDTO dto) 
	{
		//quando fallisce: 
		//1)qualche maledetto mi ha messo come idPadre "paperino", non lo riesce a convertire in un intero -> genera un eccezione in automatico (cerca in docu) MethodArgumentTypeMismatchException
		//2) non ho un agente con id=idPadre -> genera eccezione nosuchelementexception -> V
		//3) problemi con db -> lo becchiamo con una exception generica
		//4) la casa da inserire fa schifo -> creiamo un eccezione specifica noi
		Agent padre = aRepo.findById(idPadre).get();
		House toInsert = dto.convertToHouse();
		
		if(!toInsert.isValid())
			throw new InvalidEntityException("Casa non valida");
			
		
		
		toInsert.setAgente(padre);
		
		return new HouseDTO(repo.save(toInsert));
	}
	//Mappature su ID
	@GetMapping("/houses/{id}")
	public HouseDTO getOne(@PathVariable int id)
	{
		return new HouseDTO(repo.findById(id).get());
	}
	
	@GetMapping("/agents/{idPadre}/houses/{idFiglio}")
	public HouseDTO getOneByAgent(@PathVariable Integer idPadre,@PathVariable Integer idFiglio)
	{
		HouseDTO res = new HouseDTO(repo.findById(idFiglio).get());
		if(res.getAgent_id()!=idPadre)
			return null;
		
		return res;
	}
	
	@PutMapping("/agents/{idPadre}/houses/{idFiglio}")
	public HouseDTO updateOneByAgent
	(@PathVariable Integer idPadre,@PathVariable Integer idFiglio,@RequestBody HouseDTO dto)
	{
		//imposto l'id
		dto.setId(idFiglio);
		House toUpdate= dto.convertToHouse(); 
		
		
		
		Agent padre = aRepo.findById(idPadre).get();
		toUpdate.setAgente(padre);
		
		return new HouseDTO(repo.save(toUpdate));
	}
	           //        se metto proprietà qui significa fare ricerca per quella proprietà
	@GetMapping("/houses/city/{city}")
	public List<HouseDTO> getByCity(@PathVariable String city)
	{
		return repo.findByCity(city)
				.stream().map(casa -> new HouseDTO(casa)).toList();
	}
	
	
	@PutMapping("/houses/{id}")
	public House updateOne(@PathVariable int id,@RequestBody HouseDTO dto)
	{
		dto.setId(id);
		House toModify = dto.convertToHouse();
		return repo.save(toModify);
	}
	
	@DeleteMapping("/agents/{idPadre}/houses/{idFiglio}")
	public void deleteOneByAgent(@PathVariable Integer idPadre,@PathVariable Integer idFiglio)
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
	
}
