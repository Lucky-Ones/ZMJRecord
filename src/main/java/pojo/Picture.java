package pojo;

/**
 * @outhor LinZeHang
 * @creat 2022-05-04-20:19
 */
public class Picture {

    private int picture_id;
    private String picture_name;
    private String picture_time;
    private String picture_site;
    private String sample_area;
    private int experiment_id;

    public Picture() {
    }

    public Picture(String picture_name, String picture_time, String picture_site, String sample_area, int experiment_id) {
        this.picture_name = picture_name;
        this.picture_time = picture_time;
        this.picture_site = picture_site;
        this.sample_area = sample_area;
        this.experiment_id = experiment_id;
    }

    public Picture(int picture_id, String picture_name, String picture_time, String picture_site, String sample_area, int experiment_id) {
        this.picture_id = picture_id;
        this.picture_name = picture_name;
        this.picture_time = picture_time;
        this.picture_site = picture_site;
        this.sample_area = sample_area;
        this.experiment_id = experiment_id;
    }

    public int getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(int picture_id) {
        this.picture_id = picture_id;
    }

    public String getPicture_name() {
        return picture_name;
    }

    public void setPicture_name(String picture_name) {
        this.picture_name = picture_name;
    }

    public String getPicture_time() {
        return picture_time;
    }

    public void setPicture_time(String picture_time) {
        this.picture_time = picture_time;
    }

    public String getPicture_site() {
        return picture_site;
    }

    public void setPicture_site(String picture_site) {
        this.picture_site = picture_site;
    }

    public String getSample_area() {
        return sample_area;
    }

    public void setSample_area(String sample_area) {
        this.sample_area = sample_area;
    }

    public int getExperiment_id() {
        return experiment_id;
    }

    public void setExperiment_id(int experiment_id) {
        this.experiment_id = experiment_id;
    }
}
