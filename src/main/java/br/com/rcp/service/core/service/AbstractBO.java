package br.com.rcp.service.core.service;

import br.com.rcp.service.core.dao.DAOInterface;
import br.com.rcp.service.core.dto.DTOInterface;
import br.com.rcp.service.core.model.AbstractModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class AbstractBO<T extends AbstractModel, DTO extends DTOInterface<T>> implements BOInterface<T, DTO> {
	@Getter(AccessLevel.PROTECTED)
	@Inject
	private				DAOInterface	DAO;

	@Getter(AccessLevel.PRIVATE)
	@Inject
	private				MapperFactory	factory;

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private  			Class<T>		domain;

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private  			Class<DTO>		domainDTO;

	@SuppressWarnings("unchecked")
	protected AbstractBO() {
		setDomain((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		setDomainDTO((Class<DTO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
	}

	@Override
	public DTO find(Long sequence) {
		return toDTO(getDAO().find(sequence, getDomain()));
	}

	@Override
	public DTO find(String identifier) {
		return toDTO(getDAO().find(identifier, getDomain()));
	}

	@Override
	public List<DTO> find() {
		return getDAO().find(getDomain()).stream().map(this::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<DTO> find(Specification<T> specification) {
		return getDAO().find(specification, getDomain()).stream().map(this::toDTO).collect(Collectors.toList());
	}

	@Override
	public Page<DTO> find(Pageable pageable) {
		return getDAO().find(pageable, getDomain()).map(this::toDTO);
	}

	@Override
	public Page<DTO> find(Specification<T> specification, Pageable pageable) {
		return getDAO().find(specification, pageable, getDomain()).map(this::toDTO);
	}

	@Override
	public void delete(Long sequence) {
		getDAO().delete(sequence, getDomain());
	}

	@Override
	public void delete(String identifier) {
		getDAO().delete(identifier, getDomain());
	}

	@Override
	public void delete(T model) {
		getDAO().delete(model, getDomain());
	}

	protected <S, D> BoundMapperFacade<S, D> getMapper(Class<S> source, Class<D> destination) {
		getFactory().classMap(source, destination).mapNulls(false).mapNullsInReverse(false).exclude("sequence").byDefault().register();
		return getFactory().getMapperFacade(source, destination);
	}

	protected <S> BoundMapperFacade<S, S> getMapper(Class<S> domain) {
		getFactory().classMap(domain, domain).mapNulls(false).mapNullsInReverse(false).exclude("sequence").byDefault().register();
		return getFactory().getMapperFacade(domain, domain);
	}

	private DTO toDTO(T item) {
		try {
			return getDomainDTO().getConstructor(getDomain()).newInstance(item);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
