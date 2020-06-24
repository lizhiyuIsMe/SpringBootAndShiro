package com.example.demo.service.RedisService;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {
        //redis的布隆过滤器是一种数据结构,他占用空间小
        //它可以告诉你某个东西  一定不存在，或者可能存在
        //比如会员,点击链接每次都要查询库,看这个用户是否是会员
        //可以通过布隆过滤器来判断是否是会员,大约可以排除 97%的非会员，用来过滤节约流量

    private BloomFilter<Integer> bf;

    /***
     * PostConstruct 程序启动时候加载此方法
     */
    @PostConstruct
    public void initBloomFilter() {
        List<Integer> list=new ArrayList<Integer>();
        list.add(10);
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        //创建布隆过滤器(默认3%误差)
        bf = BloomFilter.create(Funnels.integerFunnel(),list.size());
        for (Integer num: list) {
            bf.put(num);
        }
    }

    /***
     * 判断id可能存在于布隆过滤器里面
     * @param id
     * @return
     */
    public boolean userIdExists(int id){
        if (bf == null) {
            return false;
        }
        return bf.mightContain(id);
    }
}
