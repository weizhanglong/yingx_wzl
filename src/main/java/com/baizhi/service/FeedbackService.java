package com.baizhi.service;

import java.util.HashMap;

public interface FeedbackService {
    HashMap<String, Object> queryByPage(Integer page, Integer rows);

    public void Exports();

    public void Import();
}
