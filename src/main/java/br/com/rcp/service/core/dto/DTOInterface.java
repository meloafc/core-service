package br.com.rcp.service.core.dto;

import br.com.rcp.service.core.model.AbstractModel;

public interface DTOInterface<T extends AbstractModel> {
	T getModel();
}
