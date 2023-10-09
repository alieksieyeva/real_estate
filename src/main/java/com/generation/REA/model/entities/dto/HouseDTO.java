package com.generation.REA.model.entities.dto;

import com.generation.REA.model.entities.House;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class HouseDTO 
{
	//SCOPE di Classe
	//Proprietà Statiche
	
	//Metodi Statici
	
	//Scope di Oggetto
	//Intercambiabilmente Costruttori-Proprietà di oggetto
	
	public HouseDTO(House h)
	{
		id = h.getId();
		city = h.getCity();
		address = h.getAddress();
		type = h.getType();
		img_url = h.getImg_url();
        description = h.getDescription();
		smp = h.getSmp();
		area = h.getArea();
		agent_id = h.getAgente().getId();
		totalPrice = area *smp;
	}
	
	public House convertToHouse()
	{
		House h = House.builder().
				  id(id)
				  .address(address)
				  .area(area)
				  .type(type)
				  .img_url(img_url)
				  .description(description)
				  .smp(smp)
				  .city(city)
				  .build();
		
		return h;
	}
	
	private Integer id;
	private String city,address,type,img_url,description;
	private int smp;
	private Double area;
	private int agent_id;
	private Double totalPrice;
	
}
