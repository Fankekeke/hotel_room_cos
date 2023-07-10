package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.PurchaseGoodsMapper;
import cc.mrbird.febs.cos.entity.CommodityInfo;
import cc.mrbird.febs.cos.entity.PurchaseGoods;
import cc.mrbird.febs.cos.entity.PurchaseRecord;
import cc.mrbird.febs.cos.dao.PurchaseRecordMapper;
import cc.mrbird.febs.cos.service.ICommodityInfoService;
import cc.mrbird.febs.cos.service.IPurchaseRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaseRecordServiceImpl extends ServiceImpl<PurchaseRecordMapper, PurchaseRecord> implements IPurchaseRecordService {

    private final ICommodityInfoService commodityInfoService;

    private final PurchaseGoodsMapper goodsMapper;

    /**
     * 分页获取采购物品信息
     *
     * @param page          分页对象
     * @param purchaseRecord 采购物品信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectRecordPage(Page<PurchaseRecord> page, PurchaseRecord purchaseRecord) {
        return baseMapper.selectRecordPage(page, purchaseRecord);
    }

    /**
     * 退货审核
     *
     * @param purchaseRecord 采购物品
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean purchaseReturn(PurchaseRecord purchaseRecord) {
        // 同意退回
        if ("3".equals(purchaseRecord.getStatus())) {
            // 需要退回的商品
            List<CommodityInfo> updateList = new ArrayList<>();
            // 获取详情
            List<PurchaseGoods> goodsList = goodsMapper.selectList(Wrappers.<PurchaseGoods>lambdaQuery().eq(PurchaseGoods::getPurchaseCode, purchaseRecord.getRecordCode()));
            List<Integer> commodityIds = goodsList.stream().map(PurchaseGoods::getCommodityId).collect(Collectors.toList());

            // 获取商品信息
            List<CommodityInfo> commodityInfoList = (List<CommodityInfo>) commodityInfoService.listByIds(commodityIds);
            Map<Integer, CommodityInfo> commodityMap = commodityInfoList.stream().collect(Collectors.toMap(CommodityInfo::getId, e -> e));

            for (PurchaseGoods goods : goodsList) {
                CommodityInfo commodity = commodityMap.get(goods.getCommodityId());
                if (commodity == null) {
                    continue;
                }
                commodity.setStockNum(commodity.getStockNum() + goods.getOutNum());
                updateList.add(commodity);
            }
            commodityInfoService.updateBatchById(updateList);
        }
        return this.updateById(purchaseRecord);
    }
}
