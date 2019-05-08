package br.com.rcp.service.core.controller;

import br.com.rcp.service.core.dto.DTOInterface;
import br.com.rcp.service.core.model.AbstractModel;
import br.com.rcp.service.core.query.CustomRSQLVisitor;
import br.com.rcp.service.core.service.BOInterface;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

public abstract class AbstractREST<T extends AbstractModel, DTO extends DTOInterface<T>> {
	@Getter(AccessLevel.PROTECTED)
	@Inject
	private			BOInterface<T, DTO>			service;

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> find(@PathVariable("id") String identifier) {
		return ResponseEntity.ok(getService().find(identifier));
	}

	@RequestMapping(method = RequestMethod.GET, path = "/")
	public ResponseEntity<?> findAll(@RequestParam(value = "search", required = false) String predicate, @RequestParam(value = "page", required = false) Pageable pagination) {
		if (predicate != null && !predicate.isEmpty() && pagination != null) {
			Node				node			= new RSQLParser().parse(predicate);
			Specification<T>	specification	= node.accept(new CustomRSQLVisitor<>());
			return ResponseEntity.ok(getService().find(specification, pagination));
		} else if (predicate != null && !predicate.isEmpty()) {
			Node				node			= new RSQLParser().parse(predicate);
			Specification<T>	specification	= node.accept(new CustomRSQLVisitor<>());
			return ResponseEntity.ok(getService().find(specification));
		} else if (pagination != null) {
			return ResponseEntity.ok(getService().find(pagination));
		}
		return ResponseEntity.ok(getService().find());
	}

	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH}, path = "/")
	public ResponseEntity<DTO> save(@RequestBody @Valid DTO dto) {
		return ResponseEntity.ok(getService().save(dto));
	}

	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH}, path = "/collection")
	public ResponseEntity<List<DTO>> save(@RequestBody @Valid List<DTO> collection) {
		return ResponseEntity.ok(getService().save(collection));
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String identifier) {
		getService().delete(identifier);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
