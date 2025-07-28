package pillihuaman.com.pe.security.user.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pillihuaman.com.pe.security.dto.RespUser;
import pillihuaman.com.pe.security.entity.user.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-28T09:05:25-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public RespUser toRespUser(User user) {
        if ( user == null ) {
            return null;
        }

        RespUser.RespUserBuilder respUser = RespUser.builder();

        respUser.id( user.getId() );
        respUser.alias( user.getAlias() );
        respUser.mobilPhone( user.getMobilPhone() );
        respUser.userName( user.getUserName() );
        respUser.enabled( user.isEnabled() );

        return respUser.build();
    }

    @Override
    public List<RespUser> usersToRespUsers(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<RespUser> list = new ArrayList<RespUser>( users.size() );
        for ( User user : users ) {
            list.add( toRespUser( user ) );
        }

        return list;
    }
}
