package com.beerhouse;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.beerhouse.model.Beer;
import com.beerhouse.repository.BeerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe unitária responsável por testes unitários através de MockMvc.
 * Nessa classe os repositórios e requisições são "mockadas", evitando assim
 * modificações no banco de dados.
 * @author lukas
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ComponentScan(basePackageClasses = BeerApplication.class)
public class BeerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BeerRepository beerRepository;

	@Before
	public void init() {
		Beer testBeer = new Beer(1, "Pratinha Porter Porteira", "Lúpulo", "7,5 % de Teor Alcoólico",
				new BigDecimal("24.80"), "Ale");

		when(beerRepository.findOne(1)).thenReturn(testBeer);
	}

	@Test
	public void retornaBeerPorId() throws Exception {
		mockMvc.perform(get("/beers/1")).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Pratinha Porter Porteira")))
				.andExpect(jsonPath("$.ingredients", is("Lúpulo")))
				.andExpect(jsonPath("$.alcoholContent", is("7,5 % de Teor Alcoólico")))
				.andExpect(jsonPath("$.price", is(24.80))).andExpect(jsonPath("$.category", is("Ale")));

		verify(beerRepository, times(1)).findOne(1);
	}

	@Test
	public void retornaListagemBeers() throws Exception {
		List<Beer> listBeers = new ArrayList<Beer>();

		listBeers.add(new Beer(1, "Everbrew Ever Cali", "lupulo", "7,1% de teor", new BigDecimal("33.99"), "Ipa"));
		listBeers.add(new Beer(2, "Everbrew Evercrisp", null, "6,6%", new BigDecimal("40.00"), null));

		when(beerRepository.findAll()).thenReturn(listBeers);

		mockMvc.perform(get("/beers")).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("Everbrew Ever Cali")))
				.andExpect(jsonPath("$[0].alcoholContent", is("7,1% de teor")))
				.andExpect(jsonPath("$[0].price", is(33.99))).andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("Everbrew Evercrisp")))
				.andExpect(jsonPath("$[1].alcoholContent", is("6,6%"))).andExpect(jsonPath("$[1].price", is(40.00)));
	}

	@Test
	public void criaBeerInvalida() throws Exception {
		String jsonCriacaoBeer, expectedMsgRetResponse;

		// JSON para a requisição POST com apenas 1 dos campos obrigatórios preenchidos.
		jsonCriacaoBeer = "{\"name\":\"Paulaner\"}";

		expectedMsgRetResponse = "1 - O teor/conteúdo deve ser informado!"
				.concat("2- O preço deve ser informado e não pode ser inferior ou igual à Zero!");

		mockMvc.perform(
				post("/beers").content(jsonCriacaoBeer).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.data", is(notNullValue())))
				.andExpect(jsonPath("$.mensagem", containsString(expectedMsgRetResponse)));

		verify(beerRepository, times(0)).save(any(Beer.class));
	}

	@Test
	public void criaBeerValida() throws Exception {
		Beer beerToSave = new Beer(1, "Pratinha Porter Porteira", "Lúpulo", "7,5 % de Teor Alcoólico",
				new BigDecimal("24.80"), "Ale");
		ObjectMapper objMapper = new ObjectMapper();

		when(beerRepository.save(any(Beer.class))).thenReturn(beerToSave);

		mockMvc.perform(post("/beers").content(objMapper.writeValueAsString(beerToSave))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(header().stringValues("Location", "http://localhost/beers/1"));

		verify(beerRepository, times(1)).save(any(Beer.class));
	}

	@Test
	public void atualizaBeerInexistente() throws Exception {
		Beer beerToUpdate = new Beer(1, "Pratinha Porter Porteira", "Lúpulo", "7,5 % de Teor Alcoólico",
				new BigDecimal("24.80"), "Ale");
		ObjectMapper objMapper = new ObjectMapper();

		/*
		 * Ao pesquisar a cerveja de id igual à 1, então deve ser retornado nulo, o que
		 * acarretará na geração do status 404 para a requisição Put
		 */
		when(beerRepository.findOne(1)).thenReturn(null);

		mockMvc.perform(put("/beers/1").content(objMapper.writeValueAsString(beerToUpdate))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

		verify(beerRepository, times(0)).save(any(Beer.class));
	}

	@Test
	public void atualizaBeerValida() throws Exception {
		Beer beerToUpdate = new Beer(1, "Pratinha Porter Porteira", "Lúpulo", "7,5 % de Teor Alcoólico",
				new BigDecimal("24.80"), "Ale");
		ObjectMapper objMapper = new ObjectMapper();

		when(beerRepository.findOne(1)).thenReturn(beerToUpdate);

		mockMvc.perform(put("/beers/1").content(objMapper.writeValueAsString(beerToUpdate))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		verify(beerRepository, times(1)).save(any(Beer.class));
	}

	@Test
	public void atualizaComPatchBeerInexistente() throws Exception {
		Beer beerToUpdate = new Beer(1, "Pratinha Porter Porteira", "Lúpulo", "7,5 % de Teor Alcoólico",
				new BigDecimal("24.80"), "Ale");
		ObjectMapper objMapper = new ObjectMapper();

		/*
		 * Ao pesquisar a cerveja de id igual à 1, então deve ser retornado nulo, o que
		 * acarretará na geração do status 404 para a requisição Patch
		 */
		when(beerRepository.findOne(1)).thenReturn(null);

		mockMvc.perform(patch("/beers/1").content(objMapper.writeValueAsString(beerToUpdate))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

		verify(beerRepository, times(0)).save(any(Beer.class));
	}

	@Test
	public void atualizaComPatchBeerValida() throws Exception {
		Beer beerToUpdate = new Beer(1, "Pratinha Porter Porteira", "Lúpulo", "7,5 % de Teor Alcoólico",
				new BigDecimal("24.80"), "Ale");
		ObjectMapper objMapper = new ObjectMapper();

		when(beerRepository.findOne(1)).thenReturn(beerToUpdate);

		mockMvc.perform(patch("/beers/1").content(objMapper.writeValueAsString(beerToUpdate))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		verify(beerRepository, times(1)).save(any(Beer.class));
	}

	@Test
	public void deletaBeerInexistente() throws Exception {
		/*
		 * Força o não retorno de uma cerveja com o Id 1. Isso irá gerar um status 404 ao
		 * invocar o delete
		 */
		when(beerRepository.findOne(1)).thenReturn(null);

		doNothing().when(beerRepository).delete(1);

		mockMvc.perform(delete("/beers/1")).andExpect(status().isNotFound());

		verify(beerRepository, times(0)).delete(1);
	}

	@Test
	public void deletaBeerExistente() throws Exception {
		Beer beerToDelete = new Beer(1, "Pratinha Porter Porteira", "Lúpulo", "7,5 % de Teor Alcoólico",
				new BigDecimal("24.80"), "Ale");

		/*
		 * Força o retorno do objeto da classe de cerveja preenchido acima.
		 * Isso irá gerar um status 204 (No Content), que é o esperado,
		 * ao invocar o delete
		 */
		when(beerRepository.findOne(1)).thenReturn(beerToDelete);

		doNothing().when(beerRepository).delete(beerToDelete);

		mockMvc.perform(delete("/beers/1")).andExpect(status().isNoContent());

		verify(beerRepository, times(1)).delete(beerToDelete);
	}
}