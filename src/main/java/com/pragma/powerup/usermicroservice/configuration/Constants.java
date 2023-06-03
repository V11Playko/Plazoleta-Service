package com.pragma.powerup.usermicroservice.configuration;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final Long CLIENT_ROLE_ID = 1L;
    public static final Long EMPLOYEE_ROLE_ID = 2L;
    public static final Long PROVIDER_ROLE_ID = 3L;
    public static final int MAX_PAGE_SIZE = 2;
    public static final String RESPONSE_MESSAGE_KEY = "message";
    public static final String PERSON_CREATED_MESSAGE = "Person created successfully";
    public static final String USER_CREATED_MESSAGE = "User created successfully";
    public static final String USER_DELETED_MESSAGE = "User deleted successfully";
    public static final String RESPONSE_ERROR_MESSAGE_KEY = "error";
    public static final String WRONG_CREDENTIALS_MESSAGE = "Wrong credentials";
    public static final String NO_DATA_FOUND_MESSAGE = "No data found.";
    public static final String PERSON_ALREADY_EXISTS_MESSAGE = "A person already exists with the DNI number provided";
    public static final String MAIL_ALREADY_EXISTS_MESSAGE = "A person with that mail already exists";
    public static final String PERSON_NOT_FOUND_MESSAGE = "No person found with the id provided";
    public static final String ROLE_NOT_FOUND_MESSAGE = "No role found with the id provided";
    public static final String ROLE_NOT_ALLOWED_MESSAGE = "No permission granted to create users with this role";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "A user already exists with the role provided";
    public static final String USER_NOT_FOUND_MESSAGE = "No user found with the role provided";
    public static final String SWAGGER_TITLE_MESSAGE = "User API Pragma Power Up";
    public static final String SWAGGER_DESCRIPTION_MESSAGE = "User microservice";
    public static final String SWAGGER_VERSION_MESSAGE = "1.0.0";
    public static final String SWAGGER_LICENSE_NAME_MESSAGE = "Apache 2.0";
    public static final String SWAGGER_LICENSE_URL_MESSAGE = "http://springdoc.org";
    public static final String SWAGGER_TERMS_OF_SERVICE_MESSAGE = "http://swagger.io/terms/";
    public static final String VALIDATE_ROLE_OWNER = "Possible three errors: You don't have owner permissions, that user does not exist or the NIT already exists.";
    public static final String NIT_ALREADY_EXISTS = "This NIT already exists";
    public static final String AUTHENTICATION_EXCEPTION = "Authorization failed when trying to connect to user service";
    public static final String DATA_ALREADY_EXIST = "Data already exists";
    public static final String BAD_REQUEST = "Incorrect data to send message";
    public static final String USER_IS_NOT_OWNER_RESTAURANT = "User is not owner of the restaurant.";
    public static final String DISH_NOT_EXIST = "This dish does not exist";
    public static final String SAME_STATE = "You must change the state of the plate to a different one";
    public static final String RESTAURANT_NOT_EXIST = "The restaurant does not exist";

    public static final String ORDER_PENDING_STATE = "PENDING";
    public static final String ORDER_PREPARATION_STATE = "PREPARATION";
    public static final String ORDER_CANCELED_STATE = "CANCELED";
    public static final String ORDER_READY_STATE = "READY";
    public static final String ORDER_DELIVERED_STATE = "DELIVERED";

    public static final String USER_HAVE_A_ORDER = "The user already has an order pending, in preparation or ready.";
    public static final String ORDER_CREATED = "Order created successfully";
    public static final String RESTAURANT_NOT_HAVE_THESE_DISHES = "These dishes do not belong to this restaurant";
    public static final String EMPLOYEE_NOT_BELONG_RESTAURANT = "Employee does not belong to any restaurant";
    public static final String ORDER_NOT_EXIST = "This order does not exist";
    public static final String ORDER_ALREADY_ASSIGNED_OR_PROCESS = "Order is already assigned or in process\n";
}
