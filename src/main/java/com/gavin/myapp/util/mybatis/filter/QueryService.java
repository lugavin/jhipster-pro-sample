package com.gavin.myapp.util.mybatis.filter;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.*;
import static java.util.stream.Collectors.joining;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import java.util.Collection;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.RangeFilter;
import tech.jhipster.service.filter.StringFilter;

public abstract class QueryService<ENTITY> extends DynamicJoinQueryWrapper<ENTITY, ENTITY> {

    public QueryService(Class<ENTITY> dtoClass, Collection<String> fields) {
        super(dtoClass, fields);
    }

    /*protected <X> QueryWrapper<ENTITY> buildSpecification(Filter<X> filter, String field) {
        return buildSpecification(filter, root -> root.get(field));
    }*/

    protected <X> void buildSpecification(Filter<X> filter, String field) {
        if (filter.getEquals() != null) {
            this.eq(field, filter.getEquals());
        } else if (filter.getIn() != null) {
            this.in(field, filter.getIn());
        } else if (filter.getNotEquals() != null) {
            this.ne(field, filter.getNotEquals());
        } else if (filter.getSpecified() != null) {
            if (filter.getSpecified()) {
                this.isNotNull(field);
            } else {
                this.isNull(field);
            }
        }
    }

    protected void buildStringSpecification(StringFilter filter, String field) {
        buildSpecification(filter, field);
    }

    protected void buildSpecification(StringFilter filter, String field) {
        if (filter.getEquals() != null) {
            this.eq(field, filter.getEquals());
        } else if (filter.getIn() != null) {
            this.in(field, filter.getIn());
        } else if (filter.getContains() != null) {
            this.like(field, filter.getContains());
        } else if (filter.getDoesNotContain() != null) {
            this.notLike(field, filter.getDoesNotContain());
        } else if (filter.getNotEquals() != null) {
            this.ne(field, filter.getNotEquals());
        } else if (filter.getSpecified() != null) {
            if (filter.getSpecified()) {
                this.isNotNull(field);
            } else {
                this.isNull(field);
            }
        }
    }

    protected <X extends Comparable<? super X>> void buildRangeSpecification(RangeFilter<X> filter, String field) {
        buildSpecification(filter, field);
    }

    protected <X extends Comparable<? super X>> void buildSpecification(RangeFilter<X> filter, String field) {
        if (filter.getEquals() != null) {
            this.eq(field, filter.getEquals());
        } else if (filter.getIn() != null) {
            this.in(field, filter.getIn());
        }
        if (filter.getSpecified() != null) {
            if (filter.getSpecified()) {
                this.isNotNull(field);
            } else {
                this.isNull(field);
            }
        }
        if (filter.getNotEquals() != null) {
            this.ne(field, filter.getNotEquals());
        }
        if (filter.getGreaterThan() != null) {
            this.gt(field, filter.getGreaterThan());
        }
        if (filter.getGreaterThanOrEqual() != null) {
            this.ge(field, filter.getGreaterThanOrEqual());
        }
        if (filter.getLessThan() != null) {
            this.lt(field, filter.getLessThan());
        }
        if (filter.getLessThanOrEqual() != null) {
            this.le(field, filter.getLessThanOrEqual());
        }
    }

    protected <X extends Comparable<? super X>> MergeSegments buildRangeMergeSegments(RangeFilter<X> filter, String field) {
        MergeSegments mergeSegments = new MergeSegments();
        if (filter.getEquals() != null) {
            mergeSegments.add(() -> String.valueOf(field), EQ, () -> String.valueOf(filter.getEquals()));
        } else if (filter.getIn() != null) {
            mergeSegments.add(() -> String.valueOf(field), IN, inExpression(filter.getIn()));
        }
        if (filter.getSpecified() != null) {
            if (filter.getSpecified()) {
                this.isNotNull(field);
            } else {
                this.isNull(field);
            }
        }
        if (filter.getNotEquals() != null) {
            mergeSegments.add(() -> String.valueOf(field), NE, () -> String.valueOf(filter.getNotEquals()));
        }
        if (filter.getGreaterThan() != null) {
            mergeSegments.add(() -> String.valueOf(field), GT, () -> String.valueOf(filter.getGreaterThan()));
        }
        if (filter.getGreaterThanOrEqual() != null) {
            mergeSegments.add(() -> String.valueOf(field), GE, () -> String.valueOf(filter.getGreaterThanOrEqual()));
        }
        if (filter.getLessThan() != null) {
            mergeSegments.add(() -> String.valueOf(field), LT, () -> String.valueOf(filter.getGreaterThanOrEqual()));
        }
        if (filter.getLessThanOrEqual() != null) {
            mergeSegments.add(() -> String.valueOf(field), LE, () -> String.valueOf(filter.getGreaterThanOrEqual()));
        }
        return mergeSegments;
    }

