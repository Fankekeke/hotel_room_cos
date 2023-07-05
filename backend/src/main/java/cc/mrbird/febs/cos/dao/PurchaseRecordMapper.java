package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PurchaseRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface PurchaseRecordMapper extends BaseMapper<PurchaseRecord> {

    /**
     * 分页获取采购物品信息
     *
     * @param page          分页对象
     * @param purchaseRecord 采购物品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRecordPage(Page<PurchaseRecord> page, @Param("purchaseRecord") PurchaseRecord purchaseRecord);
}
