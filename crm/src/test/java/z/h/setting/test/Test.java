package z.h.setting.test;

import z.h.crm.utils.DateTimeUtil;
import z.h.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author BigClever
 * @date 2021/8/22
 *
 * 验证登录状态
 **/
public class Test {
    public static void main(String[] args) {
        /**
         * 验证密码

        // 定义密码
        String wpd = "123";
        String md5 = "202cb962ac59075b964b07152d234b70";
        // 由MD5算法加密
        wpd = MD5Util.getMD5(wpd);
        // 与正确密码比对
        if(md5.equals(wpd)){
            System.out.println("密码正确...");
        }else{
            System.out.println("密码错误...");
        }*/

        /**
         * 验证失效时间

        // 定义失效时间
        String expireTime = "2019-11-27 21:50:05";
        // 获取系统当前时间
        String sysTime = DateTimeUtil.getSysTime();
        System.out.println(sysTime);
        // 比较两个时间
        int i = expireTime.compareTo(sysTime);
        System.out.println(i);
        if(i>0){
            System.out.println("账号未失效");
        }else{
            System.out.println("账号已失效");
        }*/

        /**
         * 验证锁定状态

        String lockState = "1";
        if("0".equals(lockState)){
            System.out.println("当前为锁定状态");
        }else{
            System.out.println("当前为启用状态");
        }*/

        /**
         *  验证ip是否受限
         */
        String ips = "192.168.1.1,192.168.1.2,127.0.0.1";
        String ip = "127.0.0.2";
        if(ips.contains(ip)){
            System.out.println("用户登录未受限");
        }else{
            System.out.println("用户登录已受限");
        }
    }
}
