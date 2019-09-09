package com.beerhouse.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

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
import com.beerhouse.validation.BeerValidation;

@RestController
@RequestMapping("/beers")
public class BeerController {

	@Autowired
	private BeerRepository beerRepository;

	@PostMapping
	public ResponseEntity<Beer> cria(@RequestBody Beer beer) {
		Beer savedBeer;

		if (BeerValidation.beerIsInvalida(beer)) {
			throw new BeerValidationException(BeerValidation.getMensagensCamposInvalidosBeer(beer));
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
	public ResponseEntity<Void> atualiza(@PathVariable Integer id, @RequestBody Beer beer){
		Beer beerToUpdate = beerRepository.findOne(id);

		if (beerToUpdate == null) {
			return ResponseEntity.notFound().build();
		}

		if (BeerValidation.beerIsInvalida(beer)) {
			throw new BeerValidationException(BeerValidation.getMensagensCamposInvalidosBeer(beer));
		}

		BeanUtils.copyProperties(beer, beerToUpdate, "id");

		beerToUpdate = beerRepository.save(beerToUpdate);

		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> atualizaComPatch(@PathVariable Integer id, @RequestBody Beer beer){
		Beer beerToUpdate = beerRepository.findOne(id);

		if (beerToUpdate == null) {
			return ResponseEntity.notFound().build();
		}

		if (BeerValidation.beerIsInvalida(beer)) {
			throw new BeerValidationException(BeerValidation.getMensagensCamposInvalidosBeer(beer));
		}

		BeanUtils.copyProperties(beer, beerToUpdate, "id");

		beerToUpdate = beerRepository.save(beerToUpdate);

		return ResponseEntity.ok().build();
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
}
