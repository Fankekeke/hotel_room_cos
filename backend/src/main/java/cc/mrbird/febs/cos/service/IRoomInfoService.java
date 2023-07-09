package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.RoomInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IRoomInfoService extends IService<RoomInfo> {

    /**
     * 分页获取房间信息
     *
     * @param page     分页对象
     * @param roomInfo 房间信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRoomPage(Page<RoomInfo> page, RoomInfo roomInfo);

    /**
     * 房间详情
     *
     * @param id 房间ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectRoomDetail(Integer id);
}
