package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.FoodInfo;
import cc.mrbird.febs.cos.service.IFoodInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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
@RequestMapping("/cos/food-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodInfoController {

    private final IFoodInfoService foodInfoService;

    /**
     * 分页获取公告信息
     *
     * @param page         分页对象
     * @param foodInfo 公告信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<FoodInfo> page, FoodInfo foodInfo) {
        return R.ok(foodInfoService.selectFoodPage(page, foodInfo));
    }

    /**
     * 获取公告信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(foodInfoService.list());
    }

    /**
     * 获取公告详细信息
     *
     * @param id ID
     * @return 结果
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(foodInfoService.getById(id));
    }

    /**
     * 新增公告信息
     *
     * @param foodInfo 公告信息
     * @return 结果
     */
    @PostMapping
    public R save(FoodInfo foodInfo) {
        foodInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(foodInfoService.save(foodInfo));
    }

    /**
     * 修改公告信息
     *
     * @param foodInfo 公告信息
     * @return 结果
     */
    @PutMapping
    public R edit(FoodInfo foodInfo) {
        return R.ok(foodInfoService.updateById(foodInfo));
    }

    /**
     * 删除公告信息
     *
     * @param ids 主键IDS
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(foodInfoService.removeByIds(ids));
    }
}