    protected MergeSegments buildStringMergeSegments(StringFilter filter, String field) {
        MergeSegments mergeSegments = new MergeSegments();
        if (filter.getEquals() != null) {
            mergeSegments.add(() -> String.valueOf(field), EQ, filter::getEquals);
        } else if (filter.getIn() != null) {
            mergeSegments.add(() -> String.valueOf(field), IN, inExpression(filter.getIn()));
        } else if (filter.getContains() != null) {
            mergeSegments.add(() -> String.valueOf(field), LIKE, filter::getContains);
        } else if (filter.getDoesNotContain() != null) {
            mergeSegments.add(() -> String.valueOf(field), NOT_LIKE, filter::getDoesNotContain);
        } else if (filter.getNotEquals() != null) {
            mergeSegments.add(() -> String.valueOf(field), NE, filter::getNotEquals);
        } else if (filter.getSpecified() != null) {
            if (filter.getSpecified()) {
                this.isNotNull(field);
            } else {
                this.isNull(field);
            }
        }
        return mergeSegments;
    }

    protected <X> MergeSegments buildMergeSegments(Filter<X> filter, String field) {
        MergeSegments mergeSegments = new MergeSegments();
        if (filter.getEquals() != null) {
            mergeSegments.add(() -> String.valueOf(field), EQ, () -> String.valueOf(filter.getEquals()));
        } else if (filter.getIn() != null) {
            mergeSegments.add(() -> String.valueOf(field), IN, inExpression(filter.getIn()));
        } else if (filter.getNotEquals() != null) {
            mergeSegments.add(() -> String.valueOf(field), NE, () -> String.valueOf(filter.getNotEquals()));
        } else if (filter.getSpecified() != null) {
            if (filter.getSpecified()) {
                this.isNotNull(field);
            } else {
                this.isNull(field);
            }
        }
        return mergeSegments;
    }

    /*protected <OTHER, X> Specification<ENTITY> buildReferringEntitySpecification(Filter<X> filter,
                                                                                 SingularAttribute<? super ENTITY, OTHER> reference,
                                                                                 SingularAttribute<? super OTHER, X> valueField) {
        return buildSpecification(filter, root -> root.get(reference).get(valueField));
    }*/

    /*protected <OTHER, X> Specification<ENTITY> buildReferringEntitySpecification(Filter<X> filter,
                                                                                 SetAttribute<ENTITY, OTHER> reference,
                                                                                 SingularAttribute<OTHER, X> valueField) {
        return buildReferringEntitySpecification(filter, root -> root.join(reference), entity -> entity.get(valueField));
    }*/

    /*protected <OTHER, MISC, X> Specification<ENTITY> buildReferringEntitySpecification(Filter<X> filter,
                                                                                       Function<Root<ENTITY>, SetJoin<MISC, OTHER>> functionToEntity,
                                                                                       Function<SetJoin<MISC, OTHER>, Expression<X>> entityToColumn) {
        if (filter.getEquals() != null) {
            return equalsSpecification(functionToEntity.andThen(entityToColumn), filter.getEquals());
        } else if (filter.getSpecified() != null) {
            // Interestingly, 'functionToEntity' doesn't work, we need the longer lambda formula
            return byFieldSpecified(root -> functionToEntity.apply(root), filter.getSpecified());
        }
        return null;
    }*/

    /*protected <OTHER, X extends Comparable<? super X>> Specification<ENTITY> buildReferringEntitySpecification(final RangeFilter<X> filter,
                                                                                                               final SetAttribute<ENTITY, OTHER> reference,
                                                                                                               final SingularAttribute<OTHER, X> valueField) {
        return buildReferringEntitySpecification(filter, root -> root.join(reference), entity -> entity.get(valueField));
    }*/

