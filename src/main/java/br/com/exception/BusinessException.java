package br.com.exception;


public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;

    public BusinessException () {
        super();
    }

    public BusinessException (String msg , Throwable e) {
        super(msg , e);
    }

    public BusinessException (String msg) {
        super(msg);
    }
}
