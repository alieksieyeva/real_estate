package com.generation.REA.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class House 

{
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	private String city,address,type,img_url,description;
	private int smp;
	private Double area;
	
	public boolean isValid()
	{
		return 	city!=null 			&& !city.isBlank() 			&&
				address!=null 		&& !address.isBlank() 		&&
				type!=null 			&& !type.isBlank()			&&
				img_url!=null 		&& !img_url.isBlank() 		&&
				description!=null 	&& !description.isBlank() 	&&
				smp>=0											&&
				area>0											 ;
	}
	
	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name="agent_id")
	@JsonIgnore
	private Agent agente;
	
}






