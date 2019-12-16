package com.linzx.integrate.thirdpay.exception;

public class PayErrorException extends RuntimeException {

    private PayError error;

    public PayErrorException(PayError error) {
        super(error.getString());
        this.error = error;
    }


    public PayError getPayError() {
        return error;
    }

}
