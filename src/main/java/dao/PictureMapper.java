package dao;

import pojo.Picture;

/**
 * @outhor LinZeHang
 * @creat 2022-05-04-20:45
 */
public interface PictureMapper {

    //插入一张图片,返回图片id
    int addPicture(Picture picture);

    //根据图片id查询样品面积
    String getSampleAreaById(int picture_id);

    //根据图片id查询图片地址
    String getSiteById(int picture_id);

}
