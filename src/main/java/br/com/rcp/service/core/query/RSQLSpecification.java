package br.com.rcp.service.core.query;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RSQLSpecification<T> implements Specification<T> {
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private 		String 					property;

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private			ComparisonOperator		operator;

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private 		List<String> 			arguments;

	RSQLSpecification(String property, ComparisonOperator operator, List<String> arguments) {
		setProperty(property);
		setOperator(operator);
		setArguments(arguments);
	}

	@SuppressWarnings("DuplicateExpressions")
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<?>				arguments		= resolveArguments(root);
		Object				argument 		= arguments.get(0);
		LinkedList<String>	path			= new LinkedList<>(Arrays.asList(getProperty().split("\\.")));

		switch (RSQLSearchOperation.getSimpleOperator(operator)) {
			case EQUAL:
				if (argument instanceof String) {
					return builder.like(builder.upper(buildPath(root, path)), argument.toString().replace('*', '%').toUpperCase());
				} else if (argument == null) {
					return builder.isNull(buildPath(root, path));
				} else {
					return builder.equal(buildPath(root, path), argument);
				}

			case NOT_EQUAL:
				if (argument instanceof String) {
					return builder.notLike(builder.upper(buildPath(root, path)), argument.toString().replace('*', '%').toUpperCase());
				} else if (argument == null) {
					return builder.isNotNull(buildPath(root, path));
				} else {
					return builder.notEqual(buildPath(root, path), argument);
				}

			case GREATER_THAN:
				return builder.greaterThan(buildPath(root, path), argument.toString());

			case GREATER_THAN_OR_EQUAL:
				return builder.greaterThanOrEqualTo(buildPath(root, path), argument.toString());

			case LESS_THAN:
				return builder.lessThan(buildPath(root, path), argument.toString());

			case LESS_THAN_OR_EQUAL:
				return builder.lessThanOrEqualTo(buildPath(root, path), argument.toString());

			case IN:
				return buildPath(root, path).in(arguments);

			case NOT_IN:
				return builder.not(buildPath(root, path).in(arguments));
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private List<?> resolveArguments(final Root<T> root) {
		Class				type			= buildPath(root, new LinkedList<>(Arrays.asList(getProperty().split("\\.")))).getJavaType();
		List<Object>		types			= new ArrayList<>();

		for (String argument : getArguments()) {
			if (type.equals(Integer.class) || type.equals(int.class)) {
				types.add(Integer.parseInt(argument));
			} else if (type.equals(Long.class) || type.equals(long.class)) {
				types.add(Long.parseLong(argument));
			} else if (type.equals(Double.class) || type.equals(double.class)) {
				types.add(Double.parseDouble(argument));
			} else if (type.equals(Short.class) || type.equals(short.class)) {
				types.add(Short.parseShort(argument));
			} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
				types.add(Boolean.parseBoolean(argument));
			} else if (type.isEnum()) {
				types.add(Enum.valueOf(type, argument));
			} else {
				types.add(argument);
			}
		}

		return types;
	}

	private Path<String> buildPath(final Path<T> root, final LinkedList<String> properties) {
		if (properties.size() == 1) {
			return root.get((properties.removeFirst()));
		}
		return buildPath(root.get(properties.removeFirst()), properties);
	}
}
