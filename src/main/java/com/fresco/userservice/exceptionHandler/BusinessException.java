package com.fresco.userservice.exceptionHandler;



import com.fresco.userservice.exceptionHandler.enums.ErrorCodes;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Data
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 2457189159617736175L;
    private final HttpStatus status;
    private String message;
    private String code;
    private ErrorCodes errorCode;
    private Object[] args;

    private MessageSource messageSource;

    public BusinessException(final Throwable ex, final String message, final HttpStatus status) {
        super(message, ex);
        this.status = status;
        this.message = message;
        this.code = "ERROR0000";
    }

    public BusinessException(final Throwable ex, final String message, final String code, final HttpStatus status) {
        super(message, ex);
        this.status = status;
        this.message = message;
        this.code = null;
    }

    public BusinessException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
        this.code = "ERROR0000";
    }

    public BusinessException(final ErrorCodes errorCode, final HttpStatus status) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = errorCode.name();
        this.code = errorCode.getValue();
    }

    public BusinessException(final ErrorCodes errorCode, Object[] args, final HttpStatus status) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = errorCode.name();
        this.code = errorCode.getValue();
        this.args = args;
    }

    public BusinessException(final String message, final String code, final HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public BusinessException(final Throwable ex, final String message, final Integer status) {
        super(message, ex);
        HttpStatus resolvedStatus = HttpStatus.resolve(status);
        this.status = resolvedStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : resolvedStatus;
        this.message = message;
        this.code = "ERROR0000";
    }

    public BusinessException(final Throwable ex, final String message) {
        super(message, ex);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
        this.code = "ERROR0000";
    }

    public BusinessException(final String message, final Integer status) {
        super(message);
        HttpStatus resolvedStatus = HttpStatus.resolve(status);
        this.status = resolvedStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : resolvedStatus;
        this.message = message;
        this.code = "ERROR0000";
    }

    public BusinessException(final Throwable ex) {
        super(ex);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = ex.getMessage();
        this.code = "ERROR0000";
    }


//    public HttpStatus getStatus() {
//        return this.status;
//    }
//
//    public String getMessage() {
//        return this.message;
//    }
//
//    public String getCode() {
//        return this.code;
//    }
//
//    public ErrorCodes getErrorCode() {
//        return this.errorCode;
//    }
//
//    public Object[] getArgs() {
//        return this.args;
//    }
//
//    public void setMessage(final String message) {
//        this.message = message;
//    }
//
//    public void setCode(final String code) {
//        this.code = code;
//    }
//
//    public void setErrorCode(final ErrorCodes errorCode) {
//        this.errorCode = errorCode;
//    }
//
//    public void setArgs(final Object[] args) {
//        this.args = args;
//    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BusinessException)) {
            return false;
        } else {
            BusinessException other = (BusinessException)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label63: {
                    Object this$status = this.getStatus();
                    Object other$status = other.getStatus();
                    if (this$status == null) {
                        if (other$status == null) {
                            break label63;
                        }
                    } else if (this$status.equals(other$status)) {
                        break label63;
                    }

                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                label42: {
                    Object this$errorCode = this.getErrorCode();
                    Object other$errorCode = other.getErrorCode();
                    if (this$errorCode == null) {
                        if (other$errorCode == null) {
                            break label42;
                        }
                    } else if (this$errorCode.equals(other$errorCode)) {
                        break label42;
                    }

                    return false;
                }

                if (!Arrays.deepEquals(this.getArgs(), other.getArgs())) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BusinessException;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $errorCode = this.getErrorCode();
        result = result * 59 + ($errorCode == null ? 43 : $errorCode.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getArgs());
        return result;
    }

    public String toString() {
        HttpStatus var10000 = this.getStatus();
        return "BusinessException(status=" + var10000 + ", message=" + this.getMessage() + ", code=" + this.getCode() + ", errorCode=" + this.getErrorCode() + ", args=" + Arrays.deepToString(this.getArgs()) + ")";
    }
}

