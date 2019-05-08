package br.com.rcp.service.core.service;

import br.com.rcp.service.core.dto.DTOInterface;
import br.com.rcp.service.core.model.AbstractModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@SuppressWarnings("unused")
public interface BOInterface<T extends AbstractModel, DTO extends DTOInterface<T>> {
	DTO find(Long sequence);
	DTO find(String identifier);

	List<DTO> find();
	List<DTO> find(Specification<T> specification);

	Page<DTO> find(Pageable pageable);
	Page<DTO> find(Specification<T> specification, Pageable pageable);

	void delete(Long sequence);
	void delete(String identifier);
	void delete(T model);

	DTO save(DTO data);
	List<DTO> save(List<DTO> data);
}
