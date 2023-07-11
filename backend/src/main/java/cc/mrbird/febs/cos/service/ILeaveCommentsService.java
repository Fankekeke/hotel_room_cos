package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.LeaveComments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ILeaveCommentsService extends IService<LeaveComments> {

    /**
     * 分页获取留言信息
     *
     * @param page          分页对象
     * @param leaveComments 留言信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectCommentPage(Page<LeaveComments> page, LeaveComments leaveComments);
}
