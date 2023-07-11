package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.LeaveComments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface LeaveCommentsMapper extends BaseMapper<LeaveComments> {

    /**
     * 分页获取留言信息
     *
     * @param page          分页对象
     * @param leaveComments 留言信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectCommentPage(Page<LeaveComments> page, @Param("leaveComments") LeaveComments leaveComments);
}
