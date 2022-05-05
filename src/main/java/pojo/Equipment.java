package pojo;

/**
 * @outhor LinZeHang
 * @creat 2022-05-04-20:19
 */
public class Equipment {

    private int equipment_id;
    private String equipment_name;

    public Equipment() {
    }

    public Equipment(int equipment_id, String equipment_name) {
        this.equipment_id = equipment_id;
        this.equipment_name = equipment_name;
    }

    public int getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(int equipment_id) {
        this.equipment_id = equipment_id;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }
}
