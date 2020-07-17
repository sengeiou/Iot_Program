package com.vtech.app.largeuidb.largeuibean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LargeUIItem {
    @Override
    public String toString() {
        return "LargeUIItem{" +
                "id=" + id +
                ", isApp=" + isApp +
                ", isSystemApp=" + isSystemApp +
                ", isMove=" + isMove +
                ", isModify=" + isModify +
                ", type=" + type +
                ", itemName='" + itemName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", screen=" + screen +
                ", cellX=" + cellX +
                ", cellY=" + cellY +
                ", Container=" + Container +
                '}';
    }

    @Id(autoincrement = true)
    private Long id;

    private boolean isApp;

    private boolean isSystemApp;

    //是否可移动
    private boolean isMove;

    //是否可替换/删除
    private boolean isModify;

    //类型，普通类型和定制类型
    private int type;

    private String itemName;

    private String packageName;

    //位置信息
    //第几屏
    private int screen = -1;

    //在屏中横向位置， 0 or 1
    private int cellX = -1;

    //在屏中纵向位置， 0 ～ 3
    private int cellY = -1;

    //若为某个项的子项，此处值为其父项的id值
    private Long Container = -100L;


    @Generated(hash = 2080121301)
    public LargeUIItem() {
    }

    @Generated(hash = 1809591862)
    public LargeUIItem(Long id, boolean isApp, boolean isSystemApp, boolean isMove,
            boolean isModify, int type, String itemName, String packageName,
            int screen, int cellX, int cellY, Long Container) {
        this.id = id;
        this.isApp = isApp;
        this.isSystemApp = isSystemApp;
        this.isMove = isMove;
        this.isModify = isModify;
        this.type = type;
        this.itemName = itemName;
        this.packageName = packageName;
        this.screen = screen;
        this.cellX = cellX;
        this.cellY = cellY;
        this.Container = Container;
    }

    public boolean getIsApp() {
        return this.isApp;
    }

    public void setIsApp(boolean isApp) {
        this.isApp = isApp;
    }

    public boolean getIsSystemApp() {
        return this.isSystemApp;
    }

    public void setIsSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

    public boolean getIsMove() {
        return this.isMove;
    }

    public void setIsMove(boolean isMove) {
        this.isMove = isMove;
    }

    public boolean getIsModify() {
        return this.isModify;
    }

    public void setIsModify(boolean isModify) {
        this.isModify = isModify;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getScreen() {
        return this.screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }

    public int getCellX() {
        return this.cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellY() {
        return this.cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    public Long getContainer() {
        return this.Container;
    }

    public void setContainer(Long Container) {
        this.Container = Container;
    }

}
