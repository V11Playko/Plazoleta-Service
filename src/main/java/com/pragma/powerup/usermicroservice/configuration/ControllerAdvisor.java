package com.pragma.powerup.usermicroservice.configuration;

import com.pragma.powerup.usermicroservice.adapters.driven.client.exceptions.AuthenticationException;
import com.pragma.powerup.usermicroservice.adapters.driven.client.exceptions.BadRequestException;
import com.pragma.powerup.usermicroservice.adapters.driven.client.exceptions.DataExistException;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.MailAlreadyExistsException;
import com.pragma.powerup.usermicroservice.domain.exceptions.CancelOrderErrorException;
import com.pragma.powerup.usermicroservice.domain.exceptions.DishNotExistException;
import com.pragma.powerup.usermicroservice.domain.exceptions.EmployeeNotBelongAnyRestaurant;
import com.pragma.powerup.usermicroservice.domain.exceptions.NitAlreadyExists;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.NoDataFoundException;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.PersonAlreadyExistsException;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.PersonNotFoundException;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.RoleNotAllowedForCreationException;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.RoleNotFoundException;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserAlreadyExistsException;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserNotFoundException;
import com.pragma.powerup.usermicroservice.domain.exceptions.NotificationNotSend;
import com.pragma.powerup.usermicroservice.domain.exceptions.OrderAssignedOrProcessException;
import com.pragma.powerup.usermicroservice.domain.exceptions.OrderNotExist;
import com.pragma.powerup.usermicroservice.domain.exceptions.OrderStateCannotChange;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantHaveOrdersPending;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotExist;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotHaveTheseDishes;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantPendingDeleteException;
import com.pragma.powerup.usermicroservice.domain.exceptions.SameStateException;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserHaveOrderException;
import com.pragma.powerup.usermicroservice.domain.exceptions.SecurityCodeIncorrectException;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.exceptions.ValidateRoleOwner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.pragma.powerup.usermicroservice.configuration.Constants.AUTHENTICATION_EXCEPTION;
import static com.pragma.powerup.usermicroservice.configuration.Constants.BAD_REQUEST;
import static com.pragma.powerup.usermicroservice.configuration.Constants.CANCEL_ORDER_ERROR;
import static com.pragma.powerup.usermicroservice.configuration.Constants.DATA_ALREADY_EXIST;
import static com.pragma.powerup.usermicroservice.configuration.Constants.DISH_NOT_EXIST;
import static com.pragma.powerup.usermicroservice.configuration.Constants.EMPLOYEE_NOT_BELONG_RESTAURANT;
import static com.pragma.powerup.usermicroservice.configuration.Constants.MAIL_ALREADY_EXISTS_MESSAGE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.NIT_ALREADY_EXISTS;
import static com.pragma.powerup.usermicroservice.configuration.Constants.NOTIFICATION_NOT_SEND;
import static com.pragma.powerup.usermicroservice.configuration.Constants.NO_DATA_FOUND_MESSAGE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.ORDER_ALREADY_ASSIGNED_OR_PROCESS;
import static com.pragma.powerup.usermicroservice.configuration.Constants.ORDER_NOT_EXIST;
import static com.pragma.powerup.usermicroservice.configuration.Constants.ORDER_STATE_CANNOT_CHANGE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.PERSON_ALREADY_EXISTS_MESSAGE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.PERSON_NOT_FOUND_MESSAGE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.RESPONSE_ERROR_MESSAGE_KEY;
import static com.pragma.powerup.usermicroservice.configuration.Constants.RESTAURANT_HAVE_ORDERS_PENDING;
import static com.pragma.powerup.usermicroservice.configuration.Constants.RESTAURANT_NOT_EXIST;
import static com.pragma.powerup.usermicroservice.configuration.Constants.RESTAURANT_NOT_HAVE_THESE_DISHES;
import static com.pragma.powerup.usermicroservice.configuration.Constants.RESTAURANT_PENDING_DELETE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.ROLE_NOT_ALLOWED_MESSAGE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.ROLE_NOT_FOUND_MESSAGE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.SAME_STATE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.SECURITY_CODE_INCORRECT;
import static com.pragma.powerup.usermicroservice.configuration.Constants.USER_HAVE_A_ORDER;
import static com.pragma.powerup.usermicroservice.configuration.Constants.USER_ALREADY_EXISTS_MESSAGE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.USER_IS_NOT_OWNER_RESTAURANT;
import static com.pragma.powerup.usermicroservice.configuration.Constants.USER_NOT_FOUND_MESSAGE;
import static com.pragma.powerup.usermicroservice.configuration.Constants.VALIDATE_ROLE_OWNER;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errorMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(NoDataFoundException noDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, NO_DATA_FOUND_MESSAGE));
    }
    @ExceptionHandler(PersonAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handlePersonAlreadyExistsException(
            PersonAlreadyExistsException personAlreadyExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, PERSON_ALREADY_EXISTS_MESSAGE));
    }

    @ExceptionHandler(MailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleMailAlreadyExistsException(
            MailAlreadyExistsException mailAlreadyExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, MAIL_ALREADY_EXISTS_MESSAGE));
    }
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePersonNotFoundException(
            PersonNotFoundException personNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, PERSON_NOT_FOUND_MESSAGE));
    }
    @ExceptionHandler(RoleNotAllowedForCreationException.class)
    public ResponseEntity<Map<String, String>> handleRoleNotAllowedForCreationException(
            RoleNotAllowedForCreationException roleNotAllowedForCreationException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ROLE_NOT_ALLOWED_MESSAGE));
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(
            UserAlreadyExistsException userAlreadyExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, USER_ALREADY_EXISTS_MESSAGE));
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(
            UserNotFoundException userNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, USER_NOT_FOUND_MESSAGE));
    }
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRoleNotFoundException(
            RoleNotFoundException roleNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ROLE_NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(ValidateRoleOwner.class)
    public ResponseEntity<Map<String, String>> handleOwnerRoleValidation(
            ValidateRoleOwner validateRoleOwner) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, VALIDATE_ROLE_OWNER));
    }

    @ExceptionHandler(NitAlreadyExists.class)
    public ResponseEntity<Map<String, String>> handleNitAlreadyExist(
            NitAlreadyExists nitAlreadyExists) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, NIT_ALREADY_EXISTS));

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(
            AuthenticationException authenticationException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, AUTHENTICATION_EXCEPTION));
    }

    @ExceptionHandler(DataExistException.class)
    public ResponseEntity<Map<String, String>> hadnleDataExistException(
            DataExistException dataExistException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, DATA_ALREADY_EXIST));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(
            BadRequestException badRequestException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, BAD_REQUEST));
    }

    @ExceptionHandler(UserNotIsOwner.class)
    public ResponseEntity<Map<String, String>> hadnleUserNotIsOwner(
            UserNotIsOwner userNotIsOwner) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, USER_IS_NOT_OWNER_RESTAURANT));
    }

    @ExceptionHandler(DishNotExistException.class)
    public ResponseEntity<Map<String, String>> handleDishNotExist(
            DishNotExistException dishNotExistException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, DISH_NOT_EXIST));
    }

    @ExceptionHandler(SameStateException.class)
    public ResponseEntity<Map<String, String>> handleSameState(
            SameStateException sameStateException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, SAME_STATE));
    }

    @ExceptionHandler(RestaurantNotExist.class)
    public ResponseEntity<Map<String, String>> handleRestaurantNotExist(
            RestaurantNotExist restaurantNotExist) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, RESTAURANT_NOT_EXIST));
    }

    @ExceptionHandler(UserHaveOrderException.class)
    public ResponseEntity<Map<String, String>> handleUserHaveAOrder(
            UserHaveOrderException userHaveOrderException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, USER_HAVE_A_ORDER));
    }

    @ExceptionHandler(RestaurantNotHaveTheseDishes.class)
    public ResponseEntity<Map<String, String>> handleRestaurantNotHaveTheseDishes(
            RestaurantNotHaveTheseDishes restaurantNotHaveTheseDishes) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, RESTAURANT_NOT_HAVE_THESE_DISHES));
    }

    @ExceptionHandler(EmployeeNotBelongAnyRestaurant.class)
    public ResponseEntity<Map<String, String>> handleEmployeeDoesNotBelongAnyRestaurant(
            EmployeeNotBelongAnyRestaurant employeeNotBelongAnyRestaurant) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, EMPLOYEE_NOT_BELONG_RESTAURANT));
    }

    @ExceptionHandler(OrderNotExist.class)
    public ResponseEntity<Map<String, String>> handleOrderNotExist(
            OrderNotExist orderNotExist) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ORDER_NOT_EXIST));
    }

    @ExceptionHandler(OrderAssignedOrProcessException.class)
    public ResponseEntity<Map<String, String>> handleOrderAssignedOrProcessException(
            OrderAssignedOrProcessException orderAssignedOrProcessException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ORDER_ALREADY_ASSIGNED_OR_PROCESS));
    }

    @ExceptionHandler(OrderStateCannotChange.class)
    public ResponseEntity<Map<String, String>> handleOrderStateCannotChange(
            OrderStateCannotChange orderStateCannotChange) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ORDER_STATE_CANNOT_CHANGE));
    }

    @ExceptionHandler(NotificationNotSend.class)
    public ResponseEntity<Map<String, String>> handleNotificationNotSend(
            NotificationNotSend notificationNotSend) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, NOTIFICATION_NOT_SEND));
    }

    @ExceptionHandler(SecurityCodeIncorrectException.class)
    public ResponseEntity<Map<String, String>> handleSecurityCodeIncorrect(
            SecurityCodeIncorrectException securityCodeIncorrectException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, SECURITY_CODE_INCORRECT));
    }

    @ExceptionHandler(CancelOrderErrorException.class)
    public ResponseEntity<Map<String, String>> handleCancelOrderError(
            CancelOrderErrorException cancelOrderErrorException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, CANCEL_ORDER_ERROR));
    }

    @ExceptionHandler(RestaurantPendingDeleteException.class)
    public ResponseEntity<Map<String, String>> handlePendingDeleteRestaurant(
            RestaurantPendingDeleteException restaurantPendingDeleteException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, RESTAURANT_PENDING_DELETE));
    }

    @ExceptionHandler(RestaurantHaveOrdersPending.class)
    public ResponseEntity<Map<String, String>> handleRestaurantOrdersPendingException(
            RestaurantHaveOrdersPending restaurantHaveOrdersPending) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, RESTAURANT_HAVE_ORDERS_PENDING));
    }
}
