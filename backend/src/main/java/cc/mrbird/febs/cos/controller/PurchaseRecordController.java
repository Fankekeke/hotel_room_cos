package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PurchaseRecord;
import cc.mrbird.febs.cos.service.IPurchaseRecordService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/purchase-record")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaseRecordController {

    private final IPurchaseRecordService purchaseRecordService;

    /**
     * 分页获取采购物品信息
     *
     * @param page          分页对象
     * @param purchaseRecord 采购物品信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PurchaseRecord> page, PurchaseRecord purchaseRecord) {
        return R.ok(purchaseRecordService.selectRecordPage(page, purchaseRecord));
    }

    /**
     * 获取采购物品信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(purchaseRecordService.list());
    }

    /**
     * 获取采购物品详细信息
     *
     * @param id ID
     * @return 结果
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(purchaseRecordService.getById(id));
    }

    /**
     * 新增采购物品信息
     *
     * @param purchaseRecord 采购物品信息
     * @return 结果
     */
    @PostMapping
    public R save(PurchaseRecord purchaseRecord) {
        purchaseRecord.setCreateDate(DateUtil.formatDateTime(new Date()));
        purchaseRecord.setRecordCode("PRE-" + System.currentTimeMillis());
        return R.ok(purchaseRecordService.save(purchaseRecord));
    }

    /**
     * 修改采购物品信息
     *
     * @param purchaseRecord 采购物品信息
     * @return 结果
     */
    @PutMapping
    public R edit(PurchaseRecord purchaseRecord) {
        return R.ok(purchaseRecordService.updateById(purchaseRecord));
    }

    /**
     * 删除采购物品信息
     *
     * @param ids 主键IDS
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(purchaseRecordService.removeByIds(ids));
    }
}
