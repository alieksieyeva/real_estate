package com.generation.REA.model.entities.dto.agent;

import com.generation.REA.model.entities.Agent;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AgentDTONoList extends GenericAgentDTO
{
	public AgentDTONoList() {}
	
	public AgentDTONoList(Agent a)
	{
		super(a);
	}
	
	
	@Override
	public Agent convertToAgent() 
	{
		Agent res = Agent.builder()
					.id(id)
					.name(name)
					.surname(surname)
					.salary(salary)
					.build();
		
		return res;
	}

	
}
