package com.hyoii.mall.exception


import com.hyoii.core.enums.MessageEnums
import com.hyoii.mall.common.res.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
//import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {


    /**
     * DTO 에 @Valid 과정 중 문제가 발생하면 떨어뜨리는 에러
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun methodArgumentNotValidException(ex: MethodArgumentNotValidException) : ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage ?: "Not Exception Message"
        }
        return ResponseEntity(BaseResponse(MessageEnums.INVALID_PARAMETER.code, MessageEnums.INVALID_PARAMETER.message, errors), HttpStatus.BAD_REQUEST)
    }

    /**
     * added InvalidInputException
     */
    @ExceptionHandler(InvalidInputException::class)
    protected fun invalidInputException(ex: InvalidInputException) : ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(MessageEnums.INVALID_PARAMETER.code, MessageEnums.INVALID_PARAMETER.message, errors), HttpStatus.BAD_REQUEST)
    }

//    /**
//     * 시큐리티 추가 (아이디 비밀번호 검증 실패)
//     */
//    @ExceptionHandler(BadCredentialsException::class)
//    protected fun badCredentialsException(ex: InvalidInputException) : ResponseEntity<BaseResponse<Map<String, String>>> {
//        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
//        return ResponseEntity(BaseResponse(MessageEnums.LOGIN_INCORRECT.code, MessageEnums.LOGIN_INCORRECT.message, errors), HttpStatus.BAD_REQUEST)
//    }

    /**
     * 나머지 exception
     */
    @ExceptionHandler(Exception::class)
    protected fun defaultException(ex: Exception) : ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("cause" to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(MessageEnums.FAIL.code, MessageEnums.FAIL.message, errors), HttpStatus.BAD_REQUEST)
    }
}
