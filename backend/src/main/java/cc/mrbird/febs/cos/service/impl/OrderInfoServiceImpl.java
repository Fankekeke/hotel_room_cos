package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.RoomInfoMapper;
import cc.mrbird.febs.cos.dao.RoomTypeMapper;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.entity.RoomInfo;
import cc.mrbird.febs.cos.entity.RoomType;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private final UserInfoMapper userInfoMapper;

    private final RoomInfoMapper roomInfoMapper;

    private final RoomTypeMapper roomTypeMapper;

    /**
     * 分页获取订单信息
     *
     * @param page          分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo) {
        return baseMapper.selectOrderPage(page, orderInfo);
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.between(DateUtil.parseDate("2023-07-05"), DateUtil.parseDate("2023-07-05"), DateUnit.DAY));
    }

    /**
     * 添加订单
     *
     * @param orderInfo 订单信息
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public boolean orderSave(OrderInfo orderInfo) throws Exception {
        if (orderInfo.getUserId() != null) {
            throw new FebsException("所属用户不能为空！");
        }
        // 设置订单编号
        orderInfo.setCode("OR-" + System.currentTimeMillis());
        // 校验开始时间和结束时间【开始结束时间不能小于当前日期 结束日期不等大于开始日期】
        boolean orderDateCheck = (DateUtil.compare(new DateTime(), DateUtil.parseDate(orderInfo.getStartDate())) == -1 && DateUtil.compare(new DateTime(), DateUtil.parseDate(orderInfo.getEndDate())) == -1);
        if (!orderDateCheck) {
            throw new FebsException("开始结束时间不能小于当前日期");
        }
        if (DateUtil.compare(DateUtil.parseDate(orderInfo.getEndDate()), DateUtil.parseDate(orderInfo.getStartDate())) == -1) {
            throw new FebsException("结束时间不能大于开始");
        }

        orderInfo.setOrderStatus("1");
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));

        // 计算入住天数
        long completionCycle = DateUtil.between(DateUtil.parseDate(orderInfo.getStartDate()), DateUtil.parseDate(orderInfo.getEndDate()), DateUnit.DAY);
        if (completionCycle == 0) {
            completionCycle = 1;
        }
        orderInfo.setDays(Math.toIntExact(completionCycle));
        orderInfo.setTotalPrice(orderInfo.getRentDay().multiply(new BigDecimal(orderInfo.getDays())));
        return this.save(orderInfo);
    }

    /**
     * 获取订单详情
     *
     * @param id 订单ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectDetailById(Integer id) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("user", null);
                put("order", null);
                put("room", null);
                put("type", null);
            }
        };
        // 订单信息
        OrderInfo order = this.getById(id);
        result.put("order", order);
        // 所属用户
        UserInfo user = userInfoMapper.selectById(order.getUserId());
        result.put("user", user);
        // 房间信息
        RoomInfo room = roomInfoMapper.selectOne(Wrappers.<RoomInfo>lambdaQuery().eq(RoomInfo::getCode, order.getRoomCode()));
        result.put("room", room);
        // 房间类型
        RoomType roomType = roomTypeMapper.selectById(room.getType());
        result.put("type", roomType);
        return result;
    }

    /**
     * 查询可预约房间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectReserveRoom(String startDate, String endDate) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>(16);

        // 所有房间信息
        List<RoomInfo> roomList = roomInfoMapper.selectList(Wrappers.<RoomInfo>lambdaQuery().eq(RoomInfo::getDelFlag, 0));
        List<RoomInfo> checkRoomList = new ArrayList<>();
        // 所有房间类型
        List<RoomType> roomTypeList = roomTypeMapper.selectList(Wrappers.<RoomType>lambdaQuery().eq(RoomType::getDelFlag, 0));


        return null;
    }
}
