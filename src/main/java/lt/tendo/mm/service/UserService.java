package lt.tendo.mm.service;

import lt.tendo.mm.model.*;
import lt.tendo.mm.model.dto.DeviceDto;
import lt.tendo.mm.model.dto.UserDto;
import lt.tendo.mm.restapi.errors.DeviceAlreadyExistException;
import lt.tendo.mm.restapi.errors.UserAlreadyExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@Transactional
public class UserService implements IUserService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository repository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RoleRepository roleRepository;

    // API

    @Override
    public User registerNewUserAccount(final UserDto accountDto) {
        if (userExist(accountDto.getUserPhone())) {
            throw new UserAlreadyExistException("There is an account with that user phone: " + accountDto.getUserPhone());
        }
        final User user = new User();

        user.setLogin(accountDto.getUserPhone());
        user.setPassword(accountDto.getDigitsId());
        user.setEmail(accountDto.getEmail());
        user.setPlatform(accountDto.getPlatform());
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(userRole));
        return repository.save(user);
    }

    @Override
    public Device registerNewUserDevice(final DeviceDto deviceDto) {
        logger.info("UserService - registerNewUserDevice");

        Device device = deviceRepository.findByImei(deviceDto.getImei());

        if((device != null) && device.is_active()){
            throw new DeviceAlreadyExistException("There is a device with an imei already: " + deviceDto.getImei());
        }

        logger.info("AUTH: " +  SecurityContextHolder.getContext().getAuthentication());
        //logger.info("PRINCIPAL: " + (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = repository.findByLogin(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin());

        //User user = repository.findByLogin((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        device.getUsers().add(user);

        device.setImei(deviceDto.getImei());
        device.setName(deviceDto.getName());
        user.getDevices().add(device);
        return deviceRepository.save(device);
    }

    private boolean userExist(String userPhone) {
        User u = repository.findByLogin(userPhone);
        if(u == null) return false;

        if((u != null) && (u.authorized() == 0)){
            repository.delete(u.getId());
            return false;
        }
        return true;
    }


    @Override
    public void saveRegisteredUser(final User user) {
        repository.save(user);
    }

}