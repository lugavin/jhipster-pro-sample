package com.gavin.myapp.util.mybatis.handler;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

@Component
public class FillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Field[] fields = metaObject.getOriginalObject().getClass().getDeclaredFields();
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof TableField) {
                    if (
                        ((TableField) annotation).fill().equals(FieldFill.INSERT_UPDATE) ||
                        ((TableField) annotation).fill().equals(FieldFill.INSERT)
                    ) {
                        if (ZonedDateTime.class.equals(field.getType())) {
                            this.strictInsertFill(metaObject, field.getName(), ZonedDateTime.class, ZonedDateTime.now());
                        } else if (LocalDateTime.class.equals(field.getType())) {
                            this.strictInsertFill(metaObject, field.getName(), LocalDateTime.class, LocalDateTime.now());
                        } else if (Instant.class.equals(field.getType())) {
                            this.strictInsertFill(metaObject, field.getName(), Instant.class, Instant.now());
                        }
                    }
                }
            }
        }
        // jhipster-needle-fill-insert-method - JHipster will add getters and setters here, do not remove
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Field[] fields = metaObject.getOriginalObject().getClass().getDeclaredFields();
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof TableField) {
                    if (
                        ((TableField) annotation).fill().equals(FieldFill.INSERT_UPDATE) ||
                        ((TableField) annotation).fill().equals(FieldFill.UPDATE)
                    ) {
                        if (ZonedDateTime.class.equals(field.getType())) {
                            this.strictUpdateFill(metaObject, field.getName(), ZonedDateTime.class, ZonedDateTime.now());
                        } else if (LocalDateTime.class.equals(field.getType())) {
                            this.strictUpdateFill(metaObject, field.getName(), LocalDateTime.class, LocalDateTime.now());
                        } else if (Instant.class.equals(field.getType())) {
                            this.strictUpdateFill(metaObject, field.getName(), Instant.class, Instant.now());
                        }
                    }
                }
            }
        }
        // jhipster-needle-fill-update-method - JHipster will add getters and setters here, do not remove
    }
}
