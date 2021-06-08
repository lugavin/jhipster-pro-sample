package com.gavin.myapp.anltr;

import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.gavin.myapp.service.CommonConditionQueryService;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class CriteriaLogicExprListener extends LogicExprBaseListener {

    public ParseTreeProperty<MergeSegments> specifications = new ParseTreeProperty<>();

    @Override
    public void exitOr(LogicExprParser.OrContext ctx) {
        MergeSegments left = specifications.get(ctx.expr(0));
        MergeSegments right = specifications.get(ctx.expr(1));
        if (left != null && right != null) {
            left.add(SqlKeyword.OR);
            left.add(right);
            specifications.put(ctx, left);
        }
        super.exitOr(ctx);
    }

    @Override
    public void exitStat(LogicExprParser.StatContext ctx) {
        specifications.put(ctx, specifications.get(ctx.expr()));
        super.exitStat(ctx);
    }

    @Override
    public void exitVar(LogicExprParser.VarContext ctx) {
        // 获得表达式指定的specification
        specifications.put(ctx, (MergeSegments) CommonConditionQueryService.specificationMap.get(ctx.VAR().getText()));
        super.exitVar(ctx);
    }

    @Override
    public void exitAnd(LogicExprParser.AndContext ctx) {
        MergeSegments left = specifications.get(ctx.expr(0));
        MergeSegments right = specifications.get(ctx.expr(1));
        if (left != null && right != null) {
            left.add(SqlKeyword.AND);
            left.add(right);
            specifications.put(ctx, left);
        }
        super.exitAnd(ctx);
    }

    @Override
    public void exitGroup(LogicExprParser.GroupContext ctx) {
        specifications.put(ctx, specifications.get(ctx.expr()));
        super.exitGroup(ctx);
    }
}
