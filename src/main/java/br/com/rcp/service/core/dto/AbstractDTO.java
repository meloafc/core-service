package br.com.rcp.service.core.dto;

import br.com.rcp.service.core.model.AbstractModel;
import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.lang.reflect.ParameterizedType;
import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = {"id", "created_at", "updated_at"}, alphabetic = true)
@NoArgsConstructor
public abstract class AbstractDTO<T extends AbstractModel> implements DTOInterface<T> {
	@Getter
	@Setter
	@JsonProperty("id")
	private 		String 				identifier;

	@Getter(onMethod = @__(@JsonIgnore))
	@Setter
	@JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
	private			OffsetDateTime		createdDate;

	@Getter(onMethod = @__(@JsonIgnore))
	@Setter
	@JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
	private 		OffsetDateTime		updatedDate;

	@SuppressWarnings("unchecked")
	public AbstractDTO(T entity) {
		if (entity != null) {
			MapperFactory		factory		= new DefaultMapperFactory.Builder().mapNulls(false).build();
			BoundMapperFacade	mapper		= factory.getMapperFacade(entity.getClass(), getClass());
			mapper.map(entity, this);
		}
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	@Override
	public T getModel() {
		MapperFactory		factory		= new DefaultMapperFactory.Builder().mapNulls(false).build();
		BoundMapperFacade	mapper		= factory.getMapperFacade(this.getClass(), ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
		return (T) mapper.map(this);
	}
}
