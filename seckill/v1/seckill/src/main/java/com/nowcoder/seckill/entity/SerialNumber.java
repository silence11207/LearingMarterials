package com.nowcoder.seckill.entity;

public class SerialNumber {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column serial_number.name
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column serial_number.value
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    private Integer value;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column serial_number.step
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    private Integer step;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column serial_number.name
     *
     * @return the value of serial_number.name
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column serial_number.name
     *
     * @param name the value for serial_number.name
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column serial_number.value
     *
     * @return the value of serial_number.value
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    public Integer getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column serial_number.value
     *
     * @param value the value for serial_number.value
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column serial_number.step
     *
     * @return the value of serial_number.step
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    public Integer getStep() {
        return step;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column serial_number.step
     *
     * @param step the value for serial_number.step
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    public void setStep(Integer step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "SerialNumber{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", step=" + step +
                '}';
    }
}