package com.beerhouse.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.beerhouse.customexception.BeerValidationException;
import com.beerhouse.model.Beer;
import com.beerhouse.repository.BeerRepository;

@RestController
@RequestMapping("/beers")
public class BeerController {

	@Autowired
	private BeerRepository beerRepository;

	@PostMapping
	public ResponseEntity<Beer> cria(@RequestBody Beer beer) {
		Beer savedBeer;

		if (beerIsInvalida(beer)) {
			throw new BeerValidationException(getMensagensCamposInvalidosBeer(beer));
		}

		savedBeer = beerRepository.save(beer);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedBeer.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping
	public List<Beer> listaTodos() {
		return beerRepository.findAll();
	}

	@GetMapping("/{id}")
	public Beer retornaPorId(@PathVariable Integer id) {
		return beerRepository.findOne(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Beer> atualiza(@PathVariable Integer id, @Valid @RequestBody Beer beer)
			throws BeerValidationException {
		Beer beerToUpdate = beerRepository.findOne(id);

		if (beerToUpdate == null) {
			return ResponseEntity.notFound().build();
		}

		if (beerIsInvalida(beer)) {
			throw new BeerValidationException(getMensagensCamposInvalidosBeer(beer));
		}

		BeanUtils.copyProperties(beer, beerToUpdate, "id");

		beerToUpdate = beerRepository.save(beerToUpdate);

		return ResponseEntity.ok(beerToUpdate);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Beer> atualizaComPatch(@PathVariable Integer id, @Valid @RequestBody Beer beer)
			throws BeerValidationException {
		Beer beerToUpdate = beerRepository.findOne(id);

		if (beerToUpdate == null) {
			return ResponseEntity.notFound().build();
		}

		if (beerIsInvalida(beer)) {
			throw new BeerValidationException(getMensagensCamposInvalidosBeer(beer));
		}

		BeanUtils.copyProperties(beer, beerToUpdate, "id");

		beerToUpdate = beerRepository.save(beerToUpdate);

		return ResponseEntity.ok(beerToUpdate);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleta(@PathVariable Integer id) {
		Beer beerToDelete = beerRepository.findOne(id);

		if (beerToDelete == null) {
			return ResponseEntity.notFound().build();
		}

		beerRepository.delete(beerToDelete);

		return ResponseEntity.noContent().build();
	}

	/**
	 * Retorna um booleano que indica se a cerveja informada como parâmetro é
	 * inválida. Para ser inválida pelo menos um dos campos obrigatórios deve estar
	 * sem preenchimento. No caso do campo Preço, além de ser verificado isso,
	 * também é verificado se o valor informado é maior do que Zero.
	 * 
	 * @param beer
	 * @return
	 */
	private boolean beerIsInvalida(Beer beer) {
		return beer.getName() == null || beer.getName().isEmpty() || beer.getAlcoholContent() == null
				|| beer.getAlcoholContent().isEmpty() || beer.getPrice() == null
				|| beer.getPrice().compareTo(BigDecimal.ZERO) != 1;
	}

	/**
	 * Retorna um texto contendo dados sobre os campos inválidos da cerveja
	 * informada como parâmetro.
	 * 
	 * @param beer
	 * @throws BeerValidationException
	 */
	private String getMensagensCamposInvalidosBeer(Beer beer) throws BeerValidationException {
		StringBuilder sbMensagemValidacao = new StringBuilder();
		boolean valido = true;
		int ind = 0;

		if (beer.getName() == null || beer.getName().isEmpty()) {
			sbMensagemValidacao.append(++ind).append(" - O nome deve ser informado!");

			if (valido) {
				valido = false;
			}
		}

		if (beer.getAlcoholContent() == null || beer.getAlcoholContent().isEmpty()) {
			sbMensagemValidacao.append(++ind).append(" - O teor/conteúdo deve ser informado!");

			if (valido) {
				valido = false;
			}
		}

		if (beer.getPrice() == null || beer.getPrice().compareTo(BigDecimal.ZERO) != 1) {
			sbMensagemValidacao.append(++ind).append("- O preço deve ser informado e não pode ")
					.append("ser inferior ou igual à Zero!");

			if (valido) {
				valido = false;
			}
		}

		if (!valido && sbMensagemValidacao.toString() != null && !sbMensagemValidacao.toString().isEmpty()) {
			throw new BeerValidationException("Não foi possível inserir/atualizar a cerveja! "
					.concat("Inconsistências: ").concat(sbMensagemValidacao.toString()));
		}

		return sbMensagemValidacao.toString();
	}
}
