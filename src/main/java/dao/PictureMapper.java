package dao;

import pojo.Picture;

/**
 * @outhor LinZeHang
 * @creat 2022-05-04-20:45
 */
public interface PictureMapper {

    //����һ��ͼƬ,����ͼƬid
    int addPicture(Picture picture);

    //����ͼƬid��ѯ��Ʒ���
    String getSampleAreaById(int picture_id);

    //����ͼƬid��ѯͼƬ��ַ
    String getSiteById(int picture_id);

}
