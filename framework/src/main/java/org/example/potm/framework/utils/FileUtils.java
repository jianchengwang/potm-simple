package org.example.potm.framework.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import lombok.Cleanup;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;

public class FileUtils {

    private static final int BUFFER_SIZE = 8192;
    public static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    public static String getMimeType(String fileName) {
        return getMimeType(MimeUtil.getMimeTypes(fileName));
    }
    public static String getMimeType(File file) {
        return getMimeType(MimeUtil.getMimeTypes(file));
    }
    public static String getMimeType(InputStream source) {
        return getMimeType((MimeUtil.getMimeTypes(new BufferedInputStream(source))));
    }
    public static String getMimeType(byte[] data) {
        return getMimeType((MimeUtil.getMimeTypes(data)));
    }
    public static String getMimeType(URL url) {
        return getMimeType(MimeUtil.getMimeTypes(url));
    }
    private static String getMimeType(Collection<MimeType> mimeTypes) {
        if(CollectionUtils.isNotEmpty(mimeTypes)) {
            Optional<MimeType> mimeTypeOpt = mimeTypes.stream().findFirst();
            if(mimeTypeOpt.isPresent()) {
                return mimeTypeOpt.get().toString();
            }
        }
        return DEFAULT_MIME_TYPE;
    }
    public static long copy(InputStream source, OutputStream sink)
            throws IOException {
        @Cleanup InputStream ins = source;
        @Cleanup OutputStream os = sink;
        long totalBytes = 0L;
        byte[] buf = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = ins.read(buf)) > 0) {
            os.write(buf, 0, bytesRead);
            totalBytes += bytesRead;
        }
        return totalBytes;
    }

    /**
     * 从InputStream获取File
     *
     * @param inputStream
     * @param file
     * @throws IOException
     */
    public static void inputStreamToFile(InputStream inputStream, File file) throws IOException {
        @Cleanup InputStream ins = inputStream;
        @Cleanup OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = ins.read(buffer, 0, BUFFER_SIZE)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        @Cleanup InputStream ins = inputStream;
        @Cleanup ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[BUFFER_SIZE];
        int i;
        while ((i = ins.read(bytes)) != -1) {
            baos.write(bytes, 0, i);
        }
        return baos.toByteArray();
    }
}