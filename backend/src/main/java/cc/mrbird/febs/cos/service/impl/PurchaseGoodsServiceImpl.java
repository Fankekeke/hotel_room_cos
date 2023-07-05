package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PurchaseGoods;
import cc.mrbird.febs.cos.dao.PurchaseGoodsMapper;
import cc.mrbird.febs.cos.service.IPurchaseGoodsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class PurchaseGoodsServiceImpl extends ServiceImpl<PurchaseGoodsMapper, PurchaseGoods> implements IPurchaseGoodsService {

    /**
     * 分页获取采购物品信息
     *
     * @param page          分页对象
     * @param purchaseGoods 采购物品信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPurchasePage(Page<PurchaseGoods> page, PurchaseGoods purchaseGoods) {
        return baseMapper.selectPurchasePage(page, purchaseGoods);
    }
}
