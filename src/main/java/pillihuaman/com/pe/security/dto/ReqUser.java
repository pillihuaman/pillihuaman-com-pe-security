package pillihuaman.com.pe.security.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqUser implements  Serializable  {

	private String alias;
	private String email;
	private String mobilPhone;
	private String user;
	private String userName;
	private String apiPassword;
	private String password;
	private String salPassword;


    public void setAlias(String alias) {
		this.alias = alias;
	}

    public void setEmail(String email) {
		this.email = email;
	}

    public void setUser(String user) {
		this.user = user;
	}

    public void setMobilPhone(String mobilPhone) {
		this.mobilPhone = mobilPhone;
	}

    public void setUserName(String userName) {
		this.userName = userName;
	}

    public void setSalPassword(String salPassword) {
		this.salPassword = salPassword;
	}

    public void setPassword(String password) {
		this.password = password;
	}

    public void setApiPassword(String apiPassword) {
		this.apiPassword = apiPassword;
	}


}


