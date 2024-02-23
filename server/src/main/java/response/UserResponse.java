package response;

import request.AuthToken;

public record UserResponse(String username, AuthToken auth){
}
