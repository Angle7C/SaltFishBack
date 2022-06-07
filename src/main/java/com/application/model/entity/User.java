package com.application.model.entity;

public class User {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.id
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.gmt_create
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    private Long gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.gmt_modified
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    private Long gmtModified;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.decription
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    private String decription;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.user_name
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    private String userName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.pass_word
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    private String passWord;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.wx_id
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    private String wxId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.image_url
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    private String imageUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.ranks
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    private Long ranks;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.id
     *
     * @return the value of user.id
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.id
     *
     * @param id the value for user.id
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.gmt_create
     *
     * @return the value of user.gmt_create
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.gmt_create
     *
     * @param gmtCreate the value for user.gmt_create
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.gmt_modified
     *
     * @return the value of user.gmt_modified
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public Long getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.gmt_modified
     *
     * @param gmtModified the value for user.gmt_modified
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.decription
     *
     * @return the value of user.decription
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public String getDecription() {
        return decription;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.decription
     *
     * @param decription the value for user.decription
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public void setDecription(String decription) {
        this.decription = decription == null ? null : decription.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.user_name
     *
     * @return the value of user.user_name
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.user_name
     *
     * @param userName the value for user.user_name
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.pass_word
     *
     * @return the value of user.pass_word
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.pass_word
     *
     * @param passWord the value for user.pass_word
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord == null ? null : passWord.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.wx_id
     *
     * @return the value of user.wx_id
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public String getWxId() {
        return wxId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.wx_id
     *
     * @param wxId the value for user.wx_id
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public void setWxId(String wxId) {
        this.wxId = wxId == null ? null : wxId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.image_url
     *
     * @return the value of user.image_url
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.image_url
     *
     * @param imageUrl the value for user.image_url
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.ranks
     *
     * @return the value of user.ranks
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public Long getRanks() {
        return ranks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.ranks
     *
     * @param ranks the value for user.ranks
     *
     * @mbggenerated Tue Jun 07 15:56:47 CST 2022
     */
    public void setRanks(Long ranks) {
        this.ranks = ranks;
    }
}