package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.RoomType;
import cc.mrbird.febs.cos.dao.RoomTypeMapper;
import cc.mrbird.febs.cos.service.IRoomTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class RoomTypeServiceImpl extends ServiceImpl<RoomTypeMapper, RoomType> implements IRoomTypeService {

    /**
     * 分页获取房间类型信息
     *
     * @param page          分页对象
     * @param roomType 房间类型信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectRoomTypePage(Page<RoomType> page, RoomType roomType) {
        return baseMapper.selectRoomTypePage(page, roomType);
    }
}
