package com.example.demo.study.spring.util;


import com.example.demo.study.spring.aop.Aop;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

//spring的bean工厂  存储着所有的bean信息
public class BeanFactory {
     //用来存储所有的 bean定义
     private List<BeanDefined> beanDefinedList=new ArrayList<BeanDefined>();

     //用来存储所有的bean实例
     private Map<String,Object> instanceMap=new ConcurrentHashMap<String,Object>();

     //用来存储 所有的后置处理器
     private List<BeanPostProcess> beanPostProcesses=new CopyOnWriteArrayList<BeanPostProcess>();

     public BeanFactory(List<BeanDefined> beanDefinedList) throws Exception {
          this.beanDefinedList = beanDefinedList;
          //先初始后后置处理器
          for(BeanDefined bean:beanDefinedList){
             if("singleton".equals(bean.getScope())){
                  Class<?> aClass = Class.forName(bean.getBeanClassPath());
                  //如果是一个后置处理器
                  if(isBeanPostProcess(aClass)){
                       Object obj = aClass.newInstance();
                       beanPostProcesses.add((BeanPostProcess) obj);
                       instanceMap.put(bean.getBeadId(), obj);
                  }

             }
          }
          //初始化普通bean
          for(BeanDefined bean:beanDefinedList){
               if("singleton".equals(bean.getScope())){
                    Object obj = Class.forName(bean.getBeanClassPath()).newInstance();
                    //查看这个对象是否要被aop代理,如果要被代理则直接创建这个类的代理对象缓存
                    for(BeanPostProcess beanPostProcess:beanPostProcesses){
                         if(beanPostProcess instanceof Aop){
                              obj=beanPostProcess.postProcessBeforeInitialization(obj,bean.getBeadId());
                         }
                    }
                    instanceMap.put(bean.getBeadId(), obj);
               }
          }
     }

     //判断是否是后置处理器
     private boolean isBeanPostProcess(Class<?> aClass) {
          Class<?>[] interfaces = aClass.getInterfaces();
          for(Class cc : interfaces){
               if(cc == BeanPostProcess.class){
                   return true;
               }
          }
          return false;
     }

//     public void addBeanDefined(BeanDefined beanDefined){
//          beanDefinedList.add(beanDefined);
//     }


     public Object getBeanDefined(String beanId) throws IllegalAccessException, NoSuchFieldException, InvocationTargetException {
          //返回的对象
          Object obj = null;
          String beanId2="";
          Map<String, String> property =new ConcurrentHashMap<String, String>();
          //遍历所有的对象
          for(BeanDefined beanDefined:beanDefinedList){
               //获得beanid
               beanId2=beanDefined.getBeadId();
               //获得要注入的属性
               property=beanDefined.getProperty();
               if(beanId.equals(beanDefined.getBeadId())){
                   //判断是否是factoryBean
                   boolean isFactoryBean = beanDefined.getIsFactoryBean();
                   try {
                        //如果是多例的
                        if("prototype".equals(beanDefined.getScope())){
                             Class aClass = Class.forName(beanDefined.getBeanClassPath());
                             //如果是一个factoryBean
                             if(isFactoryBean){
                                   Object factoryBean = aClass.newInstance();
                                  if(factoryBean instanceof org.springframework.beans.factory.FactoryBean){
                                       org.springframework.beans.factory.FactoryBean factoryBean1=(org.springframework.beans.factory.FactoryBean)factoryBean;
                                       obj= factoryBean1.getObject();
                                       break;
                                  }
                             }else{
                                 obj = aClass.newInstance();
                                  break;
                             }
                        }else if("singleton".equals(beanDefined.getScope())){
                             obj= instanceMap.get(beanDefined.getBeadId());
                             break;
                        }
                   } catch (Exception e) {
                        System.out.println(beanDefined.getBeanClassPath() +" 不存在");
                        e.printStackTrace();
                   }
              }
          }
          //调用所有后置处理器的前置方法
          for(BeanPostProcess beanPostProcess:beanPostProcesses){
               if(beanPostProcess instanceof Common){
                     obj=beanPostProcess.postProcessBeforeInitialization(obj,beanId2);
               }
          }
          //在这里调用初始化方法
          //在这里进行属性注入
          initValue(obj,property);
          //调用所有后置处理器的后置方法
          for(BeanPostProcess beanPostProcess:beanPostProcesses){
               if(beanPostProcess instanceof Common) {
                    obj = beanPostProcess.postProcessAfterInitialization(obj, beanId2);
               }
          }
          return obj;
     }

     //属性赋值
     private void initValue(Object obj, Map<String, String> property) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
          Set<String> message = property.keySet();
          Iterator<String> iterator = message.iterator();
          while(iterator.hasNext()){
               //成员属性
               String key = iterator.next();
               //要赋值的内容
               String value = property.get(key);

               //获得要赋值的字段
               Field field = obj.getClass().getDeclaredField(key);
               //获得所有的方法
               Method[] methods = obj.getClass().getMethods();

               for(Method method:methods){
                    //这里要通过set方法赋值,因为这样可以有一定的计算,比如说如果写在了set方法中
                    if(method.getName().equalsIgnoreCase("set"+key)){
                         Class<?> type = field.getType();
                         if(type==String.class){
                              method.invoke(obj,value);
                         }else if(type==Integer.class){
                              method.invoke(obj,Integer.parseInt(value));
                         }else if(type==boolean.class){
                              method.invoke(obj,Boolean.parseBoolean(value));
                         }else{
                              //是数组类型
                              String[] arr = value.split(",");
                              method.invoke(obj,arr);
                         }
                         break;
                    }
               }
          }
     }
}
