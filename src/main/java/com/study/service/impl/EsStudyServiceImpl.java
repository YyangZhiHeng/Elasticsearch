package com.study.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.mapper.EsStudyMapper;
import com.study.pojo.User;
import com.study.service.EsStudyService;
import org.springframework.stereotype.Service;

@Service
public class EsStudyServiceImpl extends ServiceImpl<EsStudyMapper, User> implements EsStudyService {
}
