package pillihuaman.com.pe.security.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pillihuaman.com.pe.security.dto.RespUser;
import pillihuaman.com.pe.security.entity.user.User;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    RespUser toRespUser(User user);

    List<RespUser> usersToRespUsers(List<User> users);
}
