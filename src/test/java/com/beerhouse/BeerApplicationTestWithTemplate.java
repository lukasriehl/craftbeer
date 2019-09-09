package com.beerhouse;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.model.Beer;
import com.beerhouse.repository.BeerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BeerApplicationTestWithTemplate {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private BeerRepository beerRepository;

	@Test
	public void criaBeerInvalida() {
		String jsonCriacaoBeer, expectedResponseRet;
		HttpHeaders headers;
		HttpEntity<String> entity;
		ResponseEntity<String> response;

		// JSON para a requisição POST com apenas 1 dos campos obrigatórios preenchidos.
		jsonCriacaoBeer = "{\"name\":\"Paulaner\"}";

		expectedResponseRet = "1 - O teor/conteúdo deve ser informado!"
				.concat("2- O preço deve ser informado e não pode ser inferior ou igual à Zero!");

		headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		entity = new HttpEntity<>(jsonCriacaoBeer, headers);

		response = restTemplate.postForEntity("/beers", entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertThat(response.getBody(), containsString(expectedResponseRet));

		verify(beerRepository, times(0)).save(any(Beer.class));
	}

	@Test
	public void atualizaBeerInvalida() throws Exception{
		String expectedResponseRet;
		HttpHeaders headers;
		HttpEntity<String> entity;
		ResponseEntity<String> response;	
		ObjectMapper objMapper = new ObjectMapper();	
		
		//Preenche a cerveja que não será atualizada por estar faltando o preço
		Beer beerToUpdate = new Beer(1, "Pratinha Porter Porteira", "Lúpulo", "7,5 % de Teor Alcoólico",
				null, "Ale");
		
		when(beerRepository.findOne(1)).thenReturn(beerToUpdate);
		
		expectedResponseRet = "O preço deve ser informado e não pode ser inferior ou igual à Zero!";
		
		headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		entity = new HttpEntity<>(objMapper.writeValueAsString(beerToUpdate), headers);

		response = restTemplate.exchange("/beers/1", HttpMethod.PUT, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertThat(response.getBody(), containsString(expectedResponseRet));

		verify(beerRepository, times(0)).save(any(Beer.class));
	}
	
	@Test
	public void atualizaComPatchBeerInvalida() throws Exception{
		String expectedResponseRet;
		HttpHeaders headers;
		HttpEntity<String> entity;
		ResponseEntity<String> response;	
		ObjectMapper objMapper = new ObjectMapper();	
		
		//Preenche a cerveja que não será atualizada por estar faltando o nome
		Beer beerToUpdate = new Beer(1, null, "Lúpulo", "7,5 % de Teor Alcoólico",
				new BigDecimal("39.99"), "Ale");
		
		when(beerRepository.findOne(1)).thenReturn(beerToUpdate);
		
		expectedResponseRet = "O nome deve ser informado!";
		
		headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		entity = new HttpEntity<>(objMapper.writeValueAsString(beerToUpdate), headers);

		response = restTemplate.exchange("/beers/1", HttpMethod.PATCH, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertThat(response.getBody(), containsString(expectedResponseRet));

		verify(beerRepository, times(0)).save(any(Beer.class));
	}
	
	@Test
	public void deletaBeerInexistente() {
		HttpEntity<String> entity;
		ResponseEntity<String> response;
		Beer beerToDelete = new Beer(1, "Pratinha Porter Porteira", "Lúpulo", "7,5 % de Teor Alcoólico",
				new BigDecimal("24.80"), "Ale");
		
		/*Força o não retorno de uma cerveja com o Id 1.
		Isso irá gerar um status 404 ao invocar o delete*/
		when(beerRepository.findOne(1)).thenReturn(null);
		
		entity = new HttpEntity<>(null, new HttpHeaders());
		
		response = restTemplate.exchange("/beers/1", HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(beerRepository, times(0)).delete(beerToDelete);
	}
}
