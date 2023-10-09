package com.generation.REA.model.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Agent
{
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name, surname;
	private int salary;
	
	@OneToMany (mappedBy = "agente", fetch = FetchType.EAGER)
	private List<House> caseGestite;
	
	public boolean isValid()
	{
		return 	name!=null && !name.isBlank() && 
				surname!=null && !surname.isBlank() &&
				salary>0;
	}
	

}
