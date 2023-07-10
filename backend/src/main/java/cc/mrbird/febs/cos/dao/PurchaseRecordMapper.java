package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PurchaseRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface PurchaseRecordMapper extends BaseMapper<PurchaseRecord> {

    /**
     * 分页获取采购物品信息
     *
     * @param page           分页对象
     * @param purchaseRecord 采购物品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRecordPage(Page<PurchaseRecord> page, @Param("purchaseRecord") PurchaseRecord purchaseRecord);

    /**
     * 根据日期获取采购订单
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    List<PurchaseRecord> selectOrderByDate(@Param("year") String year, @Param("month") String month);

    /**
     * 根据记录编号获取采购物品
     *
     * @param recordCode 记录编号
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectGoodsByRecordCode(@Param("recordCode") String recordCode);
}
