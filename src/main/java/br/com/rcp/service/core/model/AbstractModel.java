package br.com.rcp.service.core.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@MappedSuperclass
@IdClass(Identifier.class)
public abstract class AbstractModel implements Serializable {
	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(insertable = false, updatable = false)
	private		Long			sequence;

	@Getter
	@Setter
	@Id
	@GenericGenerator(name = "IDGenerator", strategy = "IDGenerator")
	@GeneratedValue(generator = "IDGenerator")
	@Column(insertable = false, updatable = false, length = 20)
	private		String			identifier;

	@Getter
	@Setter
	@CreatedDate
	private		OffsetDateTime	createdDate;

	@Getter
	@Setter
	@LastModifiedDate
	private 	OffsetDateTime	updatedDate;

	@Getter
	@Setter
	@CreatedBy
	private		Long			createdByID;

	@Getter
	@Setter
	@LastModifiedBy
	private		Long			modifiedByID;

	@PrePersist
	protected void prePersist() {
		//
	}

	@PreUpdate
	protected void preUpdate() {
		//
	}

	@PreRemove
	protected void preRemove() {
		//
	}

	@PostPersist
	protected void postPersist() {
		//
	}

	@PostUpdate
	protected void postUpdate() {
		//
	}

	@PostRemove
	protected void postRemove() {
		//
	}

	@PostLoad
	protected void postLoad() {
		//
	}
}
