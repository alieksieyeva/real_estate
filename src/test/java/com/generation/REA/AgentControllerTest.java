package com.generation.REA;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generation.REA.model.entities.Agent;
import com.generation.REA.model.entities.dto.agent.AgentDTONoList;
import com.generation.REA.model.repository.AgentRepository;
//impostiamo la classe di test
//quale context applichiamo
@SpringBootTest(
		
		  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		  classes = ReaApplication.class)
@AutoConfigureMockMvc
class AgentControllerTest
{

	@Autowired
	MockMvc mock;	//MockMvc classe di spring boot che permette di mandare le request e ricevere le response http
	@Autowired
	AgentRepository repo;
	
	
	
	@Test
	void testGetOneAgent() throws Exception
	{
		
		mock.perform(MockMvcRequestBuilders.get("/agents/1"))  	//fare la request
		.andDo(print())											//stampare
		.andExpect(status().isOk())								//controlla status che deve essere ok
		.andExpect(MockMvcResultMatchers.content().json(agente1Json));	//controllare json (deve corrispondere a "agente1Json")
		
		mock.perform(MockMvcRequestBuilders.get("/agents/paperino"))  	
		.andDo(print())											
		.andExpect(status().isBadRequest())								
		.andExpect(MockMvcResultMatchers.content().string("occhio al parametro"));	
		
		mock.perform(MockMvcRequestBuilders.get("/agents/255"))  	
		.andDo(print())											
		.andExpect(status().isNotFound())								
		.andExpect(MockMvcResultMatchers.content().string("non ho trovato l'elemento che vuoi leggere"));	
	}
	
	@Test
	void testInsertAgent() throws Exception
	{
		//3 casistiche
		//json buono, agente valido, response ok
		//json cattivo, response 400
		//json buono, agente non valido, response 406
		
		//json buono, agente valido, response ok
		AgentDTONoList dtoValido = new AgentDTONoList();
		dtoValido.setName("Test");
		dtoValido.setSurname("Insert");
		dtoValido.setSalary(1600);
		
		String dtoJsonizzatoValido = asJsonString(dtoValido);
		mock.perform( MockMvcRequestBuilders
			      .post("/agents")							//faccio request post
			      .content(dtoJsonizzatoValido)				//body della request è il nostro json dtoJsonizzatoValido
			      .contentType(MediaType.APPLICATION_JSON) //specifico di che tipo è il body (fa parte del header)
			      .accept(MediaType.APPLICATION_JSON))		//response deve essere sempre json (fa parte del header)
		     		.andExpect(status().isOk());			//status deve essere 200
		
		//json buono, agente non valido, response 406
		AgentDTONoList dtoNonValido = new AgentDTONoList();
		dtoNonValido.setName("Test");
		dtoNonValido.setSurname("");
		dtoNonValido.setSalary(1600);
		String dtoJsonizzatoNonValido = asJsonString(dtoNonValido);
		
		mock.perform( MockMvcRequestBuilders
			      .post("/agents")						
			      .content(dtoJsonizzatoNonValido)				
			      .contentType(MediaType.APPLICATION_JSON) 
			      .accept(MediaType.APPLICATION_JSON))
		     		.andExpect(status().isNotAcceptable());
		
		
		//json cattivo, response 400
		String jsonBrutto = "{\"id\":null,\"pluto\":\"Test\",\"surname\":\"Insert\",\"salary\":'paperino'}";
		mock.perform( MockMvcRequestBuilders
			      .post("/agents")						
			      .content(jsonBrutto)				
			      .contentType(MediaType.APPLICATION_JSON) 
			      .accept(MediaType.APPLICATION_JSON))
		     		.andExpect(status().isBadRequest());
		
	}
	
	
	@Test
	void testDeleteAgent() throws Exception
	{
		
		Agent toDelete = new Agent();
		
		toDelete = repo.save(toDelete);
		//agente trovato ed eliminato
		mock.perform( MockMvcRequestBuilders
			      .delete("/agents/"+toDelete.getId())							
			      .contentType(MediaType.APPLICATION_JSON) )
		     		.andExpect(status().isOk());		
		
		//agente c'è ma è padre forbidden
		mock.perform( MockMvcRequestBuilders
			      .delete("/agents/2")						
			      .contentType(MediaType.APPLICATION_JSON)) 
		     		.andExpect(status().isForbidden());
		
		//agente non trovato su db 404 no such element
		mock.perform( MockMvcRequestBuilders
			      .delete("/agents/9999")						
			      .contentType(MediaType.APPLICATION_JSON)) 
		     		.andExpect(status().isNotFound())  
		     		.andExpect(MockMvcResultMatchers.content().string("Non ho trovato su Db agente che vuoi cancellare"));
	}
	
	//Testa updateOne di HouseController
	
