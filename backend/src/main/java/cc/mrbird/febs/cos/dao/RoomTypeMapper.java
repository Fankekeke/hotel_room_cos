package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.RoomType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface RoomTypeMapper extends BaseMapper<RoomType> {

    /**
     * 分页获取房间类型信息
     *
     * @param page          分页对象
     * @param roomType 房间类型信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRoomTypePage(Page<RoomType> page, @Param("roomType") RoomType roomType);
}
