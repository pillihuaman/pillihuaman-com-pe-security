package pillihuaman.com.pe.security.repository;



import pillihuaman.com.pe.security.dto.ReqControl;
import pillihuaman.com.pe.security.entity.control.Control;
import pillihuaman.com.pe.security.entity.user.User;
import pillihuaman.com.pe.security.util.MyJsonWebToken;

import java.util.List;

public interface ControlRepository extends BaseMongoRepository<Control> {

	List<Control> listControl(ReqControl reqControl);
	Control saveControl(Control reqControl, MyJsonWebToken to);
	List<Control> findByUser(User user);

}
