package pojo;

/**
 * @outhor LinZeHang
 * @creat 2022-05-04-20:19
 */
public class Experiment {

    private int experiment_id;
    private int equipment_id;
    private String video_name;
    private String video_site;
    private String start_time;
    private String end_time;
    private String place;
    private String sample_id;
    private String sample_area;
    private String R1;
    private String R2;
    private String RM;
    private int picture_id_1;
    private String picture_id_2;

    public Experiment() {
    }

    public Experiment(int equipment_id, String video_name, String video_site, String start_time, String place, String sample_id) {
        this.equipment_id = equipment_id;
        this.video_name = video_name;
        this.video_site = video_site;
        this.start_time = start_time;
        this.place = place;
        this.sample_id = sample_id;
    }

    public Experiment(int experiment_id, String end_time, String sample_area, String r1, String r2, String RM, int picture_id_1, String picture_id_2) {
        this.experiment_id = experiment_id;
        this.end_time = end_time;
        this.sample_area = sample_area;
        R1 = r1;
        R2 = r2;
        this.RM = RM;
        this.picture_id_1 = picture_id_1;
        this.picture_id_2 = picture_id_2;
    }

    public int getExperiment_id() {
        return experiment_id;
    }

    public void setExperiment_id(int experiment_id) {
        this.experiment_id = experiment_id;
    }

    public int getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(int equipment_id) {
        this.equipment_id = equipment_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_site() {
        return video_site;
    }

    public void setVideo_site(String video_site) {
        this.video_site = video_site;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSample_id() {
        return sample_id;
    }

    public void setSample_id(String sample_id) {
        this.sample_id = sample_id;
    }

    public String getSample_area() {
        return sample_area;
    }

    public void setSample_area(String sample_area) {
        this.sample_area = sample_area;
    }

    public String getR1() {
        return R1;
    }

    public void setR1(String r1) {
        R1 = r1;
    }

    public String getR2() {
        return R2;
    }

    public void setR2(String r2) {
        R2 = r2;
    }

    public String getRM() {
        return RM;
    }

    public void setRM(String RM) {
        this.RM = RM;
    }

    public int getPicture_id_1() {
        return picture_id_1;
    }

    public void setPicture_id_1(int picture_id_1) {
        this.picture_id_1 = picture_id_1;
    }

    public String getPicture_id_2() {
        return picture_id_2;
    }

    public void setPicture_id_2(String picture_id_2) {
        this.picture_id_2 = picture_id_2;
    }
}
