package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DeptInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface DeptInfoMapper extends BaseMapper<DeptInfo> {

    /**
     * 分页获取部门信息
     *
     * @param page     分页对象
     * @param deptInfo 部门信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDeptPage(Page<DeptInfo> page, @Param("deptInfo") DeptInfo deptInfo);
}
