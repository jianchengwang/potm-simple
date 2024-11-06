package org.example.potm.plugins.push.mail.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InlineResourceObject {
    private String rscId;
    private String rscPath;
}
