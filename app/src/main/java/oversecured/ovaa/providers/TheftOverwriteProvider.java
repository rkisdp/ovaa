package oversecured.ovaa.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TheftOverwriteProvider extends ContentProvider {
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        File baseDir = Environment.getExternalStorageDirectory();
        String segment = uri.getLastPathSegment();
        if (segment == null) {
            throw new IllegalArgumentException("Missing path segment");
        }
        File requested = new File(baseDir, segment);
        try {
            String baseCanonical = baseDir.getCanonicalPath();
            String requestedCanonical = requested.getCanonicalPath();
            if (!requestedCanonical.startsWith(baseCanonical + File.separator)) {
                throw new SecurityException("Path traversal attempt blocked: " + uri);
            }
        } catch (IOException e) {
            throw new SecurityException("Failed to resolve canonical path", e);
        }
        return ParcelFileDescriptor.open(requested, ParcelFileDescriptor.MODE_READ_WRITE);
    }
}
