package com.server.ptitFood.common.validations.fullname;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;


public class VietnameseFullNameConstraintValidator implements ConstraintValidator<ValidVietnameseFullName, String> {
    @Override
    public void initialize(ValidVietnameseFullName arg0) {
    }

    @Override
    public boolean isValid(String fullName, ConstraintValidatorContext context) {

        final String regex = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_]+$";

        Pattern pattern = Pattern.compile(regex);

        if (fullName == null || fullName.isEmpty()) {
            return false;
        }

        if (pattern.matcher(fullName).matches()) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(
                        String.join(" ", "Vietnamese Full Name is invalid."))
                .addConstraintViolation();

        return false;
    }
}
