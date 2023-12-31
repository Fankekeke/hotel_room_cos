package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.FoodInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface FoodInfoMapper extends BaseMapper<FoodInfo> {

    /**
     * 分页获取公告信息
     *
     * @param page         分页对象
     * @param foodInfo 公告信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectFoodPage(Page<FoodInfo> page, @Param("foodInfo") FoodInfo foodInfo);
}
