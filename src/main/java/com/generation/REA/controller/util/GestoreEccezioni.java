package com.generation.REA.controller.util;

import java.util.NoSuchElementException;

import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GestoreEccezioni 
{
	
	//ExceptionHandler cattura questa eccezione per qualsiasi metodo di questa classe controller
	//il tipo di eccezione da gestire
	@ExceptionHandler(NoSuchElementException.class) //come catch(NoSuchElementException e)
	public  ResponseEntity <String> handleNoSuchElementException(NoSuchElementException e)
	{
		return new ResponseEntity <String>(e.getMessage(), HttpStatus.NOT_FOUND);
		//questo metodo d ala response nel caso in qui questa eccezione si verifichi in qualsiasi punto di questa classe 
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class) 
	public  ResponseEntity <String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e)
	{
		return new ResponseEntity <String>("occhio al parametro", HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(InvalidDataAccessResourceUsageException.class) 
	public  ResponseEntity <String> handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException e)
	{
		return new ResponseEntity <String>("db fuori uso", HttpStatus.SERVICE_UNAVAILABLE);
		
	}
	
	
	@ExceptionHandler(InvalidEntityException.class) 
	public  ResponseEntity <String> handleInvalidEntityException(InvalidEntityException e)
	{
		return new ResponseEntity <String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		
	}
}
