package com.beerhouse.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.beerhouse.customexception.BeerValidationException;
import com.beerhouse.customexception.DetalhesErro;

@ControllerAdvice
@RestController
public class TratamentoExcecaoController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BeerValidationException.class)
	public final ResponseEntity<DetalhesErro> handleUserNotFoundException(BeerValidationException ex,
			WebRequest request) {
		LocalDateTime dataHoraAtual = LocalDateTime.now();

		SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		String dataHoraFormatada = formatadorData.format(dataHoraAtual);

		DetalhesErro detErros = new DetalhesErro(dataHoraFormatada, ex.getMessage(), 
				request.getDescription(false));

		return new ResponseEntity<DetalhesErro>(detErros, HttpStatus.BAD_REQUEST);
	}
}
