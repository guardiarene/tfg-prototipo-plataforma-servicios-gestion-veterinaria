package tfg.psygcv.config.constant;

public final class RouteConstant {

  public static final String REDIRECT_LOGIN = "redirect:/users/login";

  public static final String REDIRECT_LOGIN_REGISTERED = "redirect:/users/login?registered";
  public static final String REDIRECT_LOGIN_LOGOUT = "redirect:/users/login?logout";
  public static final String REDIRECT_ADMIN_DASHBOARD = "redirect:/admin/dashboard";
  public static final String REDIRECT_VETERINARIAN_DASHBOARD = "redirect:/veterinarian/dashboard";
  public static final String REDIRECT_RECEPTIONIST_DASHBOARD = "redirect:/receptionist/dashboard";
  public static final String REDIRECT_MY_PROFILE = "redirect:/profile";
  public static final String REDIRECT_MY_PETS_CREATED = "redirect:/pets?created";
  public static final String REDIRECT_MY_PETS_UPDATED = "redirect:/pets?updated";
  public static final String REDIRECT_MY_PETS_DELETED = "redirect:/pets?deleted";
  public static final String REDIRECT_MY_APPOINTMENTS = "redirect:/appointments";
  public static final String REDIRECT_MY_SERVICES_CREATED = "redirect:/medical-services?created";
  public static final String REDIRECT_MY_SERVICES_UPDATED = "redirect:/medical-services?updated";
  public static final String REDIRECT_MY_SERVICES_DELETED = "redirect:/medical-services?deleted";
  public static final String REDIRECT_MY_CLINIC_UPDATED = "redirect:/veterinarian/clinic?updated";
  public static final String VIEW_USER_REGISTER = "users/register";
  public static final String VIEW_USER_LOGIN = "users/login";
  public static final String VIEW_ADMIN_NEW_USER = "admin/new_user";
  public static final String VIEW_ADMIN_EDIT_USER = "admin/edit_user";
  public static final String VIEW_ADMIN_DASHBOARD = "admin/dashboard";
  public static final String VIEW_ACCESS_DENIED = "error/access_denied";

  private RouteConstant() {}
}
