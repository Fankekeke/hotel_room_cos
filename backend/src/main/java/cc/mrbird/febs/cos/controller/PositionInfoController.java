package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PositionInfo;
import cc.mrbird.febs.cos.service.IPositionInfoService;
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
@RequestMapping("/cos/position-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PositionInfoController {

    private final IPositionInfoService positionInfoService;

    /**
     * 分页获取职位信息
     *
     * @param page          分页对象
     * @param positionInfo 职位信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PositionInfo> page, PositionInfo positionInfo) {
        return R.ok(positionInfoService.selectPositionPage(page, positionInfo));
    }

    /**
     * 获取职位信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(positionInfoService.list());
    }

    /**
     * 获取职位详细信息
     *
     * @param id ID
     * @return 结果
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(positionInfoService.getById(id));
    }

    /**
     * 新增职位信息
     *
     * @param positionInfo 职位信息
     * @return 结果
     */
    @PostMapping
    public R save(PositionInfo positionInfo) {
        positionInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(positionInfoService.save(positionInfo));
    }

    /**
     * 修改职位信息
     *
     * @param positionInfo 职位信息
     * @return 结果
     */
    @PutMapping
    public R edit(PositionInfo positionInfo) {
        return R.ok(positionInfoService.updateById(positionInfo));
    }

    /**
     * 删除职位信息
     *
     * @param ids 主键IDS
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(positionInfoService.removeByIds(ids));
    }
}
