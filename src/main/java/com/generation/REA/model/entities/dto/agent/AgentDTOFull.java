package com.generation.REA.model.entities.dto.agent;

import java.util.ArrayList;
import java.util.List;

import com.generation.REA.model.entities.Agent;
import com.generation.REA.model.entities.House;
import com.generation.REA.model.entities.dto.HouseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentDTOFull extends GenericAgentDTO
{
	public AgentDTOFull()
	{
		
	}
	
	public AgentDTOFull (Agent a)
	{
		super(a);
		houses = a.getCaseGestite().stream().map(house -> new HouseDTO(house)).toList();
	}
	

	@Override
	public Agent convertToAgent() 
	{
		Agent res = Agent.builder()
					.id(id)
					.name(name)
					.salary(salary)
					.surname(surname)
					.build();
		
		res.setCaseGestite
		(
				houses.stream()
				.map(dto -> {
								House h = dto.convertToHouse();
								h.setAgente(res);
								return h;
							}
				).toList()
		);
		//res.setCaseGestite(convertHousesDTOtoHouses(res));
		
		return res;
	}
	
	@SuppressWarnings("unused")
	private List<House> convertHousesDTOtoHouses(Agent padre)
	{
		List<House> res = new ArrayList<House>();
		
		for(HouseDTO dto : houses)
		{
			House h = dto.convertToHouse();
			h.setAgente(padre);
			res.add(h);
		}
		
		return res;
	}

	private List<HouseDTO> houses;
	
}
