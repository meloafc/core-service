package br.com.rcp.service.core.query;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public enum RSQLSearchOperation {
	EQUAL					(RSQLOperators.EQUAL),
	NOT_EQUAL				(RSQLOperators.NOT_EQUAL),
	GREATER_THAN			(RSQLOperators.GREATER_THAN),
	GREATER_THAN_OR_EQUAL	(RSQLOperators.GREATER_THAN_OR_EQUAL),
	LESS_THAN				(RSQLOperators.LESS_THAN),
	LESS_THAN_OR_EQUAL		(RSQLOperators.LESS_THAN_OR_EQUAL),
	IN						(RSQLOperators.IN),
	NOT_IN					(RSQLOperators.NOT_IN);

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private			ComparisonOperator 			enumeration;

	private RSQLSearchOperation(ComparisonOperator operator) {
		setEnumeration(operator);
	}

	public static RSQLSearchOperation getSimpleOperator(ComparisonOperator operator) {
		return Arrays.stream(RSQLSearchOperation.values()).filter(item -> item.getEnumeration() == operator).findAny().orElse(null);
	}
}
