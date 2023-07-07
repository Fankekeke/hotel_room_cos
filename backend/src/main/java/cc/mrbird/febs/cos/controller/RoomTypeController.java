package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.RoomType;
import cc.mrbird.febs.cos.service.IRoomTypeService;
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
@RequestMapping("/cos/room-type")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomTypeController {

    private final IRoomTypeService roomTypeService;

    /**
     * 分页获取房间类型信息
     *
     * @param page          分页对象
     * @param roomType 房间类型信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<RoomType> page, RoomType roomType) {
        return R.ok(roomTypeService.selectRoomTypePage(page, roomType));
    }

    /**
     * 获取房间类型信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(roomTypeService.list(Wrappers.<RoomType>lambdaQuery().eq(RoomType::getDelFlag, 0)));
    }

    /**
     * 获取房间类型详细信息
     *
     * @param id ID
     * @return 结果
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(roomTypeService.getById(id));
    }

    /**
     * 新增房间类型信息
     *
     * @param roomType 房间类型信息
     * @return 结果
     */
    @PostMapping
    public R save(RoomType roomType) {
        roomType.setCreateDate(DateUtil.formatDateTime(new Date()));
        roomType.setCode("ROT-" + System.currentTimeMillis());
        return R.ok(roomTypeService.save(roomType));
    }

    /**
     * 修改房间类型信息
     *
     * @param roomType 房间类型信息
     * @return 结果
     */
    @PutMapping
    public R edit(RoomType roomType) {
        return R.ok(roomTypeService.updateById(roomType));
    }

    /**
     * 删除房间类型信息
     *
     * @param ids 主键IDS
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(roomTypeService.removeByIds(ids));
    }
}
