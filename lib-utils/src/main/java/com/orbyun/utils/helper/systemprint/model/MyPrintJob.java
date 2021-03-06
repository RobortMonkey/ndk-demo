package com.orbyun.utils.helper.systemprint.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Author: jrodriguezg
 * Date: 03/05/2016.
 */
public class MyPrintJob implements Parcelable {
    private String mUri;
    private String mFilename;
    private PrintingConstants.FitMode mFitMode;
    private PrintingConstants.JobType mMimeType;

    public MyPrintJob() {
        mFitMode = PrintingConstants.FitMode.PRINT_FIT_TO_PAGE;
        mMimeType = PrintingConstants.JobType.DOCUMENT;
    }

    protected MyPrintJob(Parcel in) {
        mUri = in.readString();
        mFilename = in.readString();
        mMimeType = (PrintingConstants.JobType) in.readSerializable();
        mFitMode = (PrintingConstants.FitMode) in.readSerializable();
    }

    /* Parcelable implementation */
    public static final Creator<MyPrintJob> CREATOR = new Creator<MyPrintJob>() {
        @Override
        public MyPrintJob createFromParcel(Parcel in) {
            return new MyPrintJob(in);
        }

        @Override
        public MyPrintJob[] newArray(int size) {
            return new MyPrintJob[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUri);
        dest.writeString(mFilename);
        dest.writeSerializable(mMimeType);
        dest.writeSerializable(mFitMode);
    }

    /* Parcelable implementation - END */

    public void setUri(String uri) {
        mUri = uri;
    }

    public String getUri() {
        return mUri;
    }

    public String getFilename() {
        return mFilename;
    }

    public void setFilename(String filename) {
        mFilename = filename;
    }

    public void setMimeType(PrintingConstants.JobType mimeType) {
        mMimeType = mimeType;
    }

    public PrintingConstants.JobType getMimeType() {
        return mMimeType;
    }

    public void setFitMode(PrintingConstants.FitMode fitMode) {
        mFitMode = fitMode;
    }

    public PrintingConstants.FitMode getFitMode() {
        return mFitMode;
    }

    public boolean isValid() {
        try {
            // 1. Verify filename and URI are consistent. Check file exists:
            if (!verifyFileIntegrity()) return false;

            // 2. Verify Job type and file extension are consistent:
            // 3. Verify Job type and fit mode are consistent:
            // 4. Verify Job type and Margins are consistent:
            switch (getMimeType()) {
                case DOCUMENT:
                    if (!getFilename().toUpperCase().endsWith(".PDF"))
                        return false;
                    break;

                case IMAGE:
                    if (!(getFilename().toUpperCase().endsWith(".JPG") || getFilename().toUpperCase().endsWith(".JPEG")
                            || getFilename().toUpperCase().endsWith(".PNG")))
                        return false;
                    else {
                        if (getFitMode().equals(PrintingConstants.FitMode.PASS_PDF_AS_IS))
                            return false;
                        else if (getFitMode().equals(PrintingConstants.FitMode.PRINT_CLIP_CONTENT))
                            return false;
                    }
                    break;
            }

        // If any exception occurs, return false:
        }catch (Exception ex) {
            return false;
        }

        return true;
    }

    /**
     * Verifies file integrity.
     * @return true if file exists and URI matches filename.
     */
    public boolean verifyFileIntegrity() {
        return ((getUri().endsWith(getFilename())) && fileExists());
    }

    private boolean fileExists() {
        File aFile = new File(mUri);
        return aFile.exists();
    }
}
