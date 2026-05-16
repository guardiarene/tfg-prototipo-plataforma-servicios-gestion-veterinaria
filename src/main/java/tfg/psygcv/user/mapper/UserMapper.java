package tfg.psygcv.user.mapper;

import tfg.psygcv.user.command.CreateAdminUserCommand;
import tfg.psygcv.user.command.CreateStaffCommand;
import tfg.psygcv.user.command.RegisterCustomerCommand;
import tfg.psygcv.user.command.UpdateAdminUserCommand;
import tfg.psygcv.user.command.UpdateUserProfileCommand;
import tfg.psygcv.user.dto.request.CreateAdminUserRequest;
import tfg.psygcv.user.dto.request.CreateStaffRequest;
import tfg.psygcv.user.dto.request.CreateUserRequest;
import tfg.psygcv.user.dto.request.UpdateAdminUserRequest;
import tfg.psygcv.user.dto.request.UpdateUserRequest;
import tfg.psygcv.user.dto.response.UserResponse;
import tfg.psygcv.user.dto.response.UserSummaryResponse;
import tfg.psygcv.user.entity.User;

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

  public static RegisterCustomerCommand toRegisterCustomerCommand(CreateUserRequest request) {
    return RegisterCustomerCommand.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(request.getPassword())
        .phone(request.getPhone())
        .build();
  }

  public static CreateAdminUserCommand toCreateAdminCommand(CreateAdminUserRequest request) {
    return CreateAdminUserCommand.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(request.getPassword())
        .phone(request.getPhone())
        .role(request.getRole())
        .build();
  }

  public static UpdateAdminUserCommand toUpdateAdminCommand(UpdateAdminUserRequest request) {
    return UpdateAdminUserCommand.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(request.getPassword())
        .phone(request.getPhone())
        .role(request.getRole())
        .active(request.getActive())
        .build();
  }

  public static UpdateUserProfileCommand toUpdateProfileCommand(UpdateUserRequest request) {
    return UpdateUserProfileCommand.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .phone(request.getPhone())
        .build();
  }

  public static CreateStaffCommand toCreateStaffCommand(CreateStaffRequest request) {
    return CreateStaffCommand.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(request.getPassword())
        .phone(request.getPhone())
        .role(request.getRole())
        .build();
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
