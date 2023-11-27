package com.server.ptitFood.domain.exceptions;

public class UsernameOrPasswordNotValid extends Exception{

        public UsernameOrPasswordNotValid(String message) {
            super(message);
        }

        public UsernameOrPasswordNotValid(String message, Throwable cause) {
            super(message, cause);
        }

        public UsernameOrPasswordNotValid(Throwable cause) {
            super(cause);
        }
}
