package com.beerhouse.validation;

import java.math.BigDecimal;

import com.beerhouse.customexception.BeerValidationException;
import com.beerhouse.model.Beer;

public final class BeerValidation {
	
	/**
	 * Retorna um booleano que indica se a cerveja informada como parâmetro é
	 * inválida. Para ser inválida pelo menos um dos campos obrigatórios deve estar
	 * sem preenchimento. No caso do campo Preço, além de ser verificado isso,
	 * também é verificado se o valor informado é maior do que Zero.
	 * 
	 * @param beer
	 * @return
	 */
	public static boolean beerIsInvalida(Beer beer) {
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
	public static String getMensagensCamposInvalidosBeer(Beer beer) throws BeerValidationException {
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
