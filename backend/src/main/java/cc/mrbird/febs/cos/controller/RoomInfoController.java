package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.RoomInfo;
import cc.mrbird.febs.cos.service.IRoomInfoService;
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
@RequestMapping("/cos/room-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomInfoController {

    private final IRoomInfoService roomInfoService;

    /**
     * 分页获取房间信息
     *
     * @param page          分页对象
     * @param roomInfo 房间信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<RoomInfo> page, RoomInfo roomInfo) {
        return R.ok();
    }

    /**
     * 获取房间信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(roomInfoService.list());
    }

    /**
     * 获取房间详细信息
     *
     * @param id ID
     * @return 结果
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(roomInfoService.getById(id));
    }

    /**
     * 新增房间信息
     *
     * @param roomInfo 房间信息
     * @return 结果
     */
    @PostMapping
    public R save(RoomInfo roomInfo) {
        roomInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(roomInfoService.save(roomInfo));
    }

    /**
     * 修改房间信息
     *
     * @param roomInfo 房间信息
     * @return 结果
     */
    @PutMapping
    public R edit(RoomInfo roomInfo) {
        return R.ok(roomInfoService.updateById(roomInfo));
    }

    /**
     * 删除房间信息
     *
     * @param ids 主键IDS
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(roomInfoService.removeByIds(ids));
    }
}