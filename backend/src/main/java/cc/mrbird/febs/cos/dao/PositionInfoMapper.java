package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PositionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface PositionInfoMapper extends BaseMapper<PositionInfo> {

    /**
     * 分页获取职位信息
     *
     * @param page          分页对象
     * @param positionInfo 职位信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPositionPage(Page<PositionInfo> page, @Param("positionInfo") PositionInfo positionInfo);
}
