package com.inc.admin.dto.sys.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpPwdReq {

    @NotBlank(message = "原密码不能为空")
    private String oldPwd;

    @NotBlank(message = "新密码不能为空")
    private String newPwd;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPwd;
}
