package br.com.rcp.service.core.query;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RSQLSpecificationBuilder<T> {
	Specification<T> build(LogicalNode node) {
		List<Specification<T>>		specifications			= node.getChildren().stream().map(this::build).filter(Objects::nonNull).collect(Collectors.toList());
		Specification<T>			specification			= specifications.get(0);

		switch (node.getOperator()) {
			case OR:
				for (int i = 1; i < specifications.size(); i++) {
					specification		= Specification.where(specification).or(specifications.get(i));
				}
				break;

			case AND:
				for (int i = 1; i < specifications.size(); i++) {
					specification		= Specification.where(specification).and(specifications.get(i));
				}
				break;
		}

		return specification;
	}

	Specification<T> build(ComparisonNode node) {
		return Specification.where(new RSQLSpecification<>(node.getSelector(), node.getOperator(), node.getArguments()));
	}

	private Specification<T> build(Node node) {
		if (node instanceof LogicalNode) {
			return build((LogicalNode) node);
		}
		if (node instanceof ComparisonNode) {
			return build((ComparisonNode) node);
		}
		return null;
	}
}
