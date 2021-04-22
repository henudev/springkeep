package com.h3c.bigdata.zhgx.function.system.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: openPortal_service
 * @description: 平均分实体
 * @author: h17338
 * @create: 2018-11-05 11:06
 **/
public class AvgScoreBean implements Serializable {

    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 评论条数
     */
    private Integer scoreAmount;

    /**
     * 平均分，保留一位小数
     */
    private BigDecimal avgScore;

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getScoreAmount() {
        return scoreAmount;
    }

    public void setScoreAmount(Integer scoreAmount) {
        this.scoreAmount = scoreAmount;
    }

    public BigDecimal getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(BigDecimal avgScore) {
        this.avgScore = avgScore;
    }
}
