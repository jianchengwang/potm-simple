package org.example.potm.framework.config.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.exception.ServerException;
import org.example.potm.framework.utils.FileUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class AbstractController {

    protected void write(final HttpServletResponse response, String fileName, InputStream inputStream, String mimeType) {
        try {
            if (StringUtils.isEmpty(mimeType)) {
                mimeType = FileUtils.getMimeType(fileName);
                if (mimeType == null) {
                    mimeType = FileUtils.DEFAULT_MIME_TYPE;
                }
            }
            response.setContentType(mimeType);
            response.setCharacterEncoding("utf-8");
            if(fileName == null) {
                fileName = "unknown";
            }
            fileName = java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setHeader("filename", fileName);
            response.setHeader("Access-Control-Expose-Headers", "filename");

            if (!mimeType.contains("image") && !mimeType.contains("text")) {
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            }


            if (mimeType.contains("text")) {
                writeText(inputStream, response.getWriter());
            } else {
                FileUtils.copy(inputStream, response.getOutputStream());
            }


        } catch (Exception e) {
            throw new ServerException(FrameworkErrorCode.SERVER_ERROR, e);
        }
    }

    protected void writeText(InputStream source, PrintWriter printWriter) throws IOException {
        @Cleanup InputStream ins = source;
        @Cleanup PrintWriter writer = printWriter;

        BufferedReader reader = new BufferedReader(new InputStreamReader(ins, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        writer.write(sb.toString());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(1024);
    }
}
