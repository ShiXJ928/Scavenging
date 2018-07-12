package com.zq.scavenging.util;

import android.database.Cursor;

import com.zq.scavenging.application.MyApplication;
import com.zq.scavenging.bean.EquipmentBean;
import com.zq.scavenging.bean.ShelvesInfo;
import com.zq.scavenging.gen.EquipmentBeanDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AIERXUAN on 2018/6/19.
 */

public class LoveDao {
    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param equipmentBean
     */
    public static void insertLove(EquipmentBean equipmentBean) {
        MyApplication.getDaoInstant().getEquipmentBeanDao().insertOrReplace(equipmentBean);
    }

    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param list
     */
    public static void insertLoveList(List<EquipmentBean> list) {
        for (EquipmentBean equipmentBean : list) {
            MyApplication.getDaoInstant().getEquipmentBeanDao().insertOrReplace(equipmentBean);
        }
    }

    public static List<ShelvesInfo> getShelvesInfo() {
        List<ShelvesInfo> resultList = new ArrayList<>();
        String sql = "select Count(1) as number ,NAME_STR,Postion,POSTION_TYPE from " + EquipmentBeanDao.TABLENAME + " GROUP BY NAME_STR,Postion";
        Cursor c = MyApplication.getDaoInstant().getDatabase().rawQuery(sql, null);
        try {
            if (c.moveToFirst()) {
                do {
                    ShelvesInfo shelvesInfo = new ShelvesInfo();
                    shelvesInfo.setNum(c.getInt(0));
                    shelvesInfo.setType(c.getString(1));
                    shelvesInfo.setName(c.getString(2));
                    shelvesInfo.setPostionType(c.getInt(3));
                    resultList.add(shelvesInfo);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return resultList;
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteLove(long id) {
        MyApplication.getDaoInstant().getEquipmentBeanDao().deleteByKey(id);
    }

    /**
     * 删除数据
     */
    public static void deleteLoveAll() {
        MyApplication.getDaoInstant().getEquipmentBeanDao().deleteAll();
    }

    /**
     * 更新数据
     *
     * @param equipmentBean
     */
    public static void updateLove(EquipmentBean equipmentBean) {
        MyApplication.getDaoInstant().getEquipmentBeanDao().update(equipmentBean);
    }

//    /**
//     * 查询条件为Type=TYPE_LOVE的数据
//     *
//     * @return
//     */
//    public static List<EquipmentBean> queryLove() {
//        return MyApplication.getDaoInstant().getEquipmentBeanDao().queryBuilder().where(EquipmentBeanDao.Properties..eq(Shop.TYPE_LOVE)).list();
//    }

    /**
     * 查询全部数据
     */
    public static List<EquipmentBean> queryAll() {
        return MyApplication.getDaoInstant().getEquipmentBeanDao().loadAll();
    }
}
