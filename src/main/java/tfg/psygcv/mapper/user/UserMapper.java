package tfg.psygcv.mapper.user;

import tfg.psygcv.dto.user.request.UpdateAdminUserRequest;
import tfg.psygcv.dto.user.request.UpdateUserRequest;
import tfg.psygcv.dto.user.response.UserResponse;
import tfg.psygcv.dto.user.response.UserSummaryResponse;
import tfg.psygcv.entity.user.User;

public class UserMapper {

  private UserMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static UpdateUserRequest toUpdateRequest(User user) {
    UpdateUserRequest request = new UpdateUserRequest();
    request.setFirstName(user.getFirstName());
    request.setLastName(user.getLastName());
    request.setPhone(user.getPhone());
    request.setEmail(user.getEmail());
    return request;
  }

  public static UpdateAdminUserRequest toUpdateAdminRequest(User user) {
    UpdateAdminUserRequest request = new UpdateAdminUserRequest();
    request.setFirstName(user.getFirstName());
    request.setLastName(user.getLastName());
    request.setPhone(user.getPhone());
    request.setEmail(user.getEmail());
    request.setRole(user.getRole());
    request.setActive(user.getActive());
    return request;
  }

  public static UserResponse toResponse(User user) {
    UserResponse response = new UserResponse();
    response.setId(user.getId());
    response.setFirstName(user.getFirstName());
    response.setLastName(user.getLastName());
    response.setEmail(user.getEmail());
    response.setPhone(user.getPhone());
    response.setRole(user.getRole());
    response.setActive(user.getActive());
    return response;
  }

  public static UserSummaryResponse toSummary(User user) {
    UserSummaryResponse summary = new UserSummaryResponse();
    summary.setId(user.getId());
    summary.setFirstName(user.getFirstName());
    summary.setLastName(user.getLastName());
    summary.setEmail(user.getEmail());
    summary.setPhone(user.getPhone());
    summary.setRole(user.getRole());
    return summary;
  }
}
