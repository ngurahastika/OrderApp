package com.example.orderapp.model.order;

import com.example.orderapp.model.CreatorMod;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Notification extends CreatorMod {

	@Id
	private Long id;

	private String userId;

	private String message;

	private Boolean isRead;
}
