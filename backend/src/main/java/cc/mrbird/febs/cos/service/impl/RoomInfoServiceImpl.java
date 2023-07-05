package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.RoomInfo;
import cc.mrbird.febs.cos.dao.RoomInfoMapper;
import cc.mrbird.febs.cos.service.IRoomInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo> implements IRoomInfoService {

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
}
