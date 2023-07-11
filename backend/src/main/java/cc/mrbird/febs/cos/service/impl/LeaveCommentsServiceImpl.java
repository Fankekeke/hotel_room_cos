package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.LeaveComments;
import cc.mrbird.febs.cos.dao.LeaveCommentsMapper;
import cc.mrbird.febs.cos.service.ILeaveCommentsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class LeaveCommentsServiceImpl extends ServiceImpl<LeaveCommentsMapper, LeaveComments> implements ILeaveCommentsService {

    /**
     * 分页获取留言信息
     *
     * @param page          分页对象
     * @param leaveComments 留言信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectCommentPage(Page<LeaveComments> page, LeaveComments leaveComments) {
        return baseMapper.selectCommentPage(page, leaveComments);
    }
}
