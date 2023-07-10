package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.PurchaseRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IPurchaseRecordService extends IService<PurchaseRecord> {

    /**
     * 分页获取采购物品信息
     *
     * @param page          分页对象
     * @param purchaseRecord 采购物品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRecordPage(Page<PurchaseRecord> page, PurchaseRecord purchaseRecord);

    /**
     * 退货审核
     *
     * @param purchaseRecord 采购物品
     * @return 结果
     */
    boolean purchaseReturn(PurchaseRecord purchaseRecord);
}
