package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 职位管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PositionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 岗位编号
     */
    private String code;

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 岗位职责
     */
    private String content;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 删除标识
     */
    private String delFlag;


}