	@Test
	void testUpdateHouse() throws Exception
	{
			mock.perform( MockMvcRequestBuilders
			      .put("/houses/1")						
			      .content(house1Json)				
			      .contentType(MediaType.APPLICATION_JSON) 
			      .accept(MediaType.APPLICATION_JSON))
		     		.andExpect(status().isOk());
			
			mock.perform( MockMvcRequestBuilders
				      .put("/houses/999")						
				      .content(house1Json)				
				      .contentType(MediaType.APPLICATION_JSON) 
				      .accept(MediaType.APPLICATION_JSON))
			     		.andExpect(status().isNotFound())
			     		.andExpect(MockMvcResultMatchers.content().string("la casa da modificare non esiste nel db"));
			
			mock.perform( MockMvcRequestBuilders
				      .put("/houses/1")						
				      .content(house1JsonBrutto)				
				      .contentType(MediaType.APPLICATION_JSON) 
				      .accept(MediaType.APPLICATION_JSON))
			     		.andExpect(status().isNotAcceptable())
			     		.andExpect(MockMvcResultMatchers.content().string("L'entita non valida"));
			
		     		
		
	}
	@Test
    void getOneByAgent() throws Exception
    {
        mock.perform(MockMvcRequestBuilders.get("/agents/1/houses/3"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(house1Json));


        mock.perform(MockMvcRequestBuilders.get("/agents/paperino/houses/2"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string("occhio al parametro"));

        mock.perform(MockMvcRequestBuilders.get("/agents/2/houses/bhooo"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string("occhio al parametro"));

        mock.perform(MockMvcRequestBuilders.get("/agents/255/houses/2"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().string("questo agente non esiste sul db"));

        mock.perform(MockMvcRequestBuilders.get("/agents/2/houses/3453"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().string("questa casa non esiste sul db"));

        mock.perform(MockMvcRequestBuilders.get("/agents/1/houses/2"))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(MockMvcResultMatchers.content().string("Casa ed agente non sono collegati"));
    }
	
	private static String asJsonString(Object obj)
	{
	    try 
	    {
	        return new ObjectMapper().writeValueAsString(obj);
	    } 
	    catch (Exception e) 
	    {
	        throw new RuntimeException(e);
	    }
	}
	
	String house1Json = 
"{\r\n"
+ "    \"city\": \"Torino\",\r\n"
+ "    \"address\": \"Via Palermo 3\",\r\n"
+ "    \"type\": \"4rooms\",\r\n"
+ "    \"img_url\": \"no photo\",\r\n"
+ "    \"description\": \"beautiful house\",\r\n"
+ "    \"smp\": 3000,\r\n"
+ "    \"area\": 100.0,\r\n"
+ "    \"agent_id\": 1,\r\n"
+ "    \"totalPrice\": 300000.0\r\n"
+ "}";
	
	String house1JsonBrutto = 
			"{    \"city\": \"Torino\",\r\n"
			+ "    \"address\": \"Via Palermo 3\",\r\n"
			+ "    \"type\": \"4rooms\",\r\n"
			+ "    \"img_url\": \"no photo\",\r\n"
			+ "    \"description\": \"casa modificata\",\r\n"
			+ "    \"smp\": -3000,\r\n"
			+ "    \"area\": 100.0,\r\n"
			+ "    \"agent_id\": 1,\r\n"
			+ "    \"totalPrice\": 300000.0}";
	
	
	String agente1Json = "{\r\n"
			+ "    \"id\": 1,\r\n"
			+ "    \"name\": \"Naomi\",\r\n"
			+ "    \"surname\": \"Walker\",\r\n"
			+ "    \"salary\": 1500,\r\n"
			+ "    \"houses\": [\r\n"
			+ "        {\r\n"
			+ "            \"id\": 3,\r\n"
			+ "            \"city\": \"Torino\",\r\n"
			+ "            \"address\": \"Via Palermo 3\",\r\n"
			+ "            \"type\": \"4rooms\",\r\n"
			+ "            \"img_url\": \"no photo\",\r\n"
			+ "            \"description\": \"beautiful house\",\r\n"
			+ "            \"smp\": 3000,\r\n"
			+ "            \"area\": 100.0,\r\n"
			+ "            \"agent_id\": 1,\r\n"
			+ "            \"totalPrice\": 300000.0\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"id\": 4,\r\n"
			+ "            \"city\": \"Torino\",\r\n"
			+ "            \"address\": \"via Verdi 7\",\r\n"
			+ "            \"type\": \"monolocale\",\r\n"
			+ "            \"img_url\": \"https://cdn.cosedicasa.com/wp-content/uploads/2019/10/divano-parete-tv.jpg\",\r\n"
			+ "            \"description\": \"perfetto per programmatore in divenire\",\r\n"
			+ "            \"smp\": 700,\r\n"
			+ "            \"area\": 25.0,\r\n"
			+ "            \"agent_id\": 1,\r\n"
			+ "            \"totalPrice\": 17500.0\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"id\": 5,\r\n"
			+ "            \"city\": \"Torino\",\r\n"
			+ "            \"address\": \"via Grassi 16\",\r\n"
			+ "            \"type\": \"monolocale\",\r\n"
			+ "            \"img_url\": \"https://www.gabetti.it/uploads/ckeditor/picture/data/676/content_Arredare-un-monolocale-con-soppalco.jpg\",\r\n"
			+ "            \"description\": \"perfetto per i programmatori quasi riusciti\",\r\n"
			+ "            \"smp\": 800,\r\n"
			+ "            \"area\": 30.0,\r\n"
			+ "            \"agent_id\": 1,\r\n"
			+ "            \"totalPrice\": 24000.0\r\n"
			+ "        }\r\n"
			+ "    ]\r\n"
			+ "}";
		
	
}
