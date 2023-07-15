package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.PurchaseGoodsMapper;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.entity.CommodityInfo;
import cc.mrbird.febs.cos.entity.PurchaseGoods;
import cc.mrbird.febs.cos.entity.PurchaseRecord;
import cc.mrbird.febs.cos.dao.PurchaseRecordMapper;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.ICommodityInfoService;
import cc.mrbird.febs.cos.service.IPurchaseGoodsService;
import cc.mrbird.febs.cos.service.IPurchaseRecordService;
import cc.mrbird.febs.system.service.IMailService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class PurchaseRecordServiceImpl extends ServiceImpl<PurchaseRecordMapper, PurchaseRecord> implements IPurchaseRecordService {

    private final ICommodityInfoService commodityInfoService;

    private final IPurchaseGoodsService goodsService;

    private final PurchaseGoodsMapper goodsMapper;

    private final UserInfoMapper userInfoMapper;

    private final TemplateEngine templateEngine;

    private final IMailService mailService;

    /**
     * 分页获取采购物品信息
     *
     * @param page          分页对象
     * @param purchaseRecord 采购物品信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectRecordPage(Page<PurchaseRecord> page, PurchaseRecord purchaseRecord) {
        return baseMapper.selectRecordPage(page, purchaseRecord);
    }

    /**
     * 退货审核
     *
     * @param purchaseRecord 采购物品
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean purchaseReturn(PurchaseRecord purchaseRecord) {
        UserInfo user = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, purchaseRecord.getUserId()));
        // 同意退回
        if ("3".equals(purchaseRecord.getStatus())) {
            // 需要退回的商品
            List<CommodityInfo> updateList = new ArrayList<>();
            // 获取详情
            List<PurchaseGoods> goodsList = goodsMapper.selectList(Wrappers.<PurchaseGoods>lambdaQuery().eq(PurchaseGoods::getPurchaseCode, purchaseRecord.getRecordCode()));
            List<Integer> commodityIds = goodsList.stream().map(PurchaseGoods::getCommodityId).collect(Collectors.toList());

            // 获取商品信息
            List<CommodityInfo> commodityInfoList = (List<CommodityInfo>) commodityInfoService.listByIds(commodityIds);
            Map<Integer, CommodityInfo> commodityMap = commodityInfoList.stream().collect(Collectors.toMap(CommodityInfo::getId, e -> e));

            for (PurchaseGoods goods : goodsList) {
                CommodityInfo commodity = commodityMap.get(goods.getCommodityId());
                if (commodity == null) {
                    continue;
                }
                commodity.setStockNum(commodity.getStockNum() + goods.getOutNum());
                updateList.add(commodity);
            }
            commodityInfoService.updateBatchById(updateList);
        }
        // 发送邮件
        if (StrUtil.isNotEmpty(user.getMail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", user.getName() + "，您好。您的退款已被管理员" + ("3".equals(purchaseRecord.getStatus()) ? "通过" : "驳回" + "，如有疑问请联系管理员"));
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(user.getMail(), purchaseRecord.getRecordCode() + "：退款通知", emailContent);
        }
        return this.updateById(purchaseRecord);
    }

    /**
     * 添加采购记录
     *
     * @param purchaseRecord 采购物品
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean purchaseSave(PurchaseRecord purchaseRecord) {
        // 设置用户信息
        UserInfo user = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, purchaseRecord.getUserId()));
        purchaseRecord.setUserId(user.getId());
        // 采购编号
        purchaseRecord.setRecordCode("PRE-" + System.currentTimeMillis());
        purchaseRecord.setCreateDate(DateUtil.formatDateTime(new Date()));
        purchaseRecord.setStatus("1");

        List<PurchaseGoods> goodsList = JSONUtil.toList(purchaseRecord.getGoodsList(), PurchaseGoods.class);
        List<Integer> commodityIdList = goodsList.stream().map(PurchaseGoods::getCommodityId).collect(Collectors.toList());

        // 获取商品信息
        List<CommodityInfo> commodityList = (List<CommodityInfo>) commodityInfoService.listByIds(commodityIdList);
        Map<Integer, CommodityInfo> commodityMap = commodityList.stream().collect(Collectors.toMap(CommodityInfo::getId, e -> e));

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (PurchaseGoods goods : goodsList) {
            goods.setPurchaseCode(purchaseRecord.getRecordCode());
            CommodityInfo commodity = commodityMap.get(goods.getCommodityId());
            // 减少库存
            commodity.setStockNum(commodity.getStockNum() - goods.getOutNum());
            // 计算价格
            BigDecimal itemPrice = BigDecimal.valueOf(goods.getOutNum()).multiply(commodity.getPrice());
            goods.setItemPrice(itemPrice);
            totalPrice = totalPrice.add(itemPrice);
        }

        purchaseRecord.setTotalPrice(totalPrice);
        // 更新库存
        commodityInfoService.updateBatchById(commodityList);
        // 添加详情
        goodsService.saveBatch(goodsList);
        // 发送邮件
        if (StrUtil.isNotEmpty(user.getMail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", user.getName() + "，您好。您购买的物品已下单【"+ purchaseRecord.getRecordCode() +"】共花费"+ purchaseRecord.getTotalPrice() +"元，如有疑问请联系管理员");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(user.getMail(), purchaseRecord.getRecordCode() + "：采购通知", emailContent);
        }
        return this.save(purchaseRecord);
    }
}
