package com.netmi.workerbusiness.data.entity.home.linecommodity;

/**
 * 类描述：
 * 创建人：Sherlock
 * 创建时间：2019/5/6
 * 修改备注：
 */
public class LineOrderViewEntity {

    /**
     * id : 3157
     * status : 5
     * house_id : 219
     * create_time : 2019-04-29 20:20:25
     * img_url : https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=497543184,903213540&fm=173&app=25&f=JPEG?w=218&h=146&s=BF3A6D874A913ACC662F28A60300F00A
     * title : 当回事了空间的三口了
     * order_status : 2
     */

    private String id;
    private String status;
    private String house_id;
    private String create_time;
    private String img_url;
    private String title;
    private String statusStr;
    private int order_status;

    public String getStatusStr() {
        //0待入住  1待确认  2已成交 3不成功
        switch (order_status){
            case 0:
                statusStr = "待入住";
                break;
            case 1:
                statusStr = "待确认";
                break;
            case 2:
                statusStr = "已成交";
                break;
            case 3:
                statusStr = "未成功";
                break;
        }
        return statusStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHouse_id() {
        return house_id;
    }

    public void setHouse_id(String house_id) {
        this.house_id = house_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }
}
