package com.meal.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MealOrderGoodsCalamity {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table meal_order_goods_calamity
     *
     * @mbg.generated
     */
    public static final Boolean IS_DELETED = Deleted.IS_DELETED.value();

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table meal_order_goods_calamity
     *
     * @mbg.generated
     */
    public static final Boolean NOT_DELETED = Deleted.NOT_DELETED.value();

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.order_id
     *
     * @mbg.generated
     */
    private Integer orderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.goods_id
     *
     * @mbg.generated
     */
    private Integer goodsId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.calamity_id
     *
     * @mbg.generated
     */
    private Integer calamityId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.calamity_name
     *
     * @mbg.generated
     */
    private String calamityName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.calamity_sn
     *
     * @mbg.generated
     */
    private String calamitySn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.number
     *
     * @mbg.generated
     */
    private Short number;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.price
     *
     * @mbg.generated
     */
    private BigDecimal price;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.pic_url
     *
     * @mbg.generated
     */
    private String picUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.comment
     *
     * @mbg.generated
     */
    private Integer comment;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.add_time
     *
     * @mbg.generated
     */
    private LocalDateTime addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.update_time
     *
     * @mbg.generated
     */
    private LocalDateTime updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.deleted
     *
     * @mbg.generated
     */
    private Boolean deleted;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_order_goods_calamity.version
     *
     * @mbg.generated
     */
    private Integer version;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.id
     *
     * @return the value of meal_order_goods_calamity.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.id
     *
     * @param id the value for meal_order_goods_calamity.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.order_id
     *
     * @return the value of meal_order_goods_calamity.order_id
     *
     * @mbg.generated
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.order_id
     *
     * @param orderId the value for meal_order_goods_calamity.order_id
     *
     * @mbg.generated
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.goods_id
     *
     * @return the value of meal_order_goods_calamity.goods_id
     *
     * @mbg.generated
     */
    public Integer getGoodsId() {
        return goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.goods_id
     *
     * @param goodsId the value for meal_order_goods_calamity.goods_id
     *
     * @mbg.generated
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.calamity_id
     *
     * @return the value of meal_order_goods_calamity.calamity_id
     *
     * @mbg.generated
     */
    public Integer getCalamityId() {
        return calamityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.calamity_id
     *
     * @param calamityId the value for meal_order_goods_calamity.calamity_id
     *
     * @mbg.generated
     */
    public void setCalamityId(Integer calamityId) {
        this.calamityId = calamityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.calamity_name
     *
     * @return the value of meal_order_goods_calamity.calamity_name
     *
     * @mbg.generated
     */
    public String getCalamityName() {
        return calamityName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.calamity_name
     *
     * @param calamityName the value for meal_order_goods_calamity.calamity_name
     *
     * @mbg.generated
     */
    public void setCalamityName(String calamityName) {
        this.calamityName = calamityName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.calamity_sn
     *
     * @return the value of meal_order_goods_calamity.calamity_sn
     *
     * @mbg.generated
     */
    public String getCalamitySn() {
        return calamitySn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.calamity_sn
     *
     * @param calamitySn the value for meal_order_goods_calamity.calamity_sn
     *
     * @mbg.generated
     */
    public void setCalamitySn(String calamitySn) {
        this.calamitySn = calamitySn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.number
     *
     * @return the value of meal_order_goods_calamity.number
     *
     * @mbg.generated
     */
    public Short getNumber() {
        return number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.number
     *
     * @param number the value for meal_order_goods_calamity.number
     *
     * @mbg.generated
     */
    public void setNumber(Short number) {
        this.number = number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.price
     *
     * @return the value of meal_order_goods_calamity.price
     *
     * @mbg.generated
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.price
     *
     * @param price the value for meal_order_goods_calamity.price
     *
     * @mbg.generated
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.pic_url
     *
     * @return the value of meal_order_goods_calamity.pic_url
     *
     * @mbg.generated
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.pic_url
     *
     * @param picUrl the value for meal_order_goods_calamity.pic_url
     *
     * @mbg.generated
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.comment
     *
     * @return the value of meal_order_goods_calamity.comment
     *
     * @mbg.generated
     */
    public Integer getComment() {
        return comment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.comment
     *
     * @param comment the value for meal_order_goods_calamity.comment
     *
     * @mbg.generated
     */
    public void setComment(Integer comment) {
        this.comment = comment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.add_time
     *
     * @return the value of meal_order_goods_calamity.add_time
     *
     * @mbg.generated
     */
    public LocalDateTime getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.add_time
     *
     * @param addTime the value for meal_order_goods_calamity.add_time
     *
     * @mbg.generated
     */
    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.update_time
     *
     * @return the value of meal_order_goods_calamity.update_time
     *
     * @mbg.generated
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.update_time
     *
     * @param updateTime the value for meal_order_goods_calamity.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods_calamity
     *
     * @mbg.generated
     */
    public void andLogicalDeleted(boolean deleted) {
        setDeleted(deleted ? Deleted.IS_DELETED.value() : Deleted.NOT_DELETED.value());
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.deleted
     *
     * @return the value of meal_order_goods_calamity.deleted
     *
     * @mbg.generated
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.deleted
     *
     * @param deleted the value for meal_order_goods_calamity.deleted
     *
     * @mbg.generated
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meal_order_goods_calamity.version
     *
     * @return the value of meal_order_goods_calamity.version
     *
     * @mbg.generated
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meal_order_goods_calamity.version
     *
     * @param version the value for meal_order_goods_calamity.version
     *
     * @mbg.generated
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods_calamity
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", IS_DELETED=").append(IS_DELETED);
        sb.append(", NOT_DELETED=").append(NOT_DELETED);
        sb.append(", id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", goodsId=").append(goodsId);
        sb.append(", calamityId=").append(calamityId);
        sb.append(", calamityName=").append(calamityName);
        sb.append(", calamitySn=").append(calamitySn);
        sb.append(", number=").append(number);
        sb.append(", price=").append(price);
        sb.append(", picUrl=").append(picUrl);
        sb.append(", comment=").append(comment);
        sb.append(", addTime=").append(addTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", version=").append(version);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods_calamity
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MealOrderGoodsCalamity other = (MealOrderGoodsCalamity) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getGoodsId() == null ? other.getGoodsId() == null : this.getGoodsId().equals(other.getGoodsId()))
            && (this.getCalamityId() == null ? other.getCalamityId() == null : this.getCalamityId().equals(other.getCalamityId()))
            && (this.getCalamityName() == null ? other.getCalamityName() == null : this.getCalamityName().equals(other.getCalamityName()))
            && (this.getCalamitySn() == null ? other.getCalamitySn() == null : this.getCalamitySn().equals(other.getCalamitySn()))
            && (this.getNumber() == null ? other.getNumber() == null : this.getNumber().equals(other.getNumber()))
            && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
            && (this.getPicUrl() == null ? other.getPicUrl() == null : this.getPicUrl().equals(other.getPicUrl()))
            && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods_calamity
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getGoodsId() == null) ? 0 : getGoodsId().hashCode());
        result = prime * result + ((getCalamityId() == null) ? 0 : getCalamityId().hashCode());
        result = prime * result + ((getCalamityName() == null) ? 0 : getCalamityName().hashCode());
        result = prime * result + ((getCalamitySn() == null) ? 0 : getCalamitySn().hashCode());
        result = prime * result + ((getNumber() == null) ? 0 : getNumber().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getPicUrl() == null) ? 0 : getPicUrl().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table meal_order_goods_calamity
     *
     * @mbg.generated
     */
    public enum Deleted {
        NOT_DELETED(new Boolean("0"), "未删除"),
        IS_DELETED(new Boolean("1"), "已删除");

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        private final Boolean value;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        private final String name;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        Deleted(Boolean value, String name) {
            this.value = value;
            this.name = name;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public Boolean getValue() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public Boolean value() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public String getName() {
            return this.name;
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table meal_order_goods_calamity
     *
     * @mbg.generated
     */
    public enum Column {
        id("id", "id", "INTEGER", false),
        orderId("order_id", "orderId", "INTEGER", false),
        goodsId("goods_id", "goodsId", "INTEGER", false),
        calamityId("calamity_id", "calamityId", "INTEGER", false),
        calamityName("calamity_name", "calamityName", "VARCHAR", false),
        calamitySn("calamity_sn", "calamitySn", "VARCHAR", false),
        number("number", "number", "SMALLINT", true),
        price("price", "price", "DECIMAL", false),
        picUrl("pic_url", "picUrl", "VARCHAR", false),
        comment("comment", "comment", "INTEGER", true),
        addTime("add_time", "addTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        deleted("deleted", "deleted", "BIT", false),
        version("version", "version", "INTEGER", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        Column(String column, String javaProperty, String jdbcType, boolean isColumnNameDelimited) {
            this.column = column;
            this.javaProperty = javaProperty;
            this.jdbcType = jdbcType;
            this.isColumnNameDelimited = isColumnNameDelimited;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public static Column[] excludes(Column ... excludes) {
            ArrayList<Column> columns = new ArrayList<>(Arrays.asList(Column.values()));
            if (excludes != null && excludes.length > 0) {
                columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));
            }
            return columns.toArray(new Column[]{});
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public String getEscapedColumnName() {
            if (this.isColumnNameDelimited) {
                return new StringBuilder().append(BEGINNING_DELIMITER).append(this.column).append(ENDING_DELIMITER).toString();
            } else {
                return this.column;
            }
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table meal_order_goods_calamity
         *
         * @mbg.generated
         */
        public String getAliasedEscapedColumnName() {
            return this.getEscapedColumnName();
        }
    }
}