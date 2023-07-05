package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PurchaseRecord;
import cc.mrbird.febs.cos.dao.PurchaseRecordMapper;
import cc.mrbird.febs.cos.service.IPurchaseRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class PurchaseRecordServiceImpl extends ServiceImpl<PurchaseRecordMapper, PurchaseRecord> implements IPurchaseRecordService {

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
}
