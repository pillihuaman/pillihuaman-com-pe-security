package pillihuaman.com.pe.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class ReqUser {
    private String alias;
    private String email;
    private String mobilPhone;
    private String user;
    private String userName;
    private String apiPassword;
    private String password;
    private String salPassword;
    private String repeatpassword;
    private String typeDocument;
    private String numTypeDocument;
    private String lastName;
    private Integer code;
    private Boolean estatus;
    private List<RolesRequest> roles;
    private String tenantId;

}


