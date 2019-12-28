package com.websystique.springmvc.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="APP_USER")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Name is mandatory")
	@Column(name="SSO_ID", unique=true, nullable=false)
	private String ssoId;

	@NotBlank(message = "PASSWORD is mandatory")
	@Column(name="PASSWORD", nullable=false)
	private String password;

//	@NotBlank(message = "FIRST_NAME is mandatory")
	@Column(name="FIRST_NAME" )
	private String firstName;

//	@NotBlank(message = "LAST_NAME is mandatory")
	@Column(name="LAST_NAME" )
	private String lastName;

	@NotBlank(message = "EMAIL is mandatory")
	@Column(name="EMAIL", nullable=false)
	private String email;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "APP_USER_USER_PROFILE", 
             joinColumns = { @JoinColumn(name = "USER_ID") }, 
             inverseJoinColumns = { @JoinColumn(name = "USER_PROFILE_ID") })
	private Set<UserProfile> userProfiles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSsoId() {
		return ssoId;
	}

	public void setSsoId(String ssoId) {
		this.ssoId = ssoId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userProfiles;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return ssoId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<UserProfile> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}

	public String rolesDescription() {
		return userProfiles.stream().map(c -> c.toString().toLowerCase()).collect(Collectors.joining(", "));
	}

	public String roleEnabled(Long roleId) {
		return userProfiles.stream().map(c -> c.getId().equals(roleId)).anyMatch(Boolean::booleanValue) ? "THIS_ROLE_IS_ENABLED" : "";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return ssoId.equals(user.ssoId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ssoId);
	}

	/*
	 * DO-NOT-INCLUDE passwords in toString function.
	 * It is done here just for convenience purpose.
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", ssoId=" + ssoId + ", password=" + password
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + "]";
	}


	
}
