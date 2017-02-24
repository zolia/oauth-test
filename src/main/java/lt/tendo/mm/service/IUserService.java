package lt.tendo.mm.service;

import lt.tendo.mm.model.Device;
import lt.tendo.mm.model.User;
import lt.tendo.mm.model.dto.DeviceDto;
import lt.tendo.mm.model.dto.UserDto;
import lt.tendo.mm.restapi.errors.DeviceAlreadyExistException;
import lt.tendo.mm.restapi.errors.UserAlreadyExistException;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

    void saveRegisteredUser(User user);

    Device registerNewUserDevice(DeviceDto deviceDto)  throws DeviceAlreadyExistException;
}
