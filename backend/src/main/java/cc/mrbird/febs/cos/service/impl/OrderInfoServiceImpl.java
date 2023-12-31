package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.*;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.IMailService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private final UserInfoMapper userInfoMapper;

    private final RoomInfoMapper roomInfoMapper;

    private final PurchaseRecordMapper purchaseRecordMapper;

    private final PurchaseGoodsMapper goodsMapper;

    private final RoomTypeMapper roomTypeMapper;

    private final OrderInfoMapper orderInfoMapper;

    private final BulletinInfoMapper bulletinInfoMapper;

    private final StaffInfoMapper staffInfoMapper;

    private final TemplateEngine templateEngine;

    private final IMailService mailService;

    private final CollectInfoMapper collectInfoMapper;

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

    /**
     * 添加订单
     *
     * @param orderInfo 订单信息
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public boolean orderSave(OrderInfo orderInfo) throws Exception {
        if (orderInfo.getUserId() == null) {
            throw new FebsException("所属用户不能为空！");
        }
        UserInfo user = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfo.getUserId()));
        orderInfo.setUserId(user.getId());

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
        orderInfo.setRecedeFlag("0");
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));

        // 计算入住天数
        long completionCycle = DateUtil.between(DateUtil.parseDate(orderInfo.getStartDate()), DateUtil.parseDate(orderInfo.getEndDate()), DateUnit.DAY);
        if (completionCycle == 0) {
            completionCycle = 1;
        }
        orderInfo.setDays(Math.toIntExact(completionCycle));
        orderInfo.setTotalPrice(orderInfo.getRentDay().multiply(new BigDecimal(orderInfo.getDays())));

        // 发送邮件
        if (StrUtil.isNotEmpty(user.getMail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", user.getName() + "，您好。您的订单已生成，请注意查看");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(user.getMail(), DateUtil.formatDate(new Date()) + "客房入住提示", emailContent);
        }
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
    public List<LinkedHashMap<String, Object>> selectReserveRoom(String startDate, String endDate, Integer typeId) {
        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();

        // 所有订单信息isOverlap
        List<OrderInfo> orderList = orderInfoMapper.selectList(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getDelFlag, "0").eq(OrderInfo::getRecedeFlag, "0"));
        Map<String, List<OrderInfo>> orderMap = orderList.stream().collect(Collectors.groupingBy(OrderInfo::getRoomCode));

        // 所有房间信息
        List<RoomInfo> roomList = roomInfoMapper.selectList(Wrappers.<RoomInfo>lambdaQuery().eq(RoomInfo::getDelFlag, 0));
        // 所有房间类型
        List<RoomType> roomTypeList = roomTypeMapper.selectList(Wrappers.<RoomType>lambdaQuery().eq(RoomType::getDelFlag, 0));
        if (CollectionUtil.isEmpty(roomTypeList) || CollectionUtil.isEmpty(roomList)) {
            return null;
        }

        // 房间按类型分组
        Map<Integer, List<RoomInfo>> roomMap = roomList.stream().collect(Collectors.groupingBy(RoomInfo::getType));

        for (RoomType roomType : roomTypeList) {
            LinkedHashMap<String, Object> tempMap = new LinkedHashMap<String, Object>() {
                {
                    put("name", roomType.getTypeName());
                    put("value", Collections.emptyList());
                }
            };

            List<RoomInfo> checkRoomList = new ArrayList<>();
            // 获取所属类型房间
            List<RoomInfo> roomTempList = roomMap.get(roomType.getId());
            if (CollectionUtil.isEmpty(roomTempList)) {
                result.add(tempMap);
                continue;
            }
            for (RoomInfo roomInfo : roomTempList) {
                List<OrderInfo> orderTempList = orderMap.get(roomInfo.getCode());
                if (CollectionUtil.isEmpty(orderTempList)) {
                    checkRoomList.add(roomInfo);
                    continue;
                }
                // 获取是否有订单存在选择时间内
                boolean overlapCheck = orderTempList.stream().anyMatch(e -> DateUtil.isOverlap(DateUtil.parseDate(e.getStartDate()), DateUtil.parseDate(e.getEndDate()), DateUtil.parseDate(startDate), DateUtil.parseDate(endDate)));
                if (overlapCheck) {
                    continue;
                }
                checkRoomList.add(roomInfo);
            }
            tempMap.put("value", checkRoomList);
            result.add(tempMap);
        }
        return result;
    }

    /**
     * 查询可预约房间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param typeId    房间类型
     * @return 结果
     */
    @Override
    public List<RoomInfo> selectReserveRoomByDate(String startDate, String endDate, Integer typeId, Integer userId) {
        // 返回数据
        List<RoomInfo> result = new ArrayList<>();

        // 用户信息
        UserInfo user = userInfoMapper.selectOne(Wrappers.<UserInfo> lambdaQuery().eq(UserInfo::getUserId, userId));
        // 收藏信息
        List<CollectInfo> collectInfoList = collectInfoMapper.selectList(Wrappers.<CollectInfo>lambdaQuery().eq(CollectInfo::getUserId, user.getId()));
        Map<String, Integer> collectMap = collectInfoList.stream().collect(Collectors.toMap(CollectInfo::getRoomCode, CollectInfo::getId));

        // 所有订单信息isOverlap
        List<OrderInfo> orderList = orderInfoMapper.selectList(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getDelFlag, "0").eq(OrderInfo::getRecedeFlag, "0"));
        Map<String, List<OrderInfo>> orderMap = orderList.stream().collect(Collectors.groupingBy(OrderInfo::getRoomCode));

        // 所有房间信息
        List<RoomInfo> roomList = roomInfoMapper.selectList(Wrappers.<RoomInfo>lambdaQuery().eq(RoomInfo::getDelFlag, 0));
        // 所有房间类型
        List<RoomType> roomTypeList = roomTypeMapper.selectList(Wrappers.<RoomType>lambdaQuery().eq(RoomType::getDelFlag, 0));
        if (CollectionUtil.isEmpty(roomTypeList) || CollectionUtil.isEmpty(roomList)) {
            return null;
        }

        // 房间类型
        List<RoomType> typeList = roomTypeMapper.selectList(Wrappers.<RoomType>lambdaQuery().eq(RoomType::getDelFlag, "0"));
        Map<Integer, String> typeMap = typeList.stream().collect(Collectors.toMap(RoomType::getId, RoomType::getTypeName));

        // 房间按类型分组
        Map<Integer, List<RoomInfo>> roomMap = roomList.stream().collect(Collectors.groupingBy(RoomInfo::getType));

        for (RoomType roomType : roomTypeList) {
            List<RoomInfo> checkRoomList = new ArrayList<>();
            // 获取所属类型房间
            List<RoomInfo> roomTempList = roomMap.get(roomType.getId());
            if (CollectionUtil.isEmpty(roomTempList)) {
                continue;
            }
            for (RoomInfo roomInfo : roomTempList) {
                roomInfo.setHasHeart(CollectionUtil.isNotEmpty(collectMap) && collectMap.get(roomInfo.getCode()) != null);
                // 房间类型
                roomInfo.setTypeName(typeMap.get(roomInfo.getType()));
                List<OrderInfo> orderTempList = orderMap.get(roomInfo.getCode());
                if (CollectionUtil.isEmpty(orderTempList)) {
                    checkRoomList.add(roomInfo);
                    continue;
                }
                // 获取是否有订单存在选择时间内
                boolean overlapCheck = orderTempList.stream().anyMatch(e -> DateUtil.isOverlap(DateUtil.parseDate(startDate), DateUtil.parseDate(endDate), DateUtil.parseDate(e.getStartDate()), DateUtil.parseDate(e.getEndDate())));
                if (overlapCheck) {
                    continue;
                }
                checkRoomList.add(roomInfo);
            }
            result.addAll(checkRoomList);
        }
        return result;
    }

    /**
     * 数据统计
     *
     * @param checkDate 选择日期
     * @param typeId    房间类型
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectRoomStatistics(String checkDate, Integer typeId) {
        if (StrUtil.isEmpty(checkDate)) {
            checkDate = DateUtil.formatDate(new Date());
        }

        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        // 获取当前月份及当前月份
        String year = StrUtil.toString(DateUtil.year(DateUtil.parseDate(checkDate)));
        String month = StrUtil.toString(DateUtil.month(DateUtil.parseDate(checkDate)) + 1);

        // 本月收益
        List<LinkedHashMap<String, Object>> priceByMonth = baseMapper.selectPriceByMonth(year, month, typeId, checkDate);
        result.put("priceByMonth", priceByMonth);

        // 本月订单
        List<LinkedHashMap<String, Object>> orderNumByMonth = baseMapper.selectOrderNumByMonth(year, month, typeId, checkDate);
        result.put("orderNumByMonth", orderNumByMonth);

        // 房间类型销量统计
        List<LinkedHashMap<String, Object>> typeOrderNumRateByMonth = baseMapper.selectRoomTypeRateByMonth(year, month);
        result.put("typeOrderNumRateByMonth", typeOrderNumRateByMonth);

        // 房间类型销售统计
        List<LinkedHashMap<String, Object>> typePriceRateByMonth = baseMapper.selectRoomTypePriceRateByMonth(year, month);
        result.put("typePriceRateByMonth", typePriceRateByMonth);

        return result;
    }

    /**
     * 获取采购详情
     *
     * @param recordId 记录ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectRecordDetail(Integer recordId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("record", null);
                put("goods", Collections.emptyList());
            }
        };
        // 采购记录
        PurchaseRecord record = purchaseRecordMapper.selectById(recordId);
        result.put("record", record);
        // 采购详情
        List<LinkedHashMap<String, Object>> goods = purchaseRecordMapper.selectGoodsByRecordCode(record.getRecordCode());
        result.put("goods", goods);
        return result;
    }

    /**
     * 根据房间编号获取订单记录
     *
     * @param roomCode 房间编号
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectOrderRecordByRoomCode(String roomCode) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("room", null);
                put("order", Collections.emptyList());
            }
        };
        // 获取房间信息
        RoomInfo room = roomInfoMapper.selectOne(Wrappers.<RoomInfo>lambdaQuery().eq(RoomInfo::getCode, roomCode));
        result.put("room", room);

        // 订单
        List<OrderInfo> orderList = this.list(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getRoomCode, roomCode));
        result.put("order", orderList);
        return result;
    }

    /**
     * 统计房间当前状态
     *
     * @return 结果
     */
    @Override
    public List<RoomInfo> selectRoomStatus(Integer userId) {
        // 当前所有房间信息
        List<RoomInfo> roomList = roomInfoMapper.selectList(Wrappers.<RoomInfo>lambdaQuery().eq(RoomInfo::getDelFlag, "0"));
        // 所有订单信息
        List<OrderInfo> orderList = orderInfoMapper.selectList(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getDelFlag, "0"));
        Map<String, List<OrderInfo>> orderMap = orderList.stream().collect(Collectors.groupingBy(OrderInfo::getRoomCode));

        // 用户信息
        UserInfo user = userInfoMapper.selectOne(Wrappers.<UserInfo> lambdaQuery().eq(UserInfo::getUserId, userId));

        Map<String, Integer> collectMap = new HashMap<>(16);
        if (user != null) {
            // 收藏信息
            List<CollectInfo> collectInfoList = collectInfoMapper.selectList(Wrappers.<CollectInfo>lambdaQuery().eq(CollectInfo::getUserId, user.getId()));
            collectMap = collectInfoList.stream().collect(Collectors.toMap(CollectInfo::getRoomCode, CollectInfo::getId));
        }
        // 房间类型
        List<RoomType> typeList = roomTypeMapper.selectList(Wrappers.<RoomType>lambdaQuery().eq(RoomType::getDelFlag, "0"));
        Map<Integer, String> typeMap = typeList.stream().collect(Collectors.toMap(RoomType::getId, RoomType::getTypeName));
        for (RoomInfo room : roomList) {
            room.setHasHeart(CollectionUtil.isNotEmpty(collectMap) && collectMap.get(room.getCode()) != null);
            // 房间类型
            room.setTypeName(typeMap.get(room.getType()));
            // 此房间的订单信息
            List<OrderInfo> orderRoomList = orderMap.get(room.getCode());
            if (CollectionUtil.isEmpty(orderRoomList)) {
                room.setCheckStatus(false);
                continue;
            }
            room.setCheckStatus(orderRoomList.stream().anyMatch(e -> DateUtil.isIn(new Date(), DateUtil.parseDate(e.getStartDate()), DateUtil.parseDate(e.getEndDate()))));
        }
        return roomList;
    }

    /**
     * 获取主页统计数据
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectHomeData() {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("staffNum", 0);
                put("totalRevenue", 0);
                put("totalOrderNum", 0);
                put("roomNum", 0);
                put("monthOrderNum", 0);
                put("monthOrderTotal", 0);
                put("yearOrderNum", 0);
                put("yearOrderTotal", 0);
            }
        };

        // 员工数量
        int staffNum = staffInfoMapper.selectCount(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getDelFlag, "0"));
        result.put("staffNum", staffNum);

        List<OrderInfo> allOrderList = orderInfoMapper.selectList(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getDelFlag, "0")
                .eq(OrderInfo::getOrderStatus, "1").eq(OrderInfo::getRecedeFlag, "0"));
        List<PurchaseRecord> allRecordList = purchaseRecordMapper.selectList(Wrappers.<PurchaseRecord>lambdaQuery().eq(PurchaseRecord::getDelFlag, "0")
                .in(PurchaseRecord::getStatus, Arrays.asList("1", "4")));
        // 总收益
        BigDecimal orderTotal = allOrderList.stream().map(OrderInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal recordTotal = allRecordList.stream().map(PurchaseRecord::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalRevenue = orderTotal.add(recordTotal);
        result.put("totalRevenue", totalRevenue);

        // 总订单量
        int orderCount = allOrderList.size();
        int recordCount = allRecordList.size();
        int totalOrderNum = orderCount + recordCount;
        result.put("totalOrderNum", totalOrderNum);

        // 房间数量
        int roomNum = roomInfoMapper.selectCount(Wrappers.<RoomInfo>lambdaQuery().eq(RoomInfo::getDelFlag, "0"));
        result.put("roomNum", roomNum);


        // 获取当前月份及当前月份
        String year = StrUtil.toString(DateUtil.year(new Date()));
        String month = StrUtil.toString(DateUtil.month(new Date()) + 1);

        // 本年订单
        List<OrderInfo> orderList = baseMapper.selectOrderByDate(year, null);
        Map<String, List<OrderInfo>> orderMap = orderList.stream().collect(Collectors.groupingBy(OrderInfo::getMonth));
        // 本年购买记录
        List<PurchaseRecord> recordList = purchaseRecordMapper.selectOrderByDate(year, null);
        Map<String, List<PurchaseRecord>> recordMap = recordList.stream().collect(Collectors.groupingBy(PurchaseRecord::getMonth));

        // 本月订单量
        int orderMonthCount = CollectionUtil.isEmpty(orderMap.get(month)) ? 0 : orderMap.get(month).size();
        int recordMonthCount = CollectionUtil.isEmpty(recordMap.get(month)) ? 0 : recordMap.get(month).size();
        result.put("monthOrderNum", orderMonthCount + recordMonthCount);

        // 本月收益
        BigDecimal orderMonthTotal = CollectionUtil.isEmpty(orderMap.get(month)) ? BigDecimal.ZERO : orderMap.get(month).stream().map(OrderInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal recordMonthTotal = CollectionUtil.isEmpty(recordMap.get(month)) ? BigDecimal.ZERO : recordMap.get(month).stream().map(PurchaseRecord::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("monthOrderTotal", orderMonthTotal.add(recordMonthTotal));

        // 本年订单量
        Integer orderYearCount = CollectionUtil.isEmpty(orderList) ? 0 : orderList.size();
        Integer recordYearCount = CollectionUtil.isEmpty(recordList) ? 0 :recordList.size();
        result.put("yearOrderNum", orderYearCount + recordYearCount);

        // 本年收益
        BigDecimal orderYearTotal = CollectionUtil.isEmpty(orderList) ? BigDecimal.ZERO : orderList.stream().map(OrderInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal recordYearTotal = CollectionUtil.isEmpty(recordList) ? BigDecimal.ZERO : recordList.stream().map(PurchaseRecord::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("yearOrderTotal", orderYearTotal.add(recordYearTotal));

        // 近十天内收入统计
        List<LinkedHashMap<String, Object>> priceDay = baseMapper.selectPriceByDay();
        result.put("priceDay", priceDay);

        // 近十天内工单统计
        List<LinkedHashMap<String, Object>> orderNumDay = baseMapper.selectOrderNumByDay();
        result.put("orderNumDay", orderNumDay);

        // 本月内房间类型销量统计
        List<LinkedHashMap<String, Object>> typeRate = baseMapper.selectRoomTypeRateByMonth(year, month);
        result.put("typeRate", typeRate);

        // 公告信息
        result.put("bulletin", bulletinInfoMapper.selectList(Wrappers.<BulletinInfo>lambdaQuery()));
        return result;
    }
}
