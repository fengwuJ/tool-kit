package com.fengwuj.testdemo.common.mapper;

import org.apache.ibatis.type.TypeHandler;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;

import java.util.Map;

public class CustomizeExample extends Example {

    public CustomizeExample(Class<?> entityClass) {
        super(entityClass);
    }

    public CustomizeExample(Class<?> entityClass, boolean exists) {
        super(entityClass, exists);
    }

    public CustomizeExample(Class<?> entityClass, boolean exists, boolean notNull) {
        super(entityClass, exists, notNull);
    }

    public static class CustomizeCriteria extends Criteria {

        protected CustomizeCriteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
            super(propertyMap, exists, notNull);
        }

        //----------------- 方法重写区域

        public Criteria andEqualTo(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " =", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andNotEqualTo(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " <>", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andGreaterThan(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " >", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andGreaterThanOrEqualTo(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " >=", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andLessThan(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " <", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andLessThanOrEqualTo(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " <=", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andIn(String property, Iterable<?> values,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " in", values, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andNotIn(String property, Iterable<?> values,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " not in", values, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orBetween(String property, Object value1, Object value2,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " between", value1, value2, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andBetween(String property, Object value1, Object value2,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " between", value1, value2, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andNotBetween(String property, Object value1, Object value2,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + " not between", value1, value2, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andLike(String property, String value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + "  like", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andNotLike(String property, String value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion(this.column(property) + "  not like", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria andLikeInsensitive(String value,String tableProperty,String property) {
            this.addCriterion("upper("+tableProperty+") like", value.toUpperCase(), property);
            return  this;
        }

        public Criteria andLikeInsensitive(String value,String tableProperty,String property,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addCriterion("upper("+tableProperty+") like", value.toUpperCase(), property,typeHandlerClazz);
            return  this;
        }

        public Criteria orEqualTo(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " =", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orNotEqualTo(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " <>", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orGreaterThan(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " >", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orGreaterThanOrEqualTo(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " >=", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orLessThan(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " <", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orLessThanOrEqualTo(String property, Object value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " <=", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orIn(String property, Iterable<?> values,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " in", values, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orNotIn(String property, Iterable<?> values,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " not in", values, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orNotBetween(String property, Object value1, Object value2,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + " not between", value1, value2, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orLike(String property, String value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + "  like", value, this.property(property),typeHandlerClazz);
            return this;
        }

        public Criteria orNotLike(String property, String value,Class<? extends TypeHandler> typeHandlerClazz) {
            this.addOrCriterion(this.column(property) + "  not like", value, this.property(property),typeHandlerClazz);
            return this;
        }

        //--------------- 方法重写区域截止

        protected void addCriterion(String condition, Object value, String property,Class<? extends TypeHandler> typeHandlerClazz) {
            if (value == null) {
                if (this.notNull) {
                    throw new MapperException("属性:"+property+"不能为空");
                }
            } else if (property != null) {
                this.criteria.add(new CustomizeCriterion(condition, value,typeHandlerClazz.getName()));
            }
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property,Class<? extends TypeHandler> typeHandlerClazz) {
            if (value1 != null && value2 != null) {
                if (property != null) {
                    this.criteria.add(new CustomizeCriterion(condition, value1, value2,typeHandlerClazz.getName()));
                }
            } else if (this.notNull) {
                throw new MapperException("属性:"+property+"不能为空");
            }
        }

        protected void addOrCriterion(String condition, Object value, String property,Class<? extends TypeHandler> typeHandlerClazz) {
            if (value == null) {
                if (this.notNull) {
                    throw new MapperException("属性:"+property+"不能为空");
                }
            } else if (property != null) {
                this.criteria.add(new CustomizeCriterion(condition, value,typeHandlerClazz.getName(),true));
            }
        }

        protected void addOrCriterion(String condition, Object value1, Object value2, String property,Class<? extends TypeHandler> typeHandlerClazz) {
            if (value1 != null && value2 != null) {
                if (property != null) {
                    this.criteria.add(new CustomizeCriterion(condition, value1, value2,typeHandlerClazz.getName(),true));
                }
            } else if (this.notNull) {
                throw new MapperException("属性:"+property+"不能为空");
            }
        }

        private String column(String property) {
            if (this.propertyMap.containsKey(property)) {
                return this.propertyMap.get(property).getColumn();
            } else if (this.exists) {
                throw new MapperException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        private String property(String property) {
            if (this.propertyMap.containsKey(property)) {
                return property;
            } else if (this.exists) {
                throw new MapperException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }
    }

    @Override
    public CustomizeCriteria createCriteria() {
        CustomizeCriteria criteria = new CustomizeCriteria(this.propertyMap, this.exists, this.notNull);
        if (this.oredCriteria.isEmpty()) {
            criteria.setAndOr("and");
            this.oredCriteria.add(criteria);
        }

        return criteria;
    }

    public static class CustomizeCriterion extends Criterion{

        protected CustomizeCriterion(String condition, Object value, String typeHandler) {
            super(condition, value, typeHandler);
        }

        protected CustomizeCriterion(String condition, Object value,Object secondValue, String typeHandler) {
            super(condition, value,secondValue , typeHandler);
        }

        protected CustomizeCriterion(String condition, Object value, String typeHandler,boolean isOr) {
            super(condition, value, typeHandler,isOr);
        }

        protected CustomizeCriterion(String condition, Object value,Object secondValue, String typeHandler,boolean isOr) {
            super(condition, value,secondValue , typeHandler,isOr);
        }
    }

}