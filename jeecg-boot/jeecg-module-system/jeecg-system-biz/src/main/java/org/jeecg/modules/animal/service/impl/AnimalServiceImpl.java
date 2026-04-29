package org.jeecg.modules.animal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.animal.entity.Animal;
import org.jeecg.modules.animal.mapper.AnimalMapper;
import org.jeecg.modules.animal.service.IAnimalService;
import org.springframework.stereotype.Service;

@Service
public class AnimalServiceImpl extends ServiceImpl<AnimalMapper, Animal> implements IAnimalService {

}
