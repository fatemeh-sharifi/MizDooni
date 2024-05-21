package Model.Response;

import Entity.User.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private UserEntity user;
    public LoginResponse(String token, UserEntity user){
        this.token = token;
        this.user = user;
    }
}

