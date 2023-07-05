package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单评价
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderEvaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 评分
     */
    private BigDecimal score;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片
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
