package uk.mushow.safetynet.exceptions;

public class PersonNotFoundException extends IllegalArgumentException {

    public PersonNotFoundException(String message) {
        super(message);
    }

}