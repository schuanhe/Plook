package com.schuanhe.Plook;

import com.schuanhe.Plook.entity.Room;
import com.schuanhe.Plook.utils.CurPool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


import java.util.List;

@SpringBootApplication
@MapperScan(basePackages = {"com.schuanhe.Plook.mapper"})
public class SpringbootApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(SpringbootApplication.class, args);


    }

}
