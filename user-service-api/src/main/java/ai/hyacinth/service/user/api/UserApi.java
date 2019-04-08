package ai.hyacinth.service.user.api;

import ai.hyacinth.core.service.api.support.config.ServiceApiConfig;
import ai.hyacinth.service.user.model.UserAuthenticationRequest;
import ai.hyacinth.service.user.model.UserCreationRequest;
import ai.hyacinth.service.user.model.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service", configuration = ServiceApiConfig.class)
public interface UserApi {
  @RequestMapping(value = "/users", method = RequestMethod.POST)
  UserInfo createUser(@Validated @RequestBody UserCreationRequest userCreationRequest);

  @RequestMapping(value = "/users", method = RequestMethod.GET)
  UserInfo findUser(@RequestParam String name);

  @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
  UserInfo getUser(@PathVariable Long userId);
}
