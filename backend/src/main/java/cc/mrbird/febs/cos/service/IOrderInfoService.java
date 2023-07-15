package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.RoomInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo);

    /**
     * 添加订单
     *
     * @param orderInfo 订单信息
     * @return 结果
     * @throws Exception 异常
     */
    boolean orderSave(OrderInfo orderInfo) throws Exception;

    /**
     * 获取订单详情
     *
     * @param id 订单ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectDetailById(Integer id);

    /**
     * 查询可预约房间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param typeId    房间类型
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectReserveRoom(String startDate, String endDate, Integer typeId);

    /**
     * 数据统计
     *
     * @param checkDate 选择日期
     * @param typeId    房间类型
     * @return 结果
     */
    LinkedHashMap<String, Object> selectRoomStatistics(String checkDate, Integer typeId);

    /**
     * 获取采购详情
     *
     * @param recordId 记录ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectRecordDetail(Integer recordId);

    /**
     * 根据房间编号获取订单记录
     *
     * @param roomCode 房间编号
     * @return 结果
     */
    LinkedHashMap<String, Object> selectOrderRecordByRoomCode(String roomCode);

    /**
     * 统计房间当前状态
     *
     * @return 结果
     */
    List<RoomInfo> selectRoomStatus();

    /**
     * 获取主页统计数据
     *
     * @return 结果
     */
    LinkedHashMap<String, Object> selectHomeData();
}
