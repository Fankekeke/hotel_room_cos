package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.FoodInfo;
import cc.mrbird.febs.cos.dao.FoodInfoMapper;
import cc.mrbird.febs.cos.service.IFoodInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class FoodInfoServiceImpl extends ServiceImpl<FoodInfoMapper, FoodInfo> implements IFoodInfoService {

    /**
     * 分页获取公告信息
     *
     * @param page         分页对象
     * @param foodInfo 公告信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectFoodPage(Page<FoodInfo> page, FoodInfo foodInfo) {
        return baseMapper.selectFoodPage(page, foodInfo);
    }
}
