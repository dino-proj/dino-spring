package org.dinospring.core.sys.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.dinospring.data.domain.TenantableEntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 操作日志
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "sys_auditlog")
public class AuditLogEntity extends TenantableEntityBase<Long> {

    /**
     * 业务对象
     */
    @Size(max = 100, message = "用户类型长度应小于100")
    @Column
    private String businessObj;

    /**
     * 操作
     */
    @Size(max = 100, message = "用户类型长度应小于100")
    private String operation;

    /**
     * 用户类型
     */
    @Size(max = 100, message = "用户类型长度应小于100")
    private String userType;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户显示名
     */
    @Size(max = 100, message = "用户类型长度应小于100")
    private String userRealname;

    /**
     * 请求uri
     */
    @Size(max = 500, message = "用户类型长度应小于500")
    @Column(length = 500)
    private String requestUri;

    /**
     * 请求method
     */
    @Size(max = 20, message = "用户类型长度应小于20")
    private String requestMethod;

    /**
     * 请求参数
     */
    @Size(max = 1000, message = "用户类型长度应小于1000")
    private String requestParams;

    /**
     * 请求IP
     */
    @Size(max = 50, message = "用户类型长度应小于50")
    private String requestIp;

    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 异常信息
     */
    @Size(max = 1000, message = "用户类型长度应小于1000")
    private String errorMsg;

}
