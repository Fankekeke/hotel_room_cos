package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 购买记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PurchaseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 采购编号
     */
    private String recordCode;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 删除标识
     */
    @TableLogic
    private String delFlag;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 状态（1.已支付 2.退款中 3.已退货 4.驳回）
     */
    private String status;

    /**
     * 驳回原因
     */
    private String rejectReason;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String year;

    @TableField(exist = false)
    private String month;

    @TableField(exist = false)
    private List<PurchaseGoods> goodsList;
}
