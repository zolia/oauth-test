package lt.tendo.mm.restapi;

import lt.tendo.mm.model.Device;
import lt.tendo.mm.model.User;
import lt.tendo.mm.model.dto.DeviceDto;
import lt.tendo.mm.model.dto.UserDto;
import lt.tendo.mm.restapi.errors.ApiError;
import lt.tendo.mm.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    @ResponseBody
    public ApiError registerUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {
        logger.info("Registering user account with information: {}", accountDto);

        final User registered = userService.registerNewUserAccount(accountDto);

        return new ApiError(HttpStatus.CREATED, "user successfully registered");
    }

    @RequestMapping(value = "/device/registration", method = RequestMethod.POST)
    @ResponseBody
    public ApiError registerDevice(@Valid final DeviceDto deviceDto, final HttpServletRequest request) {
        logger.info("Registering device with information: {}", deviceDto);

        Device registered = userService.registerNewUserDevice(deviceDto);

        return new ApiError(HttpStatus.CREATED, "device successfully registered");
    }
}