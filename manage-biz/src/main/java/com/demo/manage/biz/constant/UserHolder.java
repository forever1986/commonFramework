package com.demo.manage.biz.constant;

import com.demo.manage.biz.entity.TUser;

public class UserHolder {

    private static final ThreadLocal<TUser> USER_INFO = new ThreadLocal<>();

    public static void setUserId(TUser tUser) {
        USER_INFO.set(tUser);
    }

    public static TUser getTUser() {
        return USER_INFO.get();
    }

    public static void removeTUser() {
        USER_INFO.remove();
    }
}
