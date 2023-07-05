package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.RoomInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {

    /**
     * 分页获取房间信息
     *
     * @param page          分页对象
     * @param roomInfo 房间信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRoomPage(Page<RoomInfo> page, @Param("roomInfo") RoomInfo roomInfo);
}
