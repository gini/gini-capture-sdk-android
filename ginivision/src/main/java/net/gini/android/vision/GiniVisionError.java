package net.gini.android.vision;

import android.os.Parcel;
import android.os.Parcelable;

public class GiniVisionError implements Parcelable {

    public enum ErrorCode {
    }

    private final ErrorCode mErrorCode;
    private final String mMessage;

    public GiniVisionError(ErrorCode code, String message) {
        mErrorCode = code;
        mMessage = message;
    }

    public ErrorCode getErrorCode() {
        return mErrorCode;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mErrorCode.ordinal());
        dest.writeString(mMessage);
    }

    public static final Creator<GiniVisionError> CREATOR = new Creator<GiniVisionError>() {
        @Override
        public GiniVisionError createFromParcel(Parcel in) {
            return new GiniVisionError(in);
        }

        @Override
        public GiniVisionError[] newArray(int size) {
            return new GiniVisionError[size];
        }
    };

    protected GiniVisionError(Parcel in) {
        mErrorCode = ErrorCode.values()[in.readInt()];
        mMessage = in.readString();
    }
}
