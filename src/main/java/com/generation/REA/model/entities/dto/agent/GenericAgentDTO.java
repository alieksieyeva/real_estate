package com.generation.REA.model.entities.dto.agent;

import com.generation.REA.model.entities.Agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class GenericAgentDTO 
{

	public GenericAgentDTO() {}
	
	public GenericAgentDTO(Agent a)
	{
		id= a.getId();
		name = a.getName();
		surname = a.getSurname();
		salary = a.getSalary();
	}
	
	public abstract Agent convertToAgent();

	protected Integer id;
	protected String name, surname;
	protected int salary;
}
