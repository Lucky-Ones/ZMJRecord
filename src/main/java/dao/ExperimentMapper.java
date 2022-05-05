package dao;

import pojo.Experiment;
import pojo.Picture;

/**
 * @outhor LinZeHang
 * @creat 2022-05-05-9:58
 */
public interface ExperimentMapper {

    //实验一开始增加一条实验数据，返回实验id
    int addExperiment(Experiment experiment);

    //实验结束后，update实验数据
    int updateExperiment(Experiment experiment);

}
