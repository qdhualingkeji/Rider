package com.hualing.rider.model;

import com.baidu.mapapi.search.route.PlanNode;

public class DaiQiangDanNode {
    private PlanNode qcStNode;
    private PlanNode qcEnNode;
    private PlanNode scStNode;
    private float toQcdjl;
    private float toScdjl;
    private String orderNumber;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public float getToQcdjl() {
        return toQcdjl;
    }

    public void setToQcdjl(float toQcdjl) {
        this.toQcdjl = toQcdjl;
    }

    public float getToScdjl() {
        return toScdjl;
    }

    public void setToScdjl(float toScdjl) {
        this.toScdjl = toScdjl;
    }

    public PlanNode getQcStNode() {
        return qcStNode;
    }

    public void setQcStNode(PlanNode qcStNode) {
        this.qcStNode = qcStNode;
    }

    public PlanNode getQcEnNode() {
        return qcEnNode;
    }

    public void setQcEnNode(PlanNode qcEnNode) {
        this.qcEnNode = qcEnNode;
    }

    public PlanNode getScStNode() {
        return scStNode;
    }

    public void setScStNode(PlanNode scStNode) {
        this.scStNode = scStNode;
    }

    public PlanNode getScEnNode() {
        return scEnNode;
    }

    public void setScEnNode(PlanNode scEnNode) {
        this.scEnNode = scEnNode;
    }

    private PlanNode scEnNode;
}
