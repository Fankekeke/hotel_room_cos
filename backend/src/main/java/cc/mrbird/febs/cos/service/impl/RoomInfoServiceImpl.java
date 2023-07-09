package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.RoomTypeMapper;
import cc.mrbird.febs.cos.entity.RoomInfo;
import cc.mrbird.febs.cos.dao.RoomInfoMapper;
import cc.mrbird.febs.cos.entity.RoomType;
import cc.mrbird.febs.cos.service.IRoomInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo> implements IRoomInfoService {

    private final RoomTypeMapper roomTypeMapper;

    /**
     * 分页获取房间信息
     *
     * @param page          分页对象
     * @param roomInfo 房间信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectRoomPage(Page<RoomInfo> page, RoomInfo roomInfo) {
        return baseMapper.selectRoomPage(page, roomInfo);
    }

    /**
     * 房间详情
     *
     * @param id 房间ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectRoomDetail(Integer id) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("room", null);
                put("type", null);
            }
        };
        RoomInfo room = this.getById(id);
        result.put("room", room);

        RoomType type = roomTypeMapper.selectById(room.getType());
        result.put("type", type);
        return result;
    }
}
