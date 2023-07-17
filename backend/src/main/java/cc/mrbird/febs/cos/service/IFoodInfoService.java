package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.FoodInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IFoodInfoService extends IService<FoodInfo> {

    /**
     * 分页获取公告信息
     *
     * @param page         分页对象
     * @param foodInfo 公告信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectFoodPage(Page<FoodInfo> page,FoodInfo foodInfo);
}
