package br.com.rcp.service.core.query;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

public class CustomRSQLVisitor<T> implements RSQLVisitor<Specification<T>, Void> {
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private			RSQLSpecificationBuilder<T>			builder;

	public CustomRSQLVisitor() {
		setBuilder(new RSQLSpecificationBuilder<>());
	}

	@Override
	public Specification<T> visit(AndNode node, Void param) {
		return getBuilder().build(node);
	}

	@Override
	public Specification<T> visit(OrNode node, Void param) {
		return getBuilder().build(node);
	}

	@Override
	public Specification<T> visit(ComparisonNode node, Void params) {
		return getBuilder().build(node);
	}
}
