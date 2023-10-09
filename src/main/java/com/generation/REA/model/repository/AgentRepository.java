package com.generation.REA.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.REA.model.entities.Agent;

public interface AgentRepository extends JpaRepository <Agent, Integer>
{

	
}
