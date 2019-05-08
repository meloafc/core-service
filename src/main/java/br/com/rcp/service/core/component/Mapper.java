package br.com.rcp.service.core.component;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Mapper implements FactoryBean<MapperFactory> {
	@Override
	public MapperFactory getObject() {
		return new DefaultMapperFactory.Builder().mapNulls(false).build();
	}

	@Override
	public Class<?> getObjectType() {
		return MapperFactory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
