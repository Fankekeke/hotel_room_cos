package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PositionInfo;
import cc.mrbird.febs.cos.dao.PositionInfoMapper;
import cc.mrbird.febs.cos.service.IPositionInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class PositionInfoServiceImpl extends ServiceImpl<PositionInfoMapper, PositionInfo> implements IPositionInfoService {

    /**
     * 分页获取职位信息
     *
     * @param page          分页对象
     * @param positionInfo 职位信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPositionPage(Page<PositionInfo> page, PositionInfo positionInfo) {
        return baseMapper.selectPositionPage(page, positionInfo);
    }
}
