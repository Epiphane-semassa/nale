package com.nale.newslettermanagement.data;

public class Admin {
	
	private User user;
	private Profile profile;
	
	public Admin(User user, Profile profile) {
		super();
		this.user = user;
		this.profile = profile;
	}

	public Admin() {

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return "Admin [user=" + user + ", profile=" + profile + "]";
	}


	public static class User {
		private String username;
		private String password;
		
		public User(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		public String toString() {
			return "User [username=" + username + ", password=" + password + "]";
		}
		
	}
	
	public static class Profile {
		private String email;
		private String lastName;
		private String firstName;
		private String phoneNumber;
		private String address;
		
		public Profile(String email, String lastName, String firstName, String phoneNumber, String address) {
			super();
			this.email = email;
			this.lastName = lastName;
			this.firstName = firstName;
			this.phoneNumber = phoneNumber;
			this.address = address;
		}

		public Profile() {

		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		@Override
		public String toString() {
			return "Profile [email=" + email + ", lastName=" + lastName + ", firstName=" + firstName + ", phoneNumber="
					+ phoneNumber + ", address=" + address + "]";
		}
		
		
	}

}
