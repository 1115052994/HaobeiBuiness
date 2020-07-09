package com.liemi.basemall.data.entity;

/**
 * Created by Bingo on 2018/6/1.
 */

public class FileEntity {
    /**
     * "url":"http://palcomm-screen.dev/image/frontend_14819412686103.jpg",    //图片url
     "originalname":"21.jpg",    //上传文件名称
     "name":"frontend_14819412686103.jpg",       //存储文件名称
     "path":"D:/wamp/www/bigscreen/frontend/web/image//frontend_14819412686103.jpg",     //存储文件路径
     "size":20731,       //文件大小
     "type":".jpg",      //文件类型
     "state":"SUCCESS",      //状态
     "tmp_name":"D:\\wamp\\tmp\\php11FE.tmp"     //缓存路劲
     */

    private String url;
    private String originalname;
    private String name;
    private String path;
    private int size;
    private String type;
    private String state;
    private String tmp_name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTmp_name() {
        return tmp_name;
    }

    public void setTmp_name(String tmp_name) {
        this.tmp_name = tmp_name;
    }
}
