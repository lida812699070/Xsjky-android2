package cn.xsjky.android.model;

/**
 * Created by OK on 2016/4/27.
 */
public class CancelDeliveryRequest {
    private String ReturnCode;
    private String Error;
    private String StackTrace;
    private String ErrorSource;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String returnCode) {
        ReturnCode = returnCode;
    }

    public String getErrorSource() {
        return ErrorSource;
    }

    public void setErrorSource(String errorSource) {
        ErrorSource = errorSource;
    }

    public String getStackTrace() {
        return StackTrace;
    }

    public void setStackTrace(String stackTrace) {
        StackTrace = stackTrace;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    @Override
    public String toString() {
        return "CancelDeliveryRequest{" +
                "ReturnCode='" + ReturnCode + '\'' +
                ", Error='" + Error + '\'' +
                ", StackTrace='" + StackTrace + '\'' +
                ", ErrorSource='" + ErrorSource + '\'' +
                '}';
    }
}
