package client.exception;

public class ResponseException extends RuntimeException {

    final private Code code;

    public enum Code {
        ServerError,
        ClientError,
    }

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }
}
