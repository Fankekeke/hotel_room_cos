package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 房间编号
     */
    private String roomCode;

    /**
     * 租房开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 每日租金
     */
    private BigDecimal rentDay;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 入住天数
     */
    private Integer days;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 支付状态（0.未支付 1.已支付）
     */
    private String orderStatus;

    /**
     * 是否退房（0.否 1.是）
     */
    private String recedeFlag;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String userName;

}
