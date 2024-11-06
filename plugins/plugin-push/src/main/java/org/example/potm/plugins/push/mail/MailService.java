package org.example.potm.plugins.push.mail;

import cn.hutool.core.collection.CollectionUtil;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.plugins.push.mail.constant.MailErrorCode;
import org.example.potm.plugins.push.mail.pojo.AttachObject;
import org.example.potm.plugins.push.mail.pojo.InlineResourceObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.File;
import java.util.*;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Slf4j
public class MailService {
    private boolean useMime; // 是否是Mime邮件
    private String subject; // 邮件主题
    private String from; // 邮件发送者
    private final Set<String> to; // 邮件接收者
    private final Set<String> cc; // 抄送者
    private String text; // 简单文本内容
    private String html; // 网页内容邮件
    private String template; // 邮件模板路径，目前模板支持freemarker
    private Map<String,Object> templateParam;
    private final List<AttachObject> attachs; // 附件列表
    private final List<InlineResourceObject> rscs; // 内链静态资源列表，一般图片地址

    private final JavaMailSender mailSender;
    private final FreeMarkerConfigurer templateConfig;
    public MailService(JavaMailSender mailSender, FreeMarkerConfigurer templateConfig) {
        this.mailSender = mailSender;
        this.useMime = false;
        this.to = new HashSet<>();
        this.cc = new HashSet<>();
        this.templateParam = new HashMap<>();
        this.attachs = new ArrayList<>();
        this.rscs = new ArrayList<>();
        this.templateConfig = templateConfig;
    }

    public static MailService create(JavaMailSender mailSender, FreeMarkerConfigurer templateConfig) {
        return new MailService(mailSender, templateConfig);
    }

    public MailService useMime() {
        this.useMime = true;
        return this;
    }
    public MailService subject(String subject) {
        this.subject = subject;
        return this;
    }
    public MailService from(String from) {
        this.from = from;
        return this;
    }
    public MailService to(String to) {
        this.to.add(to);
        return this;
    }
    public MailService to(String... to) {
        this.to.addAll(Arrays.asList(to));
        return this;
    }
    public MailService cc(String cc) {
        this.cc.add(cc);
        return this;
    }
    public MailService cc(String... cc) {
        if(cc!=null && cc.length>0) {
            this.cc.addAll(Arrays.asList(cc));
        }
        return this;
    }
    public MailService text(String text) {
        this.text = text;
        return this;
    }
    public MailService html(String html) {
        this.html = html;
        this.useMime = true;
        return this;
    }
    public MailService template(String template) {
        this.template = template;
        this.useMime = true;
        return this;
    }
    public MailService templateParam(Map<String,Object> templateParam) {
        this.templateParam = templateParam;
        return this;
    }
    public MailService putVar(String k, Object v) {
        this.templateParam.put(k, v);
        return this;
    }
    public MailService attach(File file, String fileName) {
        AttachObject attachObject = new AttachObject(file, fileName);
        this.attachs.add(attachObject);
        return this;
    }
    public MailService attach(File file) {
        return attach(file, null);
    }
    public MailService attach(List<File> files) {
        if(CollectionUtil.isNotEmpty(files)) {
            this.attachs.addAll(files.stream().map(f -> new AttachObject(f, null)).toList());
        }
        return this;
    }
    public MailService rsc(String rscId, String rscPath) {
        this.rscs.add(new InlineResourceObject(rscId, rscPath));
        return this;
    }

    public void send() {
        if(this.useMime) {
            this.sendMimeMail();
        } else {
            this.sendSimpleMail();
        }
    }

    /**
     * 发送普通文本邮件
     */
    private void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to.toArray(String[]::new));
        if(CollectionUtil.isNotEmpty(cc)) {
            message.setCc(cc.toArray(String[]::new));
        }
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
            log.info("简单邮件已经发送。");
        } catch (Exception e) {
            throw new ClientException(MailErrorCode.SECOND_ERROR, e);
        }
    }

    /**
     * 发送MIME邮件
     */
    private void sendMimeMail() {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to.toArray(String[]::new));
            if(CollectionUtil.isNotEmpty(cc)) {
                helper.setCc(cc.toArray(String[]::new));
            }
            helper.setSubject(subject);
            if(template!=null && !template.isEmpty()) {
                Template freemarkerTemplate = templateConfig.getConfiguration().getTemplate(template);
                String text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateParam);
                helper.setText(text, true);
            } else {
                helper.setText(html, true);
            }
            if(CollectionUtil.isNotEmpty(attachs)) {
                for(AttachObject a: attachs) {
                    helper.addAttachment(a.getFileName(), a.getFile());
                }
            }
            if(CollectionUtil.isNotEmpty(rscs)) {
                for(InlineResourceObject r: rscs) {
                    if(r.getRscPath().startsWith("http://") ||
                            r.getRscPath().startsWith("https://")) {
                        helper.addInline(r.getRscId(), new UrlResource(r.getRscPath()));
                    } else {
                        FileSystemResource res = new FileSystemResource(new File(r.getRscPath()));
                        helper.addInline(r.getRscId(), res);
                    }
                }
            }
            mailSender.send(message);
            log.info("Mime邮件发送成功");
        } catch (Exception e) {
            throw new ClientException("Mime邮件发送失败", MailErrorCode.SECOND_ERROR, e);
        }
    }
}
