package com.mysite.sbb.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true) // 중복을 허용하지 않음, 유일값만 저장할 수 있다.
	private String username;
	
	private String password;
	
	@Column(unique = true) // 중복을 허용하지 않음, 유일값만 저장할 수 있다.
	private String email;

}
