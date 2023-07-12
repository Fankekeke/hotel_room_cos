package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderEvaluate;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IOrderEvaluateService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
@RequestMapping("/cos/order-evaluate")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderEvaluateController {

    private final IOrderEvaluateService orderEvaluateService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取订单评价信息
     *
     * @param page          分页对象
     * @param orderEvaluate 订单评价信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderEvaluate> page, OrderEvaluate orderEvaluate) {
        return R.ok(orderEvaluateService.selectEvaluatePage(page, orderEvaluate));
    }

    /**
     * 获取订单评价信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderEvaluateService.list());
    }

    /**
     * 获取订单评价详细信息
     *
     * @param id ID
     * @return 结果
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderEvaluateService.getById(id));
    }

    /**
     * 新增订单评价信息
     *
     * @param orderEvaluate 订单评价信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderEvaluate orderEvaluate) {
        orderEvaluate.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 设置用户ID
        UserInfo user = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderEvaluate.getUserId()));
        orderEvaluate.setUserId(user.getId());
        return R.ok(orderEvaluateService.save(orderEvaluate));
    }

    /**
     * 修改订单评价信息
     *
     * @param orderEvaluate 订单评价信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderEvaluate orderEvaluate) {
        return R.ok(orderEvaluateService.updateById(orderEvaluate));
    }

    /**
     * 删除订单评价信息
     *
     * @param ids 主键IDS
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderEvaluateService.removeByIds(ids));
    }
}
