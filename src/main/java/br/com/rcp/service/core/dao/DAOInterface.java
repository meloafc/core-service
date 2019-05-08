package br.com.rcp.service.core.dao;

import br.com.rcp.service.core.model.AbstractModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface DAOInterface {
	<T extends AbstractModel> T find(Long sequence, Class<T> domain);
	<T extends AbstractModel> T find(String identifier, Class<T> domain);

	<T extends AbstractModel> List<T> find(Class<T> domain);
	<T extends AbstractModel> List<T> find(List<String> identifiers, Class<T> domain);
	<T extends AbstractModel> List<T> find(Specification<T> specification, Class<T> domain);

	<T extends AbstractModel> Page<T> find(Specification<T> specification, Pageable pageable, Class<T> domain);
	<T extends AbstractModel> Page<T> find(Pageable pageable, Class<T> domain);

	<T extends AbstractModel> T save(T model);
	<T extends AbstractModel> List<T> save(List<T> models);

	<T extends AbstractModel> void delete(Long sequence, Class<T> domain);
	<T extends AbstractModel> void delete(String identifier, Class<T> domain);
	<T extends AbstractModel> void delete(T model, Class<T> domain);
}
