package com.websystique.springmvc.model;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER_PROFILE")
public class UserProfile implements GrantedAuthority {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="TYPE", length=15, unique=true, nullable=false)
	private String type = UserProfileType.USER.getUserProfileType();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserProfile that = (UserProfile) o;
		return type.equals(that.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type);
	}

	@Override
	public String toString() {
		return type;
	}

	@Override
	public String getAuthority() {
		return "ROLE_" + type;
	}

	public enum UserProfileType implements Serializable{
		USER("USER"),
		DBA("DBA"),
		ADMIN("ADMIN");

		String userProfileType;

		UserProfileType(String userProfileType){
			this.userProfileType = userProfileType;
		}

		public String getUserProfileType(){
			return userProfileType;
		}

	}
}

