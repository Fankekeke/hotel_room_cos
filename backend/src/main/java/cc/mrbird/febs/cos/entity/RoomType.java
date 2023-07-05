package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 客房类型
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 编号
     */
    private String code;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 图册
     */
    private String images;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 删除标识
     */
    private String delFlag;


}
