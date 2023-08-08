package com.hung.springbootserver.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderCreatingRequest {

    @NotEmpty
    private List<BuyItem> buyItemList;

    public List<BuyItem> getBuyItemList() {
        return buyItemList;
    }

    public void setBuyItemList(List<BuyItem> buyItemList) {
        this.buyItemList = buyItemList;
    }
}
