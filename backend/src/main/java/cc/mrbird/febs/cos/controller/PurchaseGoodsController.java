package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PurchaseGoods;
import cc.mrbird.febs.cos.service.IPurchaseGoodsService;
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
@RequestMapping("/cos/purchase-goods")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaseGoodsController {

    private final IPurchaseGoodsService purchaseGoodsService;

    /**
     * 分页获取采购物品信息
     *
     * @param page          分页对象
     * @param purchaseGoods 采购物品信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PurchaseGoods> page, PurchaseGoods purchaseGoods) {
        return R.ok();
    }

    /**
     * 获取采购物品信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(purchaseGoodsService.list());
    }

    /**
     * 获取采购物品详细信息
     *
     * @param id ID
     * @return 结果
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(purchaseGoodsService.getById(id));
    }

    /**
     * 新增采购物品信息
     *
     * @param purchaseGoods 采购物品信息
     * @return 结果
     */
    @PostMapping
    public R save(PurchaseGoods purchaseGoods) {
        return R.ok(purchaseGoodsService.save(purchaseGoods));
    }

    /**
     * 修改采购物品信息
     *
     * @param purchaseGoods 采购物品信息
     * @return 结果
     */
    @PutMapping
    public R edit(PurchaseGoods purchaseGoods) {
        return R.ok(purchaseGoodsService.updateById(purchaseGoods));
    }

    /**
     * 删除采购物品信息
     *
     * @param ids 主键IDS
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(purchaseGoodsService.removeByIds(ids));
    }
}
