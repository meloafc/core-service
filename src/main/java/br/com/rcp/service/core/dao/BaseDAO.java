package br.com.rcp.service.core.dao;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaseDAO extends AbstractDAO {
	// Concrete class for instantiation (DO NOTHING HERE! Or do whatever you want...)
}
