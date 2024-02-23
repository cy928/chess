package response;

import request.AuthToken;

public record userResponse(String username, AuthToken auth){
}
