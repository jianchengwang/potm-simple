package org.example.potm.svc.quartz.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.example.potm.svc.quartz.constant.QuartzJobStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.potm.framework.pojo.PO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartzJob implements PO {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String beanName;

    private String params;

    private String cronExpres;

    private QuartzJobStatusEnum quartzJobStatus;

    private String remark;

    private LocalDateTime createTime;
}