    /*protected <OTHER, MISC, X extends Comparable<? super X>> QueryWrapper<ENTITY> buildReferringEntitySpecification(final RangeFilter<X> filter,
                                                                                                                     Function<Root<ENTITY>, SetJoin<MISC, OTHER>> functionToEntity,
                                                                                                                     Function<SetJoin<MISC, OTHER>, Expression<X>> entityToColumn) {

        Function<Root<ENTITY>, Expression<X>> fused = functionToEntity.andThen(entityToColumn);
        if (filter.getEquals() != null) {
            return equalsSpecification(fused, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(fused, filter.getIn());
        }
        Specification<ENTITY> result = Specification.where(null);
        if (filter.getSpecified() != null) {
            // Interestingly, 'functionToEntity' doesn't work, we need the longer lambda formula
            result = result.and(byFieldSpecified(root -> functionToEntity.apply(root), filter.getSpecified()));
        }
        if (filter.getNotEquals() != null) {
            result = result.and(notEqualsSpecification(fused, filter.getNotEquals()));
        }
        if (filter.getGreaterThan() != null) {
            result = result.and(greaterThan(fused, filter.getGreaterThan()));
        }
        if (filter.getGreaterThanOrEqual() != null) {
            result = result.and(greaterThanOrEqualTo(fused, filter.getGreaterThanOrEqual()));
        }
        if (filter.getLessThan() != null) {
            result = result.and(lessThan(fused, filter.getLessThan()));
        }
        if (filter.getLessThanOrEqual() != null) {
            result = result.and(lessThanOrEqualTo(fused, filter.getLessThanOrEqual()));
        }
        return result;
    }*/

    /* protected <X> QueryWrapper<ENTITY> notEqualsSpecification(String field, final X value) {
        return new QueryWrapper<ENTITY>().ne(field, value);
    }

    protected QueryWrapper<ENTITY> likeUpperSpecification(String field, final String value) {
        return new QueryWrapper<ENTITY>().like(field, wrapLikeQuery(value));
    }

    protected QueryWrapper<ENTITY> doesNotContainSpecification(String field,
                                                                final String value) {
        return new QueryWrapper<ENTITY>().notLike(field, wrapLikeQuery(value));
    }

    protected <X> QueryWrapper<ENTITY> byFieldSpecified(String field, final boolean specified) {
        return specified ?
            new QueryWrapper<ENTITY>().isNotNull(field) :
            new QueryWrapper<ENTITY>().isNull(field);
    } */

    /*protected QueryWrapper<ENTITY> byFieldEmptiness(String field,
                                                         final boolean specified) {
        return specified ?
            new QueryWrapper<ENTITY>().isEmptyOfEntity().isNotEmpty(metaclassFunction.apply(root)) :
            (root, query, builder) -> builder.isEmpty(metaclassFunction.apply(root));
    }*/

    /* protected <X> QueryWrapper<ENTITY> valueIn(String field, final Collection<X> values) {
        return new QueryWrapper<ENTITY>().in(field, values);
    }

    protected <X extends Comparable<? super X>> QueryWrapper<ENTITY> greaterThanOrEqualTo(String field, final X value) {
        return new QueryWrapper<ENTITY>().ge(field, value);
    }

    protected <X extends Comparable<? super X>> QueryWrapper<ENTITY> greaterThan(String field, final X value) {
        return new QueryWrapper<ENTITY>().gt(field, value);
    }

    protected <X extends Comparable<? super X>> QueryWrapper<ENTITY> lessThanOrEqualTo(String field, final X value) {
        return new QueryWrapper<ENTITY>().le(field, value);
    }

    protected <X> QueryWrapper<ENTITY> equalsSpecification(String field, final X value) {
        return new QueryWrapper<ENTITY>().eq(field, value);
    }

    protected <X extends Comparable<? super X>> QueryWrapper<ENTITY> lessThan(String field, final X value) {
        return new QueryWrapper<ENTITY>().lt(field, value);
    }

    protected String wrapLikeQuery(String txt) {
        return "%" + txt.toUpperCase() + '%';
    } */

    /**
     * 获取in表达式 包含括号
     *
     * @param value 集合
     */
    private ISqlSegment inExpression(Collection<?> value) {
        return () ->
            value
                .stream()
                .map(i -> formatSql("{0}", i))
                .collect(joining(StringPool.COMMA, StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET));
    }
}
