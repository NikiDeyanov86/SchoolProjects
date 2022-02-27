package org.elsys.ip.web.model.validator;

import org.elsys.ip.model.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoomNameAlreadyExists implements ConstraintValidator<ValidateRoomNameExists, String> {
    @Autowired
    private RoomRepository repository;

    @Override
    public void initialize(ValidateRoomNameExists constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context){
        return repository.findByName(name).isEmpty();
    }
}
