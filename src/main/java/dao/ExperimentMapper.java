package dao;

import pojo.Experiment;
import pojo.Picture;

/**
 * @outhor LinZeHang
 * @creat 2022-05-05-9:58
 */
public interface ExperimentMapper {

    //ʵ��һ��ʼ����һ��ʵ�����ݣ�����ʵ��id
    int addExperiment(Experiment experiment);

    //ʵ�������updateʵ������
    int updateExperiment(Experiment experiment);

}
