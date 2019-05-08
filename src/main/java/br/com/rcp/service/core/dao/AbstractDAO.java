package br.com.rcp.service.core.dao;

import br.com.rcp.service.core.model.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@SuppressWarnings("Duplicates")
@Transactional
abstract class AbstractDAO implements DAOInterface {
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private				Logger						logger;

	@Getter(AccessLevel.PROTECTED)
	@PersistenceContext
	private				EntityManager				manager;

	protected AbstractDAO() {
		setLogger(LoggerFactory.getLogger(getClass()));
	}

	@Override
	public <T extends AbstractModel> T find(Long sequence, Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaQuery<T>		query			= builder.createQuery(domain);
			Root<T>					root			= query.from(domain);
			return getManager().createQuery(query.select(root).where(builder.equal(root.get(AbstractModel_.sequence), sequence))).getSingleResult();
		} catch (Exception error) {
			getLogger().error(error.getMessage());
			return null;
		}
	}

	@Override
	public <T extends AbstractModel> T find(String identifier, Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaQuery<T>		query			= builder.createQuery(domain);
			Root<T>					root			= query.from(domain);
			return getManager().createQuery(query.select(root).where(builder.equal(root.get(AbstractModel_.identifier), identifier))).getSingleResult();
		} catch (Exception error) {
			getLogger().error(error.getMessage());
			return null;
		}
	}

	@Override
	public <T extends AbstractModel> List<T> find(Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaQuery<T>		query			= builder.createQuery(domain);
			Root<T>					root			= query.from(domain);
			return getManager().createQuery(query.select(root)).getResultList();
		} catch (Exception error) {
			getLogger().error(error.getMessage());
			return null;
		}
	}

	@Override
	public <T extends AbstractModel> List<T> find(List<String> identifiers, Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaQuery<T>		query			= builder.createQuery(domain);
			Root<T>					root			= query.from(domain);
			return getManager().createQuery(query.select(root).where(root.get(AbstractModel_.identifier).in(identifiers))).getResultList();
		} catch (Exception error) {
			getLogger().error(error.getMessage());
			return null;
		}
	}

	@Override
	public <T extends AbstractModel> List<T> find(Specification<T> specification, Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaQuery<T>		query			= builder.createQuery(domain);
			Root<T>					root			= query.from(domain);
			return getManager().createQuery(query.select(root).where(specification.toPredicate(root, query, builder))).getResultList();
		} catch (Exception error) {
			getLogger().error(error.getMessage());
			return null;
		}
	}

	@Override
	public <T extends AbstractModel> Page<T> find(Pageable pageable, Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaQuery<T>		query			= builder.createQuery(domain);
			Root<T>					root			= query.from(domain);
			CriteriaQuery<Long> 	counter			= builder.createQuery(Long.class);
			List<T>					result			= getManager().createQuery(query.select(root).orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder))).setFirstResult((int) pageable.getOffset()).setMaxResults((int) (pageable.getOffset() + pageable.getPageSize())).getResultList();
			Long					total			= getManager().createQuery(counter.select(builder.count(counter.from(domain)))).getSingleResult();
			return new PageImpl<>(result, pageable, total);
		} catch (Exception error) {
			getLogger().error(error.getMessage());
			return null;
		}
	}

	@Override
	public <T extends AbstractModel> Page<T> find(Specification<T> specification, Pageable pageable, Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaQuery<T>		query			= builder.createQuery(domain);
			Root<T>					root			= query.from(domain);
			CriteriaQuery<Long> 	counter			= builder.createQuery(Long.class);
			List<T>					result			= getManager().createQuery(query.select(root).where(specification.toPredicate(root, query, builder)).orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder))).setFirstResult((int) pageable.getOffset()).setMaxResults((int) (pageable.getOffset() + pageable.getPageSize())).getResultList();
			Long					total			= getManager().createQuery(counter.select(builder.count(counter.from(domain)))).getSingleResult();
			return new PageImpl<>(result, pageable, total);
		} catch (Exception error) {
			getLogger().error(error.getMessage());
			return null;
		}
	}

	@Override
	public <T extends AbstractModel> T save(T model) {
		getManager().persist(model);
		getManager().flush();
		getManager().clear();
		return model;
	}

	@Override
	public <T extends AbstractModel> List<T> save(List<T> models) {
		getManager().getTransaction().begin();

		for (int i = 0; i < models.size(); i++) {
			getManager().persist(models.get(i));
			if (i % 16 == 0) {
				getManager().flush();
				getManager().clear();
			}
		}

		getManager().flush();
		getManager().clear();
		getManager().getTransaction().commit();
		return models;
	}

	@Override
	public <T extends AbstractModel> void delete(Long sequence, Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaDelete<T> 		query			= builder.createCriteriaDelete(domain);
			Root<T>					root			= query.from(domain);
			getManager().createQuery(query.where(builder.equal(root.get(AbstractModel_.sequence), sequence))).executeUpdate();
		} catch (Exception error) {
			getLogger().error(error.getMessage());
		}
	}

	@Override
	public <T extends AbstractModel> void delete(String identifier, Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaDelete<T> 		query			= builder.createCriteriaDelete(domain);
			Root<T>					root			= query.from(domain);
			getManager().createQuery(query.where(builder.equal(root.get(AbstractModel_.identifier), identifier))).executeUpdate();
		} catch (Exception error) {
			getLogger().error(error.getMessage());
		}
	}

	@Override
	public <T extends AbstractModel> void delete(T model, Class<T> domain) {
		try {
			CriteriaBuilder			builder			= getManager().getCriteriaBuilder();
			CriteriaDelete<T> 		query			= builder.createCriteriaDelete(domain);
			Root<T>					root			= query.from(domain);
			getManager().createQuery(query.where(builder.equal(root, model))).executeUpdate();
		} catch (Exception error) {
			getLogger().error(error.getMessage());
		}
	}
}
